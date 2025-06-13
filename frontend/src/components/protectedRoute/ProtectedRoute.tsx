import React from 'react';
import { Navigate } from 'react-router-dom';
import { getCookie, TRADER_ID_TOKEN, TRADER_NAME_COOKIE } from '../../common/utils/utils';

const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const traderId = getCookie(TRADER_ID_TOKEN);
  const traderName = getCookie(TRADER_NAME_COOKIE);

  if (!traderId || !traderName) {
    return <Navigate to="/login" replace />;
  }

  return <>{children}</>;
};

export default ProtectedRoute;