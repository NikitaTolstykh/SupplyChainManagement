import React from "react";
import { Link } from "react-router-dom";
import { useClientStats } from "../../hooks/client/useClientStats.ts";
import { useOrders } from "../../hooks/client/useOrders.ts";

const DashboardOverview: React.FC = () => {
    const { data: stats, isLoading: statsLoading } = useClientStats();
    const { data: orders, isLoading: ordersLoading } = useOrders();

    const recentOrders = orders?.slice(0, 5) || [];

    const getStatusColor = (status: string) => {
        switch (status) {
            case 'DELIVERED': return 'bg-green-100 text-green-800';
            case 'CANCELLED': return 'bg-red-100 text-red-800';
            case 'IN_PROGRESS': return 'bg-blue-100 text-blue-800';
            case 'ASSIGNED': return 'bg-purple-100 text-purple-800';
            case 'ACCEPTED': return 'bg-yellow-100 text-yellow-800';
            case 'CREATED': return 'bg-gray-100 text-gray-800';
            default: return 'bg-gray-100 text-gray-800';
        }
    };

    return (
        <div className="space-y-6">
            <div className="flex items-center justify-between">
                <h1 className="text-2xl font-semibold text-gray-900">Dashboard Overview</h1>
                <Link
                    to="/client/create-order"
                    className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md transition-colors"
                >
                    Create New Order
                </Link>
            </div>

            {/* Statistics Cards */}
            {statsLoading ? (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                    {[...Array(4)].map((_, i) => (
                        <div key={i} className="bg-white rounded-lg shadow p-6">
                            <div className="animate-pulse">
                                <div className="h-4 bg-gray-200 rounded w-3/4 mb-2"></div>
                                <div className="h-8 bg-gray-200 rounded w-1/2"></div>
                            </div>
                        </div>
                    ))}
                </div>
            ) : stats ? (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                    <div className="bg-white rounded-lg shadow p-6">
                        <div className="flex items-center">
                            <div className="flex-1">
                                <p className="text-sm font-medium text-gray-600">Total Orders</p>
                                <p className="text-3xl font-bold text-gray-900">{stats.totalOrders}</p>
                            </div>
                            <div className="p-3 bg-blue-100 rounded-full">
                                <span className="text-blue-600 text-xl">📦</span>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white rounded-lg shadow p-6">
                        <div className="flex items-center">
                            <div className="flex-1">
                                <p className="text-sm font-medium text-gray-600">Completed</p>
                                <p className="text-3xl font-bold text-green-600">{stats.completedOrders}</p>
                            </div>
                            <div className="p-3 bg-green-100 rounded-full">
                                <span className="text-green-600 text-xl">✅</span>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white rounded-lg shadow p-6">
                        <div className="flex items-center">
                            <div className="flex-1">
                                <p className="text-sm font-medium text-gray-600">Total Spent</p>
                                <p className="text-3xl font-bold text-purple-600">${stats.totalSpent}</p>
                            </div>
                            <div className="p-3 bg-purple-100 rounded-full">
                                <span className="text-purple-600 text-xl">💰</span>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white rounded-lg shadow p-6">
                        <div className="flex items-center">
                            <div className="flex-1">
                                <p className="text-sm font-medium text-gray-600">Avg Rating</p>
                                <p className="text-3xl font-bold text-yellow-500">
                                    {stats.averageRating > 0 ? stats.averageRating.toFixed(1) : "N/A"}
                                </p>
                            </div>
                            <div className="p-3 bg-yellow-100 rounded-full">
                                <span className="text-yellow-600 text-xl">⭐</span>
                            </div>
                        </div>
                    </div>
                </div>
            ) : (
                <div className="bg-white rounded-lg shadow p-6">
                    <p className="text-gray-600">Failed to load statistics</p>
                </div>
            )}

            {/* Recent Orders */}
            <div className="bg-white rounded-lg shadow">
                <div className="px-6 py-4 border-b border-gray-200">
                    <div className="flex items-center justify-between">
                        <h2 className="text-lg font-semibold text-gray-900">Recent Orders</h2>
                        <Link to="/client/orders" className="text-blue-600 hover:text-blue-800 text-sm">
                            View All Orders →
                        </Link>
                    </div>
                </div>

                <div className="p-6">
                    {ordersLoading ? (
                        <div className="space-y-4">
                            {[...Array(3)].map((_, i) => (
                                <div key={i} className="animate-pulse">
                                    <div className="h-4 bg-gray-200 rounded w-3/4 mb-2"></div>
                                    <div className="h-3 bg-gray-200 rounded w-1/2"></div>
                                </div>
                            ))}
                        </div>
                    ) : recentOrders.length > 0 ? (
                        <div className="space-y-4">
                            {recentOrders.map((order) => (
                                <Link
                                    key={order.id}
                                    to={`/client/orders/${order.id}`}
                                    className="block p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors"
                                >
                                    <div className="flex items-center justify-between">
                                        <div>
                                            <p className="font-medium text-gray-900">Order #{order.id}</p>
                                            <p className="text-sm text-gray-600">
                                                {order.fromAddress} → {order.toAddress}
                                            </p>
                                            <p className="text-xs text-gray-500">
                                                {new Date(order.createdAt).toLocaleDateString()}
                                            </p>
                                        </div>
                                        <div className="text-right">
                                            <span className={`inline-block px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(order.status)}`}>
                                                {order.status}
                                            </span>
                                            <p className="text-sm font-medium text-gray-900 mt-1">
                                                ${order.price}
                                            </p>
                                        </div>
                                    </div>
                                </Link>
                            ))}
                        </div>
                    ) : (
                        <div className="text-center py-8">
                            <div className="text-gray-500 mb-4">No orders yet</div>
                            <Link
                                to="/client/create-order"
                                className="inline-block bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md transition-colors"
                            >
                                Create Your First Order
                            </Link>
                        </div>
                    )}
                </div>
            </div>

            {/* Quick Actions */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="bg-white rounded-lg shadow p-6">
                    <h3 className="text-lg font-semibold text-gray-900 mb-4">Quick Actions</h3>
                    <div className="space-y-3">
                        <Link
                            to="/client/create-order"
                            className="block w-full text-left px-4 py-3 bg-blue-50 hover:bg-blue-100 text-blue-700 rounded-lg transition-colors"
                        >
                            <span className="text-lg mr-2">➕</span>
                            Create New Order
                        </Link>
                        <Link
                            to="/client/orders"
                            className="block w-full text-left px-4 py-3 bg-gray-50 hover:bg-gray-100 text-gray-700 rounded-lg transition-colors"
                        >
                            <span className="text-lg mr-2">📋</span>
                            View All Orders
                        </Link>
                    </div>
                </div>

                {stats?.mostUsedRoute && (
                    <div className="bg-white rounded-lg shadow p-6">
                        <h3 className="text-lg font-semibold text-gray-900 mb-4">Most Used Route</h3>
                        <div className="p-4 bg-gray-50 rounded-lg">
                            <p className="text-gray-700">{stats.mostUsedRoute}</p>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default DashboardOverview;