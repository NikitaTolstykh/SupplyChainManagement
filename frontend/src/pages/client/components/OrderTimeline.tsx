import React from 'react';
import type { OrderStatusHistoryDto } from "../../../lib/types/ClientDtos.ts";

interface OrderTimelineProps {
    statusHistory: OrderStatusHistoryDto[];
}

const OrderTimeline: React.FC<OrderTimelineProps> = ({ statusHistory }) => {
    return (
        <div className="flex items-center space-x-4 relative">
            {statusHistory.map((historyItem, index) => (
                <div key={historyItem.id} className="flex flex-col items-center">
                    <div
                        className="w-8 h-8 rounded-full bg-blue-600 text-white flex items-center justify-center text-sm font-semibold">
                        {index + 1}
                    </div>
                    <p className="mt-2 text-center text-sm">
                        {historyItem.fromStatus} → {historyItem.toStatus}
                    </p>
                    <p className="text-xs text-gray-500">
                        {new Date(historyItem.changedAt).toLocaleDateString()}
                    </p>
                    <p className="text-xs text-gray-400">
                        by {historyItem.changedBy}
                    </p>
                </div>
            ))}
        </div>
    );
};

export default OrderTimeline;