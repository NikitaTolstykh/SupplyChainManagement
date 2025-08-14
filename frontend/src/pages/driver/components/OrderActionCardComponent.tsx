import React from "react";
import type {DriverOrderListItemDto} from "../../../lib/types/DriverOrderListItemDto.ts";
import type {OrderStatus} from "../../../lib/types/OrderStatus.ts";

interface OrderActionCardProps {
    order: DriverOrderListItemDto;
    onViewDetails: (orderId: number) => void;
    onAccept?: (orderId: number) => void;
    onComplete?: (orderId: number) => void;
    isAcceptLoading?: boolean;
    isCompleteLoading?: boolean;
}

const OrderActionCard: React.FC<OrderActionCardProps> = ({
                                                             order,
                                                             onViewDetails,
                                                             onAccept,
                                                             onComplete,
                                                             isAcceptLoading = false,
                                                             isCompleteLoading = false
                                                         }) => {
    const getStatusColor = (status: OrderStatus) => {
        switch (status) {
            case 'ASSIGNED':
                return 'bg-yellow-100 text-yellow-800';
            case 'IN_PROGRESS':
                return 'bg-blue-100 text-blue-800';
            case 'DELIVERED':
                return 'bg-green-100 text-green-800';
            default:
                return 'bg-gray-100 text-gray-800';
        }
    };
    const getStatusIcon = (status: OrderStatus) => {
        switch (status) {
            case 'ASSIGNED':
                return '📋';
            case 'IN_PROGRESS':
                return '🚚';
            case 'DELIVERED':
                return '✅';
            default:
                return '📦';
        }
    };

    const isUrgent = () => {
        const pickupTime = new Date(order.pickupTime);
        const now = new Date();
        const hoursDiff = (pickupTime.getTime() - now.getTime()) / (1000 * 60 * 60);
        return hoursDiff <= 2 && hoursDiff >= 0;
    };

    return (
        <div
            className="bg-white rounded-2xl shadow-lg border border-gray-200 p-8 hover:shadow-xl transition-all relative">
            {/* Urgent indicator */}
            {isUrgent() && (
                <div
                    className="absolute -top-2 -right-2 bg-red-100 text-red-800 px-3 py-1 rounded-full text-sm font-medium">
                    🚨 Urgent
                </div>
            )}

            {/* Order status */}
            <div className="flex items-center space-x-2 mb-4">
                <span className="text-2xl">{getStatusIcon(order.status)}</span>
                <span className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(order.status)}`}>
                    {order.status}
                </span>
            </div>

            {/* Order ID and Price */}
            <div className="flex items-center justify-between mb-6">
                <h3 className="text-xl font-bold text-gray-900">Order #{order.id}</h3>
                <span className="text-2xl font-bold text-green-600">${order.price}</span>
            </div>

            {/* Route Information */}
            <div className="space-y-4 mb-6">
                <div className="bg-gray-50 rounded-lg p-4 space-y-2">
                    <div className="flex items-start space-x-3">
                        <span className="text-green-600 text-lg">📍</span>
                        <div>
                            <p className="text-sm font-medium text-gray-700">From</p>
                            <p className="text-gray-900">{order.fromAddress}</p>
                        </div>
                    </div>
                    <div className="border-l-2 border-gray-300 ml-2 h-4"></div>
                    <div className="flex items-start space-x-3">
                        <span className="text-red-600 text-lg">📍</span>
                        <div>
                            <p className="text-sm font-medium text-gray-700">To</p>
                            <p className="text-gray-900">{order.toAddress}</p>
                        </div>
                    </div>
                </div>
            </div>

            {/* Pickup Time */}
            <div className="bg-gray-50 rounded-lg p-4 mb-6">
                <div className="flex items-center space-x-2">
                    <span className="text-lg">🕒</span>
                    <div>
                        <p className="text-sm font-medium text-gray-700">Pickup Time</p>
                        <p className="text-lg font-semibold text-gray-900">
                            {new Date(order.pickupTime).toLocaleString()}
                        </p>
                    </div>
                </div>
            </div>

            {/* Actions */}
            <div className="space-y-4">
                {/* Accept Button - only for ASSIGNED orders */}
                {order.status === 'ASSIGNED' && onAccept && (
                    <button
                        onClick={() => onAccept(order.id)}
                        disabled={isAcceptLoading}
                        className="w-full bg-emerald-600 hover:bg-emerald-700 disabled:bg-emerald-400 text-white font-semibold py-4 px-8 rounded-xl text-lg transition-colors"
                    >
                        {isAcceptLoading ? (
                            <span className="flex items-center justify-center">
                                <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
                                     xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor"
                                            strokeWidth="4"></circle>
                                    <path className="opacity-75" fill="currentColor"
                                          d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                </svg>
                                Accepting...
                            </span>
                        ) : (
                            <span className="flex items-center justify-center">
                                <span className="mr-2">✓</span>
                                Accept Order
                            </span>
                        )}
                    </button>
                )}

                {/* Complete Button - only for IN_PROGRESS orders */}
                {order.status === 'IN_PROGRESS' && onComplete && (
                    <button
                        onClick={() => onComplete(order.id)}
                        disabled={isCompleteLoading}
                        className="w-full bg-blue-600 hover:bg-blue-700 disabled:bg-blue-400 text-white font-semibold py-4 px-8 rounded-xl text-lg transition-colors"
                    >
                        {isCompleteLoading ? (
                            <span className="flex items-center justify-center">
                                <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
                                     xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor"
                                            strokeWidth="4"></circle>
                                    <path className="opacity-75" fill="currentColor"
                                          d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                </svg>
                                Completing...
                            </span>
                        ) : (
                            <span className="flex items-center justify-center">
                                <span className="mr-2">🏁</span>
                                Complete Order
                            </span>
                        )}
                    </button>
                )}

                {/* View Details Button */}
                <button
                    onClick={() => onViewDetails(order.id)}
                    className="w-full bg-gray-100 hover:bg-gray-200 text-gray-700 font-medium py-3 px-6 rounded-lg transition-colors"
                >
                    View Details
                </button>
            </div>
        </div>
    );
};

export default OrderActionCard;