import React, {useState} from 'react';
import OrdersKanban from './components/OrdersKanban';
import DriversList from './components/DriversList';
import OrderDetailsModal from './components/OrderDetailsModal';
import AssignDriverModal from './components/AssignDriverModal';
import {useDispatcherOrders} from '../../hooks/dispatcher/useDispatcherOrders';
import type {OrderStatus} from '../../lib/types/OrderStatus';

const OrdersManagementPage: React.FC = () => {
    const [selectedOrderId, setSelectedOrderId] = useState<number | null>(null);
    const [assignDriverOrderId, setAssignDriverOrderId] = useState<number | null>(null);
    const [statusFilter, setStatusFilter] = useState<OrderStatus | 'ALL'>('ALL');
    const [viewMode, setViewMode] = useState<'kanban' | 'list'>('kanban');

    const {data: orders, isLoading, isError, refetch} = useDispatcherOrders();

    const filteredOrders = orders?.filter(order => {
        const matchesStatus = statusFilter === 'ALL' || order.status === statusFilter;
        return matchesStatus;
    }) || [];

    const statusOptions: Array<{ value: OrderStatus | 'ALL', label: string }> = [
        {value: 'ALL', label: 'All Status'},
        {value: 'CREATED', label: 'Created'},
        {value: 'ACCEPTED', label: 'Accepted'},
        {value: 'ASSIGNED', label: 'Assigned'},
        {value: 'IN_PROGRESS', label: 'In Progress'},
        {value: 'DELIVERED', label: 'Delivered'},
        {value: 'CANCELLED', label: 'Cancelled'},
    ];

    const getOrderInfo = (orderId: number) => {
        const order = orders?.find(o => o.id === orderId);
        if (!order) return '';
        return `${order.fromAddress} → ${order.toAddress} (${order.cargoType})`;
    };

    const handleOrderSelect = (orderId: number) => {
        setSelectedOrderId(orderId);
    };

    const handleAssignDriver = (orderId: number) => {
        setAssignDriverOrderId(orderId);
    };

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
                <button
                    onClick={() => refetch()}
                    className="mt-2 text-red-600 hover:text-red-800 underline"
                >
                    Try Again
                </button>
            </div>
        );
    }

    return (
        <div className="space-y-6">
            {/* Header */}
            <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between">
                <div>
                    <h1 className="text-2xl font-semibold text-gray-900">Orders Management</h1>
                    <p className="text-gray-600">Manage and track all delivery orders</p>
                </div>
                <div className="flex bg-gray-100 rounded-lg p-1 mt-4 lg:mt-0">
                    <button
                        onClick={() => setViewMode('kanban')}
                        className={`px-3 py-1 rounded text-sm transition-colors ${
                            viewMode === 'kanban'
                                ? 'bg-white text-gray-900 shadow-sm'
                                : 'text-gray-600 hover:text-gray-900'
                        }`}
                    >
                        Kanban
                    </button>
                    <button
                        onClick={() => setViewMode('list')}
                        className={`px-3 py-1 rounded text-sm transition-colors ${
                            viewMode === 'list'
                                ? 'bg-white text-gray-900 shadow-sm'
                                : 'text-gray-600 hover:text-gray-900'
                        }`}
                    >
                        List
                    </button>
                </div>
            </div>

            {/* Filters */}
            <div className="bg-white rounded-lg shadow-sm border p-4">
                <div className="flex flex-wrap gap-4">
                    <select
                        value={statusFilter}
                        onChange={(e) => setStatusFilter(e.target.value as OrderStatus | 'ALL')}
                        className="border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    >
                        {statusOptions.map(option => (
                            <option key={option.value} value={option.value}>
                                {option.label}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="mt-3 text-sm text-gray-600">
                    Showing {filteredOrders.length} of {orders?.length || 0} orders
                </div>
            </div>

            {/* Content */}
            <div className="grid grid-cols-1 xl:grid-cols-4 gap-6">
                {/* Orders View */}
                <div className="xl:col-span-3">
                    {viewMode === 'kanban' ? (
                        <OrdersKanban
                            orders={filteredOrders}
                            onSelectOrder={handleOrderSelect}
                            onAssignDriver={handleAssignDriver}
                        />
                    ) : (
                        <div className="bg-white rounded-lg shadow-sm border p-6">
                            <div className="space-y-4">
                                {filteredOrders.map((order) => (
                                    <div
                                        key={order.id}
                                        className="border border-gray-200 rounded-lg p-4 hover:bg-gray-50 cursor-pointer transition-colors"
                                        onClick={() => handleOrderSelect(order.id)}
                                    >
                                        <div className="flex items-center justify-between">
                                            <div>
                                                <h3 className="font-semibold text-gray-900">Order #{order.id}</h3>
                                                <p className="text-gray-600">{order.fromAddress} → {order.toAddress}</p>
                                                <p className="text-sm text-gray-500">
                                                    {order.cargoType} • {order.weightKg}kg • ${order.price}
                                                </p>
                                            </div>
                                            <span className={`px-2 py-1 rounded-full text-xs font-medium ${
                                                order.status === 'DELIVERED' ? 'bg-green-100 text-green-800' :
                                                    order.status === 'CANCELLED' ? 'bg-red-100 text-red-800' :
                                                        order.status === 'IN_PROGRESS' ? 'bg-blue-100 text-blue-800' :
                                                            order.status === 'ASSIGNED' ? 'bg-purple-100 text-purple-800' :
                                                                order.status === 'ACCEPTED' ? 'bg-yellow-100 text-yellow-800' :
                                                                    'bg-gray-100 text-gray-800'
                                            }`}>
                                                {order.status}
                                            </span>
                                        </div>
                                    </div>
                                ))}

                                {filteredOrders.length === 0 && (
                                    <div className="text-center py-8">
                                        <div className="text-gray-400 text-4xl mb-2">📋</div>
                                        <p className="text-gray-600">No orders found</p>
                                    </div>
                                )}
                            </div>
                        </div>
                    )}
                </div>

                {/* Drivers Sidebar */}
                <div className="xl:col-span-1">
                    <DriversList/>
                </div>
            </div>

            {/* Modals */}
            <OrderDetailsModal
                isOpen={!!selectedOrderId}
                onClose={() => setSelectedOrderId(null)}
                orderId={selectedOrderId || 0}
                onAssignDriver={handleAssignDriver}
            />

            <AssignDriverModal
                isOpen={!!assignDriverOrderId}
                onClose={() => setAssignDriverOrderId(null)}
                orderId={assignDriverOrderId || 0}
                orderInfo={assignDriverOrderId ? getOrderInfo(assignDriverOrderId) : ''}
            />
        </div>
    );
};

export default OrdersManagementPage;