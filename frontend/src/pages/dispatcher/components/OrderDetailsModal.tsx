import React, {useState} from 'react';
import Modal from '../../../components/ui/Modal';
import Button from '../../../components/ui/Button';
import {useOrderDetails, useOrderStatusHistory} from '../../../hooks/dispatcher/useDispatcherOrders';
import {useUpdateOrderStatus, useCancelOrder} from '../../../hooks/dispatcher/useOrderActions';
import type {OrderStatus} from '../../../lib/types/OrderStatus';

interface OrderDetailsModalProps {
    isOpen: boolean;
    onClose: () => void;
    orderId: number;
    onAssignDriver?: (orderId: number) => void;
}

const OrderDetailsModal: React.FC<OrderDetailsModalProps> = ({
                                                                 isOpen,
                                                                 onClose,
                                                                 orderId,
                                                                 onAssignDriver
                                                             }) => {
    const [newStatus, setNewStatus] = useState<OrderStatus | ''>('');

    const {data: order, isLoading: orderLoading, isError: orderError} = useOrderDetails(orderId);
    const {data: statusHistory} = useOrderStatusHistory(orderId);
    const updateStatusMutation = useUpdateOrderStatus();
    const cancelOrderMutation = useCancelOrder();

    const getStatusColor = (status: OrderStatus) => {
        switch (status) {
            case 'DELIVERED':
                return 'bg-green-100 text-green-800';
            case 'CANCELLED':
                return 'bg-red-100 text-red-800';
            case 'IN_PROGRESS':
                return 'bg-blue-100 text-blue-800';
            case 'ASSIGNED':
                return 'bg-purple-100 text-purple-800';
            case 'ACCEPTED':
                return 'bg-yellow-100 text-yellow-800';
            case 'CREATED':
                return 'bg-gray-100 text-gray-800';
            default:
                return 'bg-gray-100 text-gray-800';
        }
    };

    const getPaymentMethodLabel = (method: string): string => {
        switch (method) {
            case "CASH":
                return "Cash";
            case "CARD":
                return "Card";
            case "BANK_TRANSACTION":
                return "Bank Transfer";
            case "POSTAL_PAYMENT":
                return "Postal Payment";
            default:
                return method;
        }
    };

    const handleUpdateStatus = async () => {
        if (!newStatus || !order) return;

        try {
            await updateStatusMutation.mutateAsync({
                orderId: order.id,
                data: {status: newStatus}
            });
            setNewStatus('');
        } catch (error) {
            console.error('Failed to update status:', error);
        }
    };

    const handleCancelOrder = async () => {
        if (!order || !confirm('Are you sure you want to cancel this order?')) return;

        try {
            await cancelOrderMutation.mutateAsync(order.id);
        } catch (error) {
            console.error('Failed to cancel order:', error);
        }
    };

    if (!isOpen) return null;

    if (orderLoading) {
        return (
            <Modal isOpen={isOpen} onClose={onClose} title="Loading...">
                <div className="text-center py-8">
                    <div className="text-gray-600">Loading order details...</div>
                </div>
            </Modal>
        );
    }

    if (orderError || !order) {
        return (
            <Modal isOpen={isOpen} onClose={onClose} title="Error">
                <div className="text-center py-8">
                    <div className="text-red-600">Failed to load order details.</div>
                </div>
            </Modal>
        );
    }

    const canAssignDriver = order.status === 'ACCEPTED' && !order.driverId;
    const canUpdateStatus = order.status !== 'CANCELLED' && order.status !== 'DELIVERED';
    const canCancel = order.status === 'CREATED' || order.status === 'ACCEPTED';

    // Available status transitions based on current status
    const getAvailableStatuses = (currentStatus: OrderStatus): OrderStatus[] => {
        const statusTransitions: Record<OrderStatus, OrderStatus[]> = {
            'CREATED': ['ACCEPTED', 'CANCELLED'],
            'ACCEPTED': ['ASSIGNED', 'CANCELLED'],
            'ASSIGNED': ['IN_PROGRESS', 'CANCELLED'],
            'IN_PROGRESS': ['DELIVERED'],
            'DELIVERED': [],
            'CANCELLED': []
        };
        return statusTransitions[currentStatus] || [];
    };

    const availableStatuses = getAvailableStatuses(order.status);

    return (
        <Modal isOpen={isOpen} onClose={onClose} title={`Order #${order.id} Details`}>
            <div className="max-w-4xl max-h-screen overflow-y-auto">
                {/* Order Information */}
                <div className="space-y-6">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div className="space-y-3">
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
                            <div>
                                <span className="block text-sm font-medium text-gray-700">Weight:</span>
                                <span className="text-gray-900">{order.weightKg} kg</span>
                            </div>
                        </div>

                        <div className="space-y-3">
                            <div>
                                <span className="block text-sm font-medium text-gray-700">Status:</span>
                                <span
                                    className={`inline-block px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(order.status)}`}>
                                    {order.status}
                                </span>
                            </div>
                            <div>
                                <span className="block text-sm font-medium text-gray-700">Price:</span>
                                <span className="text-gray-900 text-lg font-semibold">${order.price}</span>
                            </div>
                            <div>
                                <span className="block text-sm font-medium text-gray-700">Payment:</span>
                                <span className="text-gray-900">{getPaymentMethodLabel(order.paymentMethod)}</span>
                            </div>
                            <div>
                                <span className="block text-sm font-medium text-gray-700">Pickup Time:</span>
                                <span className="text-gray-900">{new Date(order.pickupTime).toLocaleString()}</span>
                            </div>
                        </div>
                    </div>

                    {/* Client Information */}
                    <div className="bg-gray-50 rounded-lg p-4">
                        <h3 className="text-lg font-semibold mb-3">Client Information</h3>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <span className="block text-sm font-medium text-gray-700">Name:</span>
                                <span className="text-gray-900">{order.clientName}</span>
                            </div>
                            <div>
                                <span className="block text-sm font-medium text-gray-700">Email:</span>
                                <span className="text-gray-900">{order.clientEmail}</span>
                            </div>
                            <div>
                                <span className="block text-sm font-medium text-gray-700">Phone:</span>
                                <span className="text-gray-900">{order.clientPhoneNumber}</span>
                            </div>
                        </div>
                    </div>

                    {/* Driver Information */}
                    {order.driverId && (
                        <div className="bg-blue-50 rounded-lg p-4">
                            <h3 className="text-lg font-semibold mb-3">Driver Information</h3>
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <div>
                                    <span className="block text-sm font-medium text-gray-700">Driver:</span>
                                    <span className="text-gray-900">{order.driverName}</span>
                                </div>
                                <div>
                                    <span className="block text-sm font-medium text-gray-700">License Plate:</span>
                                    <span className="text-gray-900">{order.licensePlate}</span>
                                </div>
                            </div>
                        </div>
                    )}

                    {/* Description and Comments */}
                    {(order.cargoDescription || order.comment) && (
                        <div className="space-y-3">
                            {order.cargoDescription && (
                                <div>
                                    <span className="block text-sm font-medium text-gray-700">Description:</span>
                                    <span className="text-gray-900">{order.cargoDescription}</span>
                                </div>
                            )}
                            {order.comment && (
                                <div>
                                    <span className="block text-sm font-medium text-gray-700">Comment:</span>
                                    <span className="text-gray-900">{order.comment}</span>
                                </div>
                            )}
                        </div>
                    )}

                    {/* Error Messages */}
                    {updateStatusMutation.isError && (
                        <div className="bg-red-50 border border-red-200 rounded-lg p-4">
                            <p className="text-red-800">Error updating status. Please try again.</p>
                        </div>
                    )}

                    {cancelOrderMutation.isError && (
                        <div className="bg-red-50 border border-red-200 rounded-lg p-4">
                            <p className="text-red-800">Error cancelling order. Please try again.</p>
                        </div>
                    )}

                    {/* Actions */}
                    <div className="flex flex-wrap gap-3">
                        {canAssignDriver && (
                            <Button
                                variant="success"
                                onClick={() => onAssignDriver?.(order.id)}
                            >
                                Assign Driver
                            </Button>
                        )}

                        {canUpdateStatus && availableStatuses.length > 0 && (
                            <div className="flex gap-2">
                                <select
                                    value={newStatus}
                                    onChange={(e) => setNewStatus(e.target.value as OrderStatus)}
                                    className="border border-gray-300 rounded px-3 py-1 text-sm"
                                >
                                    <option value="">Update Status...</option>
                                    {availableStatuses.map(status => (
                                        <option key={status} value={status}>{status}</option>
                                    ))}
                                </select>
                                <Button
                                    variant="primary"
                                    onClick={handleUpdateStatus}
                                    disabled={!newStatus || updateStatusMutation.isPending}
                                >
                                    {updateStatusMutation.isPending ? 'Updating...' : 'Update'}
                                </Button>
                            </div>
                        )}

                        {canCancel && (
                            <Button
                                variant="danger"
                                onClick={handleCancelOrder}
                                disabled={cancelOrderMutation.isPending}
                            >
                                {cancelOrderMutation.isPending ? 'Cancelling...' : 'Cancel Order'}
                            </Button>
                        )}
                    </div>

                    {/* Status History */}
                    {statusHistory && statusHistory.length > 0 && (
                        <div className="bg-white rounded-lg border p-4">
                            <h3 className="text-lg font-semibold mb-3">Status History</h3>
                            <div className="space-y-2">
                                {statusHistory.map((status) => (
                                    <div key={status.id}
                                         className="flex items-center justify-between border-l-4 border-blue-200 pl-3 py-1">
                                        <div>
                                            <span className="font-medium">{status.fromStatus} → {status.toStatus}</span>
                                            <span className="text-sm text-gray-600 ml-2">by {status.changedBy}</span>
                                        </div>
                                        <span className="text-sm text-gray-500">
                                            {new Date(status.changedAt).toLocaleString()}
                                        </span>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </Modal>
    );
};

export default OrderDetailsModal;