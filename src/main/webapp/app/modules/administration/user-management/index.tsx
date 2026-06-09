import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import UserManagement from './user-management';

const UserManagementRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserManagement />} />
  </ErrorBoundaryRoutes>
);

export default UserManagementRoutes;
