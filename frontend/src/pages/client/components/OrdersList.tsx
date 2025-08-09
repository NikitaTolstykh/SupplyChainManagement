import React from "react";
import type {OrderListItemDto} from "../../../lib/types/ClientDtos.ts";

interface OrderListProps {
    orders: OrderListItemDto[];
    onSelectOrder: (id: number) => void;
}

const OrdersList: React.FC<OrderListProps> = ({orders, onSelectOrder}) => {
    return (
        <div className="space-y-4">
            {orders.map(order => (
                <div key={order.id}
                     className="bg-white rounded-xl shadow-sm border-l-4 border-blue-500 p-6 hover:shadow-md cursor-pointer transition-shadow"
                     onClick={() => onSelectOrder(order.id)}
                >
                    <h3 className="text-lg font-semibold mb-1">Order #{order.id}</h3>
                    <p className="text-gray-700">Status: {order.status}</p>
                    <p className="text-gray-500 text-sm">{new Date(order.createdAt).toLocaleDateString()}</p>
                </div>
            ))}
        </div>
    );
};

export default OrdersList;