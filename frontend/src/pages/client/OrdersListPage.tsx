import React from "react";
import { Link } from "react-router-dom";
import { useOrders } from "../../hooks/client/useOrders.ts";

const OrdersListPage: React.FC = () => {
    const { data: orders, isLoading, isError } = useOrders();

    if (isLoading) {
        return (
            <div className="flex items-center justify-center h-64">
                <div className="text-gray-600">Loading orders...</div>
            </div>
        );
    }

    if (isError) {
        return (
            <div className="bg-red-50 border border-red-200 rounded-md p-4">
                <div className="text-red-800">Failed to load orders.</div>
            </div>
        );
    }

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

    const getPaymentMethodLabel = (method: string): string => {
        switch (method) {
            case "CASH": return "Cash";
            case "CARD": return "Card";
            case "BANK_TRANSACTION": return "Bank Transfer";
            case "POSTAL_PAYMENT": return "Postal Payment";
            default: return method;
        }
    };

    return (
        <div>
            <div className="flex items-center justify-between mb-6">
                <h1 className="text-2xl font-semibold">Your Orders</h1>
                <Link
                    to="/client/create-order"
                    className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md transition-colors"
                >
                    Create New Order
                </Link>
            </div>

            {orders?.length === 0 ? (
                <div className="bg-white rounded-lg shadow p-8 text-center">
                    <div className="text-gray-600 mb-4">No orders found.</div>
                    <Link
                        to="/client/create-order"
                        className="text-blue-600 hover:text-blue-800"
                    >
                        Create your first order
                    </Link>
                </div>
            ) : (
                <div className="bg-white rounded-lg shadow overflow-hidden">
                    <div className="divide-y divide-gray-200">
                        {orders?.map((order) => (
                            <div key={order.id} className="p-6 hover:bg-gray-50 transition-colors">
                                <Link to={`/client/orders/${order.id}`} className="block">
                                    <div className="flex items-center justify-between">
                                        <div className="flex-1">
                                            <div className="flex items-center justify-between mb-2">
                                                <span className="text-lg font-medium text-gray-900">
                                                    Order #{order.id}
                                                </span>
                                                <span className={`inline-block px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(order.status)}`}>
                                                    {order.status}
                                                </span>
                                            </div>

                                            <div className="text-gray-700 mb-2">
                                                <span className="font-medium">Route:</span> {order.fromAddress} → {order.toAddress}
                                            </div>

                                            <div className="flex items-center space-x-4 text-sm text-gray-600">
                                                <span>Price: ${order.price}</span>
                                                <span>Payment: {getPaymentMethodLabel(order.paymentMethod)}</span>
                                            </div>

                                            <div className="text-xs text-gray-500 mt-2">
                                                Created: {new Date(order.createdAt).toLocaleString()}
                                                {order.pickupTime && (
                                                    <span className="ml-4">
                                                        Pickup: {new Date(order.pickupTime).toLocaleString()}
                                                    </span>
                                                )}
                                            </div>
                                        </div>

                                        <div className="ml-4">
                                            <svg className="w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                                            </svg>
                                        </div>
                                    </div>
                                </Link>
                            </div>
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
};

export default OrdersListPage;