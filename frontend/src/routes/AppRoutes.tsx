import { Routes, Route } from 'react-router-dom';
import LoginPage from "../pages/auth/LoginPage.tsx";
import RegisterPage from "../pages/auth/RegisterPage.tsx";
import AdminDashboard from "../pages/admin/AdminDashboard.tsx";
import VehiclesPage from "../pages/admin/VehiclesPage.tsx";
import WorkersPage from "../pages/admin/WorkersPage.tsx";
import ProtectedRoute from "../components/ProtectedRoute";
import { Role } from "../lib/types/Role";

const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />

            <Route
                path="/admin"
                element={
                    <ProtectedRoute requiredRole={Role.ADMIN}>
                        <AdminDashboard />
                    </ProtectedRoute>
                }
            >
                <Route index element={<WorkersPage />} />
                <Route path="workers" element={<WorkersPage />} />
                <Route path="vehicles" element={<VehiclesPage />} />
            </Route>

        </Routes>
    );
};

export default AppRoutes;
