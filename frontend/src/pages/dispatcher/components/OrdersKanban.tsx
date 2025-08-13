import React from 'react';
import type { OrderListItemDto } from '../../../lib/types/ClientDtos';
import type { OrderStatus } from '../../../lib/types/OrderStatus';

interface OrdersKanbanProps {
    orders: OrderListItemDto[];
    onSelectOrder: (orderId: number) => void;
    onAssignDriver: (orderId: number) => void;
}

const OrdersKanban: React.FC<OrdersKanbanProps> = ({
                                                       orders,
                                                       onSelectOrder,
                                                       onAssignDriver
                                                   }) => {
    const columns: { status: OrderStatus; title: string; color: string }[] = [
        { status: 'CREATED', title: 'New Orders', color: 'bg-gray-100' },
        { status: 'ACCEPTED', title: 'Accepted', color: 'bg-yellow-100' },
        { status: 'ASSIGNED', title: 'Assigned', color: 'bg-blue-100' },
        { status: 'IN_PROGRESS', title: 'In Progress', color: 'bg-purple-100' },
        { status: 'DELIVERED', title: 'Delivered', color: 'bg-green-100' },
        { status: 'CANCELLED', title: 'Cancelled', color: 'bg-red-100' },
    ];

    const getOrdersByStatus = (status: OrderStatus) =>
        orders.filter(order => order.status === status);

    const getStatusColor = (status: OrderStatus) => {
        switch (status) {
            case 'DELIVERED': return 'bg-green-100 text-green-800 border-green-200';
            case 'CANCELLED': return 'bg-red-100 text-red-800 border-red-200';
            case 'IN_PROGRESS': return 'bg-blue-100 text-blue-800 border-blue-200';
            case 'ASSIGNED': return 'bg-purple-100 text-purple-800 border-purple-200';
            case 'ACCEPTED': return 'bg-yellow-100 text-yellow-800 border-yellow-200';
            case 'CREATED': return 'bg-gray-100 text-gray-800 border-gray-200';
            default: return 'bg-gray-100 text-gray-800 border-gray-200';
        }
    };

    const getPaymentMethodLabel = (method: string): string => {
        switch (method) {
            case "CASH": return "💰";
            case "CARD": return "💳";
            case "BANK_TRANSACTION": return "🏦";
            case "POSTAL_PAYMENT": return "📮";
            default: return "💰";
        }
    };

    return (
        <div className="grid grid-cols-1 lg:grid-cols-3 xl:grid-cols-6 gap-6 h-full">
            {columns.map((column) => {
                const columnOrders = getOrdersByStatus(column.status);

                return (
                    <div key={column.status} className="flex flex-col">
                        {/* Column Header */}
                        <div className={`${column.color} p-4 rounded-t-xl border-b`}>
                            <div className="flex items-center justify-between">
                                <h3 className="font-semibold text-gray-700">{column.title}</h3>
                                <span className="bg-white px-2 py-1 rounded-full text-xs font-medium text-gray-600">
                                    {columnOrders.length}
                                </span>
                            </div>
                        </div>

                        {/* Column Content */}
                        <div className="flex-1 bg-gray-50 p-4 rounded-b-xl min-h-96 overflow-y-auto">
                            {columnOrders.length === 0 ? (
                                <div className="text-center text-gray-500 py-8">
                                    <div className="text-4xl mb-2">📦</div>
                                    <p className="text-sm">No orders</p>
                                </div>
                            ) : (
                                <div className="space-y-3">
                                    {columnOrders.map((order) => (
                                        <div
                                            key={order.id}
                                            className="bg-white rounded-lg shadow-sm border border-gray-200 p-4 cursor-pointer hover:shadow-md transition-shadow"
                                            onClick={() => onSelectOrder(order.id)}
                                        >
                                            {/* Order Header */}
                                            <div className="flex items-center justify-between mb-3">
                                                <span className="font-semibold text-gray-900">
                                                    #{order.id}
                                                </span>
                                                <span className={`inline-flex items-center px-2 py-1 rounded-full text-xs font-medium border ${getStatusColor(order.status)}`}>
                                                    {order.status}
                                                </span>
                                            </div>

                                            {/* Route Info */}
                                            <div className="space-y-2 mb-3">
                                                <div className="flex items-start">
                                                    <span className="text-green-600 mr-2">📍</span>
                                                    <span className="text-sm text-gray-700 line-clamp-2">
                                                        {order.fromAddress}
                                                    </span>
                                                </div>
                                                <div className="flex items-start">
                                                    <span className="text-red-600 mr-2">📍</span>
                                                    <span className="text-sm text-gray-700 line-clamp-2">
                                                        {order.toAddress}
                                                    </span>
                                                </div>
                                            </div>

                                            {/* Order Details */}
                                            <div className="space-y-1 mb-3 text-sm text-gray-600">
                                                <div className="flex items-center justify-between">
                                                    <span>Cargo:</span>
                                                    <span className="font-medium">{order.cargoType}</span>
                                                </div>
                                                <div className="flex items-center justify-between">
                                                    <span>Weight:</span>
                                                    <span className="font-medium">{order.weightKg} kg</span>
                                                </div>
                                                <div className="flex items-center justify-between">
                                                    <span>Payment:</span>
                                                    <span className="font-medium">
                                                        {getPaymentMethodLabel(order.paymentMethod)} ${order.price}
                                                    </span>
                                                </div>
                                            </div>

                                            {/* Timestamps */}
                                            <div className="text-xs text-gray-500 space-y-1">
                                                <div>Created: {new Date(order.createdAt).toLocaleDateString()}</div>
                                                {order.pickupTime && (
                                                    <div>Pickup: {new Date(order.pickupTime).toLocaleDateString()}</div>
                                                )}
                                            </div>

                                            {/* Action for ACCEPTED orders */}
                                            {order.status === 'ACCEPTED' && (
                                                <div className="mt-3 pt-3 border-t border-gray-200">
                                                    <button
                                                        onClick={(e) => {
                                                            e.stopPropagation();
                                                            onAssignDriver(order.id);
                                                        }}
                                                        className="w-full bg-emerald-600 hover:bg-emerald-700 text-white px-3 py-1 rounded-md text-sm font-medium transition-colors"
                                                    >
                                                        Assign Driver
                                                    </button>
                                                </div>
                                            )}

                                            {/* Urgent Indicator */}
                                            {order.pickupTime && new Date(order.pickupTime) <= new Date(Date.now() + 24 * 60 * 60 * 1000) && (
                                                <div className="absolute -top-2 -right-2 bg-red-500 text-white text-xs px-2 py-1 rounded-full">
                                                    Urgent
                                                </div>
                                            )}
                                        </div>
                                    ))}
                                </div>
                            )}

                            {/* Drop Zone for ACCEPTED status */}
                            {column.status === 'ACCEPTED' && columnOrders.length === 0 && (
                                <div className="bg-blue-50 border-2 border-dashed border-blue-300 rounded-lg p-4 text-center">
                                    <div className="text-blue-600 mb-2">👥</div>
                                    <p className="text-sm text-blue-600">
                                        Orders ready for driver assignment will appear here
                                    </p>
                                </div>
                            )}
                        </div>
                    </div>
                );
            })}
        </div>
    );
};

export default OrdersKanban;