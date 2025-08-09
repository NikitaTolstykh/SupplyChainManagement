import React from "react";
import { useParams, Link } from "react-router-dom";
import { useOrderDetails, useOrderStatusHistory } from "../../hooks/client/useOrders.ts";

const OrderDetailsPage: React.FC = () => {
    const { orderId } = useParams<{ orderId: string }>();
    const id = orderId ? parseInt(orderId) : 0;

    const {
        data: order,
        isLoading: orderLoading,
        isError: orderError,
    } = useOrderDetails(id);

    const {
        data: statusHistory,
        isLoading: historyLoading,
        isError: historyError,
    } = useOrderStatusHistory(id);

    if (orderLoading || historyLoading) {
        return (
            <div className="flex items-center justify-center h-64">
                <div className="text-gray-600">Loading order details...</div>
            </div>
        );
    }

    if (orderError) {
        return (
            <div className="bg-red-50 border border-red-200 rounded-md p-4">
                <div className="text-red-800">Failed to load order details.</div>
            </div>
        );
    }

    if (historyError) {
        return (
            <div className="bg-red-50 border border-red-200 rounded-md p-4">
                <div className="text-red-800">Failed to load order status history.</div>
            </div>
        );
    }

    if (!order) {
        return (
            <div className="text-center py-8">
                <div className="text-gray-600">Order not found.</div>
                <Link to="/client/orders" className="text-blue-600 hover:text-blue-800 mt-2 inline-block">
                    ← Back to Orders
                </Link>
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

    const getDistanceCategoryLabel = (category: string): string => {
        switch (category) {
            case "SHORT": return "Short Distance";
            case "MEDIUM": return "Medium Distance";
            case "LONG": return "Long Distance";
            default: return category;
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

    const canRate = order.status === 'DELIVERED';

    return (
        <div className="max-w-4xl mx-auto">
            <div className="flex items-center justify-between mb-6">
                <h1 className="text-2xl font-semibold">Order #{order.id} Details</h1>
                <div className="flex items-center space-x-3">
                    {canRate && (
                        <Link
                            to={`/client/rating/${order.id}`}
                            className="bg-yellow-600 hover:bg-yellow-700 text-white px-4 py-2 rounded-md transition-colors"
                        >
                            Rate Order
                        </Link>
                    )}
                    <Link to="/client/orders" className="text-blue-600 hover:text-blue-800">
                        ← Back to Orders
                    </Link>
                </div>
            </div>

            {/* Order Information */}
            <div className="bg-white rounded-lg shadow p-6 mb-6">
                <h2 className="text-lg font-semibold mb-4">Order Information</h2>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="space-y-4">
                        <div>
                            <span className="block text-sm font-medium text-gray-700">From Address:</span>
                            <span className="text-gray-900">{order.fromAddress}</span>
                        </div>

                        <div>
                            <span className="block text-sm font-medium text-gray-700">To Address:</span>
                            <span className="text-gray-900">{order.toAddress}</span>
                        </div>

                        <div>
                            <span className="block text-sm font-medium text-gray-700">Cargo Type:</span>
                            <span className="text-gray-900">{order.cargoType}</span>
                        </div>

                        {order.cargoDescription && (
                            <div>
                                <span className="block text-sm font-medium text-gray-700">Description:</span>
                                <span className="text-gray-900">{order.cargoDescription}</span>
                            </div>
                        )}
                    </div>

                    <div className="space-y-4">
                        <div>
                            <span className="block text-sm font-medium text-gray-700">Weight:</span>
                            <span className="text-gray-900">{order.weightKg} kg</span>
                        </div>

                        <div>
                            <span className="block text-sm font-medium text-gray-700">Distance Category:</span>
                            <span className="text-gray-900">{getDistanceCategoryLabel(order.distanceCategory)}</span>
                        </div>

                        <div>
                            <span className="block text-sm font-medium text-gray-700">Payment Method:</span>
                            <span className="text-gray-900">{getPaymentMethodLabel(order.paymentMethod)}</span>
                        </div>

                        <div>
                            <span className="block text-sm font-medium text-gray-700">Pickup Time:</span>
                            <span className="text-gray-900">
                                {new Date(order.pickupTime).toLocaleString()}
                            </span>
                        </div>

                        <div>
                            <span className="block text-sm font-medium text-gray-700">Status:</span>
                            <span className={`inline-block px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(order.status)}`}>
                                {order.status}
                            </span>
                        </div>

                        <div>
                            <span className="block text-sm font-medium text-gray-700">Price:</span>
                            <span className="text-gray-900 text-lg font-semibold">${order.price}</span>
                        </div>
                    </div>
                </div>

                {order.comment && (
                    <div className="mt-4 pt-4 border-t">
                        <span className="block text-sm font-medium text-gray-700">Comment:</span>
                        <span className="text-gray-900">{order.comment}</span>
                    </div>
                )}

                <div className="mt-4 pt-4 border-t text-sm text-gray-500">
                    <div>Created: {new Date(order.createdAt).toLocaleString()}</div>
                    <div>Last Updated: {new Date(order.updatedAt).toLocaleString()}</div>
                </div>
            </div>

            {/* Status History */}
            <div className="bg-white rounded-lg shadow p-6">
                <h2 className="text-lg font-semibold mb-4">Status History</h2>

                {statusHistory?.length === 0 ? (
                    <p className="text-gray-600">No status updates available.</p>
                ) : (
                    <div className="space-y-3">
                        {statusHistory?.map((status) => (
                            <div key={status.id} className="border-l-4 border-blue-200 pl-4 py-2">
                                <div className="flex items-center justify-between">
                                    <span className="font-medium text-gray-900">
                                        {status.fromStatus} → {status.toStatus}
                                    </span>
                                    <span className="text-sm text-gray-500">
                                        {new Date(status.changedAt).toLocaleString()}
                                    </span>
                                </div>
                                <div className="text-sm text-gray-600">
                                    Changed by: {status.changedBy}
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default OrderDetailsPage;