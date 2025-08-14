import { Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "../pages/auth/LoginPage.tsx";
import RegisterPage from "../pages/auth/RegisterPage.tsx";
import AdminDashboard from "../pages/admin/components/AdminDashboard.tsx";
import VehiclesPage from "../pages/admin/VehiclesPage.tsx";
import WorkersPage from "../pages/admin/WorkersPage.tsx";
import ClientDashboard from "../pages/client/ClientDashboard.tsx";
import DashboardOverview from "../pages/client/DashboardOverview.tsx";
import OrdersListPage from "../pages/client/OrdersListPage.tsx";
import CreateOrderPage from "../pages/client/CreateOrderPage.tsx";
import OrderDetailsPage from "../pages/client/OrderDetailsPage.tsx";
import RatingPage from "../pages/client/RatingPage.tsx";
import ProtectedRoute from "./ProtectedRoute.tsx";
import DispatcherDashboard from "../pages/dispatcher/DispatcherDashboard.tsx";
import OrdersManagementPage from "../pages/dispatcher/OrdersManagementPage.tsx";
import RatingsPage from "../pages/dispatcher/RatingsPage.tsx";
import DriverDashboard from "../pages/driver/DriverDashboard.tsx";
import AssignedOrdersPage from "../pages/driver/AssignedOrdersPage.tsx";
import CompletedOrdersPage from "../pages/driver/CompletedOrdersPage.tsx";
import { RoleValues } from "../lib/types/Role";

const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/" element={<Navigate to="/login" replace />} />

            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />

            <Route
                path="/unauthorized"
                element={
                    <div className="min-h-screen flex items-center justify-center">
                        <div className="text-center">
                            <h1 className="text-2xl font-bold text-red-600 mb-4">
                                Access Denied
                            </h1>
                            <p className="text-gray-600 mb-4">
                                You don't have permission to access this page.
                            </p>
                            <a href="/login" className="text-blue-600 hover:text-blue-700">
                                Go to Login
                            </a>
                        </div>
                    </div>
                }
            />

            {/* Admin routes */}
            <Route
                path="/admin/*"
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

            {/* Client routes */}
            <Route
                path="/client/*"
                element={
                    <ProtectedRoute requiredRole={RoleValues.CLIENT}>
                        <ClientDashboard />
                    </ProtectedRoute>
                }
            >
                <Route index element={<DashboardOverview />} />
                <Route path="orders" element={<OrdersListPage />} />
                <Route path="create-order" element={<CreateOrderPage />} />
                <Route path="orders/:orderId" element={<OrderDetailsPage />} />
                <Route path="rating/:orderId" element={<RatingPage />} />
            </Route>

            {/* Dispatcher routes */}
            <Route
                path="/dispatcher/*"
                element={
                    <ProtectedRoute requiredRole={RoleValues.DISPATCHER}>
                        <DispatcherDashboard />
                    </ProtectedRoute>
                }
            >
                <Route index element={<OrdersManagementPage />} />
                <Route path="ratings" element={<RatingsPage />} />
            </Route>

            {/* Driver routes */}
            <Route
                path="/driver/*"
                element={
                    <ProtectedRoute requiredRole={RoleValues.DRIVER}>
                        <DriverDashboard />
                    </ProtectedRoute>
                }
            >
                <Route index element={<AssignedOrdersPage />} />
                <Route path="completed" element={<CompletedOrdersPage />} />
            </Route>

            {/* Fallback */}
            <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
    );
};

export default AppRoutes;