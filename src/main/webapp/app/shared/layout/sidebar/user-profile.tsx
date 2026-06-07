import React from 'react';
import { Avatar, Box } from '@mui/material';
import PersonIcon from '@mui/icons-material/Person';
import { useAppSelector } from 'app/config/store';

interface UserProfileProps {
  collapsed: boolean;
  isMobile: boolean;
}

export const UserProfile: React.FC<UserProfileProps> = ({ collapsed, isMobile }) => {
  const account = useAppSelector(state => state.authentication.account);

  if (collapsed && !isMobile) return null;

  const initials = account?.firstName
    ? account.firstName.charAt(0).toUpperCase()
    : account?.login
      ? account.login.charAt(0).toUpperCase()
      : '?';

  return (
    <Avatar
      sx={{
        width: 48,
        height: 48,
        bgcolor: 'rgba(255, 255, 255, 0.2)',
        border: '2px solid rgba(255, 255, 255, 0.3)',
        fontSize: '1.2rem',
        fontWeight: 600,
      }}
    >
      {initials || <PersonIcon />}
    </Avatar>
  );
};

interface ProfileInfoProps {
  collapsed: boolean;
  isMobile: boolean;
}

export const ProfileInfo: React.FC<ProfileInfoProps> = ({ collapsed, isMobile }) => {
  const account = useAppSelector(state => state.authentication.account);

  if (collapsed && !isMobile) return null;

  return (
    <>
      <p className="user-name">
        {account?.firstName} {account?.lastName}
      </p>
      <p className="user-role">{account?.authorities?.map(auth => auth.replace('ROLE_', '')).join(', ')}</p>
    </>
  );
};
