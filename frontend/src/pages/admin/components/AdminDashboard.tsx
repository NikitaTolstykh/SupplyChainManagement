import React from 'react';
import { Outlet, Link, useLocation } from 'react-router-dom';
import { useAuthStore } from '../../../store/authStore.ts';
import Button from "../../../components/ui/Button.tsx";

const AdminDashboard: React.FC = () => {
    const location = useLocation();
    const { user, logout } = useAuthStore();

    const navigationItems = [
        { path: '/admin/workers', label: 'Workers', icon: '👥' },
        { path: '/admin/vehicles', label: 'Vehicles', icon: '🚗' },
    ];

    const isActiveRoute = (path: string) => {
        return location.pathname === path || (path === '/admin/workers' && location.pathname === '/admin');
    };

    return (
        <div className="min-h-screen bg-gray-50">
            {/* Header */}
            <header className="bg-white shadow-sm border-b">
                <div className="flex items-center justify-between px-6 py-4">
                    <div className="flex items-center space-x-4">
                        <h1 className="text-2xl font-bold text-gray-900">Admin Dashboard</h1>
                        <span className="text-sm text-gray-500">
                            Welcome, {user?.email}
                        </span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <span className="text-sm text-gray-600">
                            Role: {user?.role}
                        </span>
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
                                                ? 'bg-blue-50 text-blue-700 border-r-2 border-blue-600'
                                                : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'
                                        }`}
                                    >
                                        <span className="text-lg">{item.icon}</span>
                                        <span>{item.label}</span>
                                    </Link>
                                </li>
                            ))}
                        </ul>
                    </nav>

                    {/* Stats Section */}
                    <div className="mt-8 px-4">
                        <div className="bg-gray-50 rounded-lg p-4">
                            <h3 className="text-sm font-medium text-gray-900 mb-3">
                                Quick Stats
                            </h3>
                            <div className="space-y-2 text-sm text-gray-600">
                                <div className="flex justify-between">
                                    <span>Total Workers:</span>
                                    <span className="font-medium">--</span>
                                </div>
                                <div className="flex justify-between">
                                    <span>Total Vehicles:</span>
                                    <span className="font-medium">--</span>
                                </div>
                                <div className="flex justify-between">
                                    <span>Active Drivers:</span>
                                    <span className="font-medium">--</span>
                                </div>
                            </div>
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

export default AdminDashboard;