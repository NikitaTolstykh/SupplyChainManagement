import {Routes, Route, Navigate} from 'react-router-dom';
import LoginPage from "../pages/auth/LoginPage.tsx";
import RegisterPage from "../pages/auth/RegisterPage.tsx";
import AdminDashboard from "../pages/admin/AdminDashboard.tsx";
import VehiclesPage from "../pages/admin/VehiclesPage.tsx";
import WorkersPage from "../pages/admin/WorkersPage.tsx";
import ProtectedRoute from "./ProtectedRoute.tsx";
import { RoleValues } from "../lib/types/Role";

const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/" element={<Navigate to="/login" replace />} />

            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />


            <Route path="/unauthorized" element={
                <div className="min-h-screen flex items-center justify-center">
                    <div className="text-center">
                        <h1 className="text-2xl font-bold text-red-600 mb-4">Access Denied</h1>
                        <p className="text-gray-600 mb-4">You don't have permission to access this page.</p>
                        <a href="/login" className="text-blue-600 hover:text-blue-700">Go to Login</a>
                    </div>
                </div>
            } />

            <Route
                path="/admin"
                element={
                    <ProtectedRoute requiredRole={RoleValues.ADMIN}>
                        <AdminDashboard />
                    </ProtectedRoute>
                }
            >
                <Route index element={<WorkersPage />} />
                <Route path="workers" element={<WorkersPage />} />
                <Route path="vehicles" element={<VehiclesPage />} />
            </Route>


            <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
    );
};

export default AppRoutes;