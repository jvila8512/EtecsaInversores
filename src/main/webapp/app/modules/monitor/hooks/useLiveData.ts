import { useEffect, useRef, useCallback, useState } from 'react';
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
import { Storage } from 'react-jhipster';
import type { LiveDataMap, WsStatus } from '../types';
import type { IDatoInversor } from 'app/shared/model/dato-inversor.model';
import type { IEventoAlerta } from 'app/shared/model/evento-alerta.model';

interface UseLiveDataOptions {
  inversorIds: number[];
  onAlert?: (alerta: IEventoAlerta) => void;
}

interface UseLiveDataResult {
  liveData: LiveDataMap;
  wsStatus: WsStatus;
  reconnect: () => void;
}

export function useLiveData({ inversorIds, onAlert }: UseLiveDataOptions): UseLiveDataResult {
  const [liveData, setLiveData] = useState<LiveDataMap>({});
  const [wsStatus, setWsStatus] = useState<WsStatus>('disconnected');
  const stompClientRef = useRef<any>(null);
  const subscriptionsRef = useRef<any[]>([]);
  const reconnectTimerRef = useRef<any>(null);

  const disconnect = useCallback(() => {
    subscriptionsRef.current.forEach(sub => {
      try {
        sub.unsubscribe();
      } catch (_e) {
        // already disconnected
      }
    });
    subscriptionsRef.current = [];
    if (stompClientRef.current?.connected) {
      try {
        stompClientRef.current.disconnect();
      } catch (_e) {
        // ignore
      }
    }
    stompClientRef.current = null;
    if (reconnectTimerRef.current) {
      clearTimeout(reconnectTimerRef.current);
      reconnectTimerRef.current = null;
    }
  }, []);

  const connect = useCallback(() => {
    if (stompClientRef.current?.connected) return;

    const loc = window.location;
    const baseHref = document.querySelector('base')?.getAttribute('href')?.replace(/\/$/, '') || '';
    let url = `//${loc.host}${baseHref}/websocket/tracker`;
    const authToken = Storage.local.get('jhi-authenticationToken') || Storage.session.get('jhi-authenticationToken');
    if (authToken) {
      url += `?access_token=${authToken}`;
    }

    const socket = new SockJS(url);
    const client = Stomp.over(socket, { protocols: ['v12.stomp'] });
    client.debug = () => {}; // silence stomp debug

    const headers: Record<string, string> = {};

    client.connect(
      headers,
      () => {
        setWsStatus('connected');

        // Subscribe to each inversor's live topic
        inversorIds.forEach(id => {
          try {
            const sub = client.subscribe(`/topic/inversor/${id}/live`, msg => {
              try {
                const dato: IDatoInversor = JSON.parse(msg.body);
                setLiveData(prev => ({ ...prev, [id]: dato }));
              } catch (_e) {
                // ignore parse errors
              }
            });
            subscriptionsRef.current.push(sub);
          } catch (_e) {
            // ignore subscription errors
          }
        });

        // Subscribe to global alerts
        try {
          const alertSub = client.subscribe('/topic/alertas', msg => {
            try {
              const alerta: IEventoAlerta = JSON.parse(msg.body);
              onAlert?.(alerta);
            } catch (_e) {
              // ignore
            }
          });
          subscriptionsRef.current.push(alertSub);
        } catch (_e) {
          // ignore
        }
      },
      () => {
        // on error / close
        setWsStatus('reconnecting');
        stompClientRef.current = null;
        // Auto-reconnect after 5 seconds
        reconnectTimerRef.current = setTimeout(() => {
          setWsStatus('disconnected');
          connect();
        }, 5000);
      },
    );

    stompClientRef.current = client;
  }, [inversorIds, onAlert]);

  useEffect(() => {
    connect();
    return () => {
      disconnect();
    };
  }, [connect, disconnect]);

  // Re-subscribe when inversorIds change
  useEffect(() => {
    if (stompClientRef.current?.connected) {
      // Unsubscribe old
      subscriptionsRef.current.forEach(sub => {
        try {
          sub.unsubscribe();
        } catch (_e) {
          // ignore
        }
      });
      subscriptionsRef.current = [];

      // Subscribe new
      inversorIds.forEach(id => {
        try {
          const sub = stompClientRef.current.subscribe(`/topic/inversor/${id}/live`, msg => {
            try {
              const dato: IDatoInversor = JSON.parse(msg.body);
              setLiveData(prev => ({ ...prev, [id]: dato }));
            } catch (_e) {
              // ignore
            }
          });
          subscriptionsRef.current.push(sub);
        } catch (_e) {
          // ignore
        }
      });

      try {
        const alertSub = stompClientRef.current.subscribe('/topic/alertas', msg => {
          try {
            const alerta: IEventoAlerta = JSON.parse(msg.body);
            onAlert?.(alerta);
          } catch (_e) {
            // ignore
          }
        });
        subscriptionsRef.current.push(alertSub);
      } catch (_e) {
        // ignore
      }
    }
  }, [inversorIds, onAlert]);

  return { liveData, wsStatus, reconnect: connect };
}
