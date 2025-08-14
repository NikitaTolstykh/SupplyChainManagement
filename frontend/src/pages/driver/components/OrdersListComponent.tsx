import React from 'react';
import OrderActionCard from "./OrderActionCardComponent.tsx";
import type {DriverOrderListItemDto} from "../../../lib/types/DriverOrderListItemDto.ts";

interface OrdersListProps {
    orders: DriverOrderListItemDto[];
    onViewDetails: (orderId: number) => void;
    onAccept?: (orderId: number) => void;
    onComplete?: (orderId: number) => void;
    isAcceptLoading?: boolean;
    isCompleteLoading?: boolean;
    emptyMessage?: string;
    emptyIcon?: string;
    title?: string;
    subtitle?: string;
}

const OrdersList: React.FC<OrdersListProps> = ({
                                                   orders,
                                                   onViewDetails,
                                                   onAccept,
                                                   onComplete,
                                                   isAcceptLoading = false,
                                                   isCompleteLoading = false,
                                                   emptyMessage = "No orders available",
                                                   emptyIcon = "📦",
                                                   title,
                                                   subtitle
                                               }) => {
    return (
        <div className="space-y-6">
            {/* Header */}
            {(title || subtitle) && (
                <div className="text-center">
                    {title && <h2 className="text-3xl font-bold text-gray-900 mb-2">{title}</h2>}
                    {subtitle && <p className="text-gray-600 text-lg">{subtitle}</p>}
                </div>
            )}

            {/* Orders Count */}
            <div className="bg-white rounded-lg shadow-sm border p-4">
                <div className="flex items-center justify-between">
                    <span className="text-gray-700 font-medium">Total Orders:</span>
                    <span className="bg-blue-100 text-blue-800 px-3 py-1 rounded-full text-sm font-medium">
                        {orders.length}
                    </span>
                </div>
            </div>

            {/* Orders Grid */}
            {orders.length === 0 ? (
                <div className="text-center py-16">
                    <div className="text-6xl mb-4">{emptyIcon}</div>
                    <h3 className="text-xl font-medium text-gray-900 mb-2">{emptyMessage}</h3>
                    <p className="text-gray-600">
                        {onAccept
                            ? "New assigned orders will appear here when dispatchers assign them to you."
                            : "Orders you've completed will be shown here."
                        }
                    </p>
                </div>
            ) : (
                <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-6">
                    {orders.map((order) => (
                        <OrderActionCard
                            key={order.id}
                            order={order}
                            onViewDetails={onViewDetails}
                            onAccept={onAccept}
                            onComplete={onComplete}
                            isAcceptLoading={isAcceptLoading}
                            isCompleteLoading={isCompleteLoading}
                        />
                    ))}
                </div>
            )}
        </div>
    );
};

export default OrdersList;