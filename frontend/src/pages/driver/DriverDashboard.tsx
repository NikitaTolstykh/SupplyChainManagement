import React from 'react';
import {Outlet, Link, useLocation} from 'react-router-dom';
import {useAuthStore} from '../../store/authStore';
import {useAssignedOrders, useCompletedOrders} from "../../hooks/driver/useDriverOrdersHook.ts";
import Button from '../../components/ui/Button';

const DriverDashboard: React.FC = () => {
    const location = useLocation();
    const {user, logout} = useAuthStore();
    const {data: assignedOrders = []} = useAssignedOrders();
    const {data: completedOrders = []} = useCompletedOrders();

    const navigationItems = [
        {
            path: "/driver",
            label: "Assigned Orders",
            icon: "📋",
            count: assignedOrders.length,
            countColor: "bg-yellow-100 text-yellow-800"
        },
        {
            path: "/driver/completed",
            label: "Completed Orders",
            icon: "✅",
            count: completedOrders.length,
            countColor: "bg-green-100 text-green-800"
        },
    ];

    const isActiveRoute = (path: string) => {
        if (path === "/driver") {
            return location.pathname === "/driver" || location.pathname === "/driver/";
        }
        return location.pathname === path;
    };

    // Count urgent orders (pickup time within 2 hours)
    const urgentOrders = assignedOrders.filter(order => {
        const pickupTime = new Date(order.pickupTime);
        const now = new Date();
        const hoursDiff = (pickupTime.getTime() - now.getTime()) / (1000 * 60 * 60);
        return hoursDiff <= 2 && hoursDiff >= 0;
    }).length;

    // Count in-progress orders
    const inProgressOrders = assignedOrders.filter(order => order.status === 'IN_PROGRESS').length;

    return (
        <div className="min-h-screen bg-gray-50">
            {/* Header */}
            <header className="bg-white shadow-sm border-b">
                <div className="flex items-center justify-between px-6 py-4">
                    <div className="flex items-center space-x-4">
                        <h1 className="text-2xl font-bold text-gray-900">Driver Dashboard</h1>
                        <span className="text-sm text-gray-500">Welcome, {user?.email}</span>
                    </div>

                    <div className="flex items-center space-x-3">
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
                                        className={`flex items-center justify-between px-4 py-3 rounded-lg text-sm font-medium transition-colors ${
                                            isActiveRoute(item.path)
                                                ? "bg-blue-50 text-blue-700 border-r-2 border-blue-600"
                                                : "text-gray-600 hover:bg-gray-50 hover:text-gray-900"
                                        }`}
                                    >
                                        <div className="flex items-center space-x-3">
                                            <span className="text-lg">{item.icon}</span>
                                            <span>{item.label}</span>
                                        </div>
                                        {item.count > 0 && (
                                            <span
                                                className={`px-2 py-1 rounded-full text-xs font-medium ${item.countColor}`}>
                                                {item.count}
                                            </span>
                                        )}
                                    </Link>
                                </li>
                            ))}
                        </ul>
                    </nav>

                    {/* Quick Stats */}
                    <div className="mt-6 px-4 space-y-3">
                        {/* Urgent Orders Alert */}
                        {urgentOrders > 0 && (
                            <div className="bg-red-50 border border-red-200 rounded-lg p-4">
                                <div className="flex items-center mb-2">
                                    <span className="text-red-600 text-lg mr-2">🚨</span>
                                    <span className="text-sm font-medium text-red-800">Urgent Orders</span>
                                </div>
                                <p className="text-xs text-red-700">
                                    {urgentOrders} order{urgentOrders !== 1 ? 's' : ''} need{urgentOrders === 1 ? 's' : ''} immediate
                                    attention
                                </p>
                            </div>
                        )}

                        {/* In Progress Orders */}
                        {inProgressOrders > 0 && (
                            <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
                                <div className="flex items-center mb-2">
                                    <span className="text-blue-600 text-lg mr-2">🚚</span>
                                    <span className="text-sm font-medium text-blue-800">In Progress</span>
                                </div>
                                <p className="text-xs text-blue-700">
                                    {inProgressOrders} order{inProgressOrders !== 1 ? 's' : ''} in delivery
                                </p>
                            </div>
                        )}

                        {/* System Status */}
                        <div className="bg-green-50 border border-green-200 rounded-lg p-4">
                            <div className="flex items-center mb-2">
                                <span className="w-2 h-2 bg-green-500 rounded-full mr-2"></span>
                                <span className="text-sm font-medium text-green-800">Driver Status</span>
                            </div>
                            <p className="text-xs text-green-700">Active and ready</p>
                            <div className="mt-2 text-xs text-green-600">
                                Last updated: {new Date().toLocaleTimeString()}
                            </div>
                        </div>
                    </div>

                    {/* Quick Actions */}
                    <div className="mt-6 px-4">
                        <h3 className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-3">
                            Quick Actions
                        </h3>
                        <div className="space-y-2">
                            <button
                                className="w-full text-left px-3 py-2 text-sm text-gray-600 hover:bg-gray-50 rounded">
                                📍 Update Location
                            </button>
                            <button
                                className="w-full text-left px-3 py-2 text-sm text-gray-600 hover:bg-gray-50 rounded">
                                📞 Contact Support
                            </button>
                            <button
                                className="w-full text-left px-3 py-2 text-sm text-gray-600 hover:bg-gray-50 rounded">
                                📊 View Stats
                            </button>
                        </div>
                    </div>
                </aside>

                {/* Main Content */}
                <main className="flex-1">
                    <Outlet/>
                </main>
            </div>
        </div>
    );
};

export default DriverDashboard;