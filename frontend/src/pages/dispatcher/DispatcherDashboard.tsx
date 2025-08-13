import React from "react";
import {Outlet, Link, useLocation} from "react-router-dom";
import {useAuthStore} from "../../store/authStore";
import Button from "../../components/ui/Button";

const DispatcherDashboard: React.FC = () => {
    const location = useLocation();
    const {user, logout} = useAuthStore();

    const navigationItems = [
        {path: "/dispatcher", label: "Orders Management", icon: "📋"},
        {path: "/dispatcher/ratings", label: "Ratings & Reviews", icon: "⭐"},
    ];

    const isActiveRoute = (path: string) => {
        if (path === "/dispatcher") {
            return location.pathname === "/dispatcher" || location.pathname === "/dispatcher/";
        }
        return location.pathname === path;
    };

    return (
        <div className="min-h-screen bg-gray-50">
            {/* Header */}
            <header className="bg-white shadow-sm border-b">
                <div className="flex items-center justify-between px-6 py-4">
                    <div className="flex items-center space-x-4">
                        <h1 className="text-2xl font-bold text-gray-900">Dispatcher Dashboard</h1>
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

                    {/* System Status */}
                    <div className="mt-6 px-4">
                        <div className="bg-green-50 border border-green-200 rounded-lg p-4">
                            <div className="flex items-center mb-2">
                                <span className="w-2 h-2 bg-green-500 rounded-full mr-2"></span>
                                <span className="text-sm font-medium text-green-800">System Status</span>
                            </div>
                            <p className="text-xs text-green-700">All systems operational</p>
                            <div className="mt-2 text-xs text-green-600">
                                Last updated: {new Date().toLocaleTimeString()}
                            </div>
                        </div>
                    </div>
                </aside>

                {/* Main Content */}
                <main className="flex-1 p-6">
                    <Outlet/>
                </main>
            </div>
        </div>
    );
};

export default DispatcherDashboard;