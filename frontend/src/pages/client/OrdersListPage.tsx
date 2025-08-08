import React from "react";
import {Link} from "react-router-dom";
import { useOrders} from "../../hooks/client/useOrders.ts";

const OrdersListPage: React.FC = () => {
    const { data: orders, isLoading, isError } = useOrders();

    if (isLoading) {
        return <div>Loading orders...</div>;
    }

    if (isError) {
        return <div className="text-red-600">Failed to load orders.</div>;
    }

    return (
        <div>
            <h1 className="text-2xl font-semibold mb-6">Your Orders</h1>
            {orders?.length === 0 ? (
                <p>No orders found.</p>
            ) : (
                <ul className="space-y-4">
                    {orders?.map((order) => (
                        <li
                            key={order.id}
                            className="p-4 bg-white rounded shadow hover:bg-gray-50 transition"
                        >
                            <Link to={`/client/orders/${order.id}`} className="block">
                                <div className="flex justify-between">
                                    <span className="font-medium">Order #{order.id}</span>
                                    <span className="text-sm text-gray-500">{order.status}</span>
                                </div>
                                <div className="text-gray-700 text-sm mt-1">
                                    {order.pickupLocation} → {order.dropoffLocation}
                                </div>
                            </Link>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default OrdersListPage;