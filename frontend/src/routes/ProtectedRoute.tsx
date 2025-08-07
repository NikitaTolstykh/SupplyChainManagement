import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';
import type {Role} from '../lib/types/Role';

interface ProtectedRouteProps {
    children: React.ReactNode;
    requiredRole?: Role;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children, requiredRole }) => {
    const { isAuthenticated, user } = useAuthStore();

    console.log('ProtectedRoute - isAuthenticated:', isAuthenticated);
    console.log('ProtectedRoute - user:', user);
    console.log('ProtectedRoute - requiredRole:', requiredRole);

    if (!isAuthenticated) {
        console.log('Not authenticated, redirecting to login');
        return <Navigate to="/login" replace />;
    }

    if (requiredRole && user?.role !== requiredRole) {
        console.log(`Role mismatch: ${user?.role} !== ${requiredRole}, redirecting to unauthorized`);
        return <Navigate to="/unauthorized" replace />;
    }

    console.log('Access granted, rendering children');
    return <>{children}</>;
};

export default ProtectedRoute;