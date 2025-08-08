import React from "react";
import { Outlet, Link, useLocatione} from "react-router-dom";
import {useAuthStore} from "../../store/authStore.ts";
import Button from "../../components/ui/Button.tsx";
import {useClientStats} from "../../hooks/client/useClientStats.ts";

const ClientDashboard: React.FC = () => {
    const location = useLocation();
    const { user, logout } = useAuthStore();
    const { data: stats, isLoading, isError } = useClientStats();

    const navigationItems = [
        { path: "/client/dashboard", label: "Dashboard", icon: "📊" },
        { path: "/client/orders", label: "Orders", icon: "📦" },
        { path: "/client/create-order", label: "Create Order", icon: "➕" },
    ];

    const isActiveRoute = (path: string) => location.pathname === path;

    return (
        <div className="min-h-screen bg-gray-50">
            {/* Header */}
            <header className="bg-white shadow-sm border-b">
                <div className="flex items-center justify-between px-6 py-4">
                    <div className="flex items-center space-x-4">
                        <h1 className="text-2xl font-bold text-gray-900">Client Dashboard</h1>
                        <span className="text-sm text-gray-500">Welcome, {user?.email}</span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <span className="text-sm text-gray-600">Role: {user?.role}</span>
                        <Button variant="secondary" onClick={logout}>
                            Logout
                        </Button>
                    </div>
                </div>
            </header>

            <div className="flex">
                {/* Sidebar */}
                <aside className="w-64 bg-white shadow-sm min-h-screen">
                    <nav className="mt-6 px-4">
                        <ul className="space-y-2">
                            {navigationItems.map((item) => (
                                <li key={item.path}>
                                    <Link
                                        to={item.path}
                                        className={`flex items-center space-x-3 px-4 py-3 rounded-lg text-sm font-medium transition-colors ${
                                            isActiveRoute(item.path)
                                                ? "bg-blue-50 text-blue-700 border-r-2 border-blue-600"
                                                : "text-gray-600 hover:bg-gray-50 hover:text-gray-900"
                                        }`}
                                    >
                                        <span className="text-lg">{item.icon}</span>
                                        <span>{item.label}</span>
                                    </Link>
                                </li>
                            ))}
                        </ul>
                    </nav>

                    {/* Quick Stats */}
                    <div className="mt-8 px-4">
                        <div className="bg-gray-50 rounded-lg p-4">
                            <h3 className="text-sm font-medium text-gray-900 mb-3">Quick Stats</h3>

                            {isLoading && (
                                <div className="text-gray-600 text-sm">Loading statistics...</div>
                            )}

                            {isError && (
                                <div className="text-red-600 text-sm">
                                    Error loading statistics
                                </div>
                            )}

                            {!isLoading && !isError && stats && (
                                <div className="space-y-2 text-sm text-gray-600">
                                    <div className="flex justify-between">
                                        <span>Total Orders:</span>
                                        <span className="font-medium">{stats.totalOrders}</span>
                                    </div>
                                    <div className="flex justify-between">
                                        <span>Active Orders:</span>
                                        <span className="font-medium">{stats.activeOrders}</span>
                                    </div>
                                    <div className="flex justify-between">
                                        <span>Completed Orders:</span>
                                        <span className="font-medium">{stats.completedOrders}</span>
                                    </div>
                                </div>
                            )}
                        </div>
                    </div>
                </aside>

                {/* Main Content */}
                <main className="flex-1 p-6">
                    <Outlet />
                </main>
            </div>
        </div>
    );
};

export default ClientDashboard;
