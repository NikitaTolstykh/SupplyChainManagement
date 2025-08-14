import React, {useState} from 'react';
import {useAssignedOrders} from "../../hooks/driver/useDriverOrdersHook.ts";
import {useAcceptOrder, useCompleteOrder} from "../../hooks/driver/useDriverActionsHook.ts";
import OrdersList from "./components/OrdersListComponent.tsx";
import OrderDetails from "./components/OrderDetailsComponent.tsx";
import Toast from '../../components/ui/Toast';

const AssignedOrdersPage: React.FC = () => {
    const [selectedOrderId, setSelectedOrderId] = useState<number | null>(null);
    const [toast, setToast] = useState<{ message: string; type: 'success' | 'error' } | null>(null);

    const {data: orders = [], isLoading, isError, refetch} = useAssignedOrders();
    const acceptMutation = useAcceptOrder();
    const completeMutation = useCompleteOrder();

    const handleViewDetails = (orderId: number) => {
        setSelectedOrderId(orderId);
    };

    const handleAcceptOrder = async (orderId: number) => {
        try {
            await acceptMutation.mutateAsync(orderId);
            setToast({message: 'Order accepted successfully!', type: 'success'});
            setSelectedOrderId(null);
        } catch (error) {
            setToast({message: 'Failed to accept order. Please try again.', type: 'error'});
        }
    };

    const handleCompleteOrder = async (orderId: number) => {
        try {
            await completeMutation.mutateAsync(orderId);
            setToast({message: 'Order completed successfully!', type: 'success'});
            setSelectedOrderId(null);
        } catch (error) {
            setToast({message: 'Failed to complete order. Please try again.', type: 'error'});
        }
    };

    if (isLoading) {
        return (
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <div className="flex items-center justify-center h-64">
                    <div className="text-center">
                        <div
                            className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
                        <p className="text-gray-600">Loading your assigned orders...</p>
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
                        <p className="text-red-700 mb-4">There was an error loading your assigned orders.</p>
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

    return (
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <OrdersList
                orders={orders}
                onViewDetails={handleViewDetails}
                onAccept={handleAcceptOrder}
                onComplete={handleCompleteOrder}
                isAcceptLoading={acceptMutation.isPending}
                isCompleteLoading={completeMutation.isPending}
                title="Assigned Orders"
                subtitle="Orders assigned to you by dispatchers"
                emptyMessage="No assigned orders"
                emptyIcon="📋"
            />

            {/* Order Details Modal */}
            {selectedOrderId && (
                <OrderDetails
                    isOpen={!!selectedOrderId}
                    onClose={() => setSelectedOrderId(null)}
                    orderId={selectedOrderId}
                    onAccept={handleAcceptOrder}
                    onComplete={handleCompleteOrder}
                    isAcceptLoading={acceptMutation.isPending}
                    isCompleteLoading={completeMutation.isPending}
                />
            )}

            {/* Toast Notifications */}
            {toast && (
                <Toast
                    message={toast.message}
                    type={toast.type}
                    onClose={() => setToast(null)}
                    duration={4000}
                />
            )}
        </div>
    );
};

export default AssignedOrdersPage;