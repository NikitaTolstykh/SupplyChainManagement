import React, {useState} from 'react';
import OrdersList from "./components/OrdersListComponent.tsx";
import OrderDetails from "./components/OrderDetailsComponent.tsx";
import {useCompletedOrders} from "../../hooks/driver/useDriverOrdersHook.ts";

const CompletedOrdersPage: React.FC = () => {
    const [selectedOrderId, setSelectedOrderId] = useState<number | null>(null);

    const {data: orders = [], isLoading, isError, refetch} = useCompletedOrders();

    const handleViewDetails = (orderId: number) => {
        setSelectedOrderId(orderId);
    };

    if (isLoading) {
        return (
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <div className="flex items-center justify-center h-64">
                    <div className="text-center">
                        <div
                            className="animate-spin rounded-full h-12 w-12 border-b-2 border-green-600 mx-auto mb-4"></div>
                        <p className="text-gray-600">Loading your completed orders...</p>
                    </div>
                </div>
            </div>
        );
    }

    if (isError) {
        return (
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <div className="bg-red-50 border border-red-200 rounded-md p-6">
                    <div className="text-center">
                        <div className="text-red-600 text-4xl mb-4">⚠️</div>
                        <h3 className="text-lg font-medium text-red-800 mb-2">Failed to load orders</h3>
                        <p className="text-red-700 mb-4">There was an error loading your completed orders.</p>
                        <button
                            onClick={() => refetch()}
                            className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-lg"
                        >
                            Try Again
                        </button>
                    </div>
                </div>
            </div>
        );
    }

    // Calculate statistics
    const totalEarnings = orders.reduce((sum, order) => sum + order.price, 0);
    const thisMonthOrders = orders.filter(order => {
        const orderDate = new Date(order.pickupTime);
        const now = new Date();
        return orderDate.getMonth() === now.getMonth() && orderDate.getFullYear() === now.getFullYear();
    });
    const thisMonthEarnings = thisMonthOrders.reduce((sum, order) => sum + order.price, 0);

    return (
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            {/* Statistics Cards */}
            {orders.length > 0 && (
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
                    <div className="bg-white rounded-2xl shadow-lg border border-gray-200 p-6">
                        <div className="flex items-center">
                            <div className="flex-1">
                                <p className="text-sm font-medium text-gray-600">Total Completed</p>
                                <p className="text-3xl font-bold text-gray-900">{orders.length}</p>
                            </div>
                            <div className="p-3 bg-green-100 rounded-full">
                                <span className="text-green-600 text-2xl">✅</span>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white rounded-2xl shadow-lg border border-gray-200 p-6">
                        <div className="flex items-center">
                            <div className="flex-1">
                                <p className="text-sm font-medium text-gray-600">Total Earnings</p>
                                <p className="text-3xl font-bold text-green-600">${totalEarnings.toFixed(2)}</p>
                            </div>
                            <div className="p-3 bg-green-100 rounded-full">
                                <span className="text-green-600 text-2xl">💰</span>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white rounded-2xl shadow-lg border border-gray-200 p-6">
                        <div className="flex items-center">
                            <div className="flex-1">
                                <p className="text-sm font-medium text-gray-600">This Month</p>
                                <p className="text-2xl font-bold text-blue-600">{thisMonthOrders.length}</p>
                                <p className="text-sm text-gray-500">${thisMonthEarnings.toFixed(2)}</p>
                            </div>
                            <div className="p-3 bg-blue-100 rounded-full">
                                <span className="text-blue-600 text-2xl">📊</span>
                            </div>
                        </div>
                    </div>
                </div>
            )}

            <OrdersList
                orders={orders}
                onViewDetails={handleViewDetails}
                title="Completed Orders"
                subtitle="Your delivery history and earnings"
                emptyMessage="No completed orders yet"
                emptyIcon="📜"
            />

            {/* Order Details Modal */}
            {selectedOrderId && (
                <OrderDetails
                    isOpen={!!selectedOrderId}
                    onClose={() => setSelectedOrderId(null)}
                    orderId={selectedOrderId}
                />
            )}
        </div>
    );
};

export default CompletedOrdersPage;