import React from 'react';
import Modal from '../../../components/ui/Modal';
import Button from '../../../components/ui/Button';
import {useOrderDetails} from "../../../hooks/driver/useDriverOrdersHook.ts";
import type {OrderStatus} from "../../../lib/types/OrderStatus.ts";

interface OrderDetailsProps {
    isOpen: boolean;
    onClose: () => void;
    orderId: number;
    onAccept?: (orderId: number) => void;
    onComplete?: (orderId: number) => void;
    isAcceptLoading?: boolean;
    isCompleteLoading?: boolean;
}

const OrderDetails: React.FC<OrderDetailsProps> = ({
                                                       isOpen,
                                                       onClose,
                                                       orderId,
                                                       onAccept,
                                                       onComplete,
                                                       isAcceptLoading = false,
                                                       isCompleteLoading = false
                                                   }) => {
    const {data: order, isLoading, isError} = useOrderDetails(orderId);

    const getStatusColor = (status: OrderStatus) => {
        switch (status) {
            case 'ASSIGNED':
                return 'bg-yellow-100 text-yellow-800';
            case 'IN_PROGRESS':
                return 'bg-blue-100 text-blue-800';
            case 'DELIVERED':
                return 'bg-green-100 text-green-800';
            case 'CANCELLED':
                return 'bg-red-100 text-red-800';
            default:
                return 'bg-gray-100 text-gray-800';
        }
    };

    const getPaymentMethodLabel = (method: string): string => {
        switch (method) {
            case "CASH":
                return "💰 Cash";
            case "CARD":
                return "💳 Card";
            case "BANK_TRANSACTION":
                return "🏦 Bank Transfer";
            case "POSTAL_PAYMENT":
                return "📮 Postal Payment";
            default:
                return method;
        }
    };

    if (!isOpen) return null;

    if (isLoading) {
        return (
            <Modal isOpen={isOpen} onClose={onClose} title="Loading...">
                <div className="text-center py-8">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
                    <p className="mt-4 text-gray-600">Loading order details...</p>
                </div>
            </Modal>
        );
    }

    if (isError || !order) {
        return (
            <Modal isOpen={isOpen} onClose={onClose} title="Error">
                <div className="text-center py-8">
                    <div className="text-red-600 text-4xl mb-4">❌</div>
                    <p className="text-red-600 font-medium">Failed to load order details.</p>
                </div>
            </Modal>
        );
    }

    return (
        <Modal isOpen={isOpen} onClose={onClose} title={`Order #${order.id} Details`}>
            <div className="max-w-2xl max-h-screen overflow-y-auto">
                <div className="space-y-6">
                    {/* Order Status */}
                    <div className="text-center">
                        <span
                            className={`inline-block px-4 py-2 rounded-full text-sm font-medium ${getStatusColor(order.status)}`}>
                            {order.status}
                        </span>
                    </div>

                    {/* Route Information */}
                    <div className="bg-gray-50 rounded-lg p-6">
                        <h3 className="text-lg font-semibold mb-4 text-gray-900">Route Information</h3>
                        <div className="space-y-4">
                            <div className="flex items-start space-x-3">
                                <span className="text-green-600 text-xl">📍</span>
                                <div className="flex-1">
                                    <p className="text-sm font-medium text-gray-700">Pickup Address</p>
                                    <p className="text-gray-900">{order.fromAddress}</p>
                                </div>
                            </div>
                            <div className="border-l-2 border-gray-300 ml-2 h-6"></div>
                            <div className="flex items-start space-x-3">
                                <span className="text-red-600 text-xl">📍</span>
                                <div className="flex-1">
                                    <p className="text-sm font-medium text-gray-700">Delivery Address</p>
                                    <p className="text-gray-900">{order.toAddress}</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Order Details */}
                    <div className="bg-gray-50 rounded-lg p-6">
                        <h3 className="text-lg font-semibold mb-4 text-gray-900">Order Details</h3>
                        <div className="space-y-3">
                            <div className="flex justify-between">
                                <span className="text-gray-700">Cargo Type:</span>
                                <span className="font-medium text-gray-900">{order.cargoType}</span>
                            </div>
                            <div className="flex justify-between">
                                <span className="text-gray-700">Weight:</span>
                                <span className="font-medium text-gray-900">{order.weightKg} kg</span>
                            </div>
                            <div className="flex justify-between">
                                <span className="text-gray-700">Price:</span>
                                <span className="font-medium text-green-600 text-lg">${order.price}</span>
                            </div>
                            <div className="flex justify-between">
                                <span className="text-gray-700">Payment:</span>
                                <span
                                    className="font-medium text-gray-900">{getPaymentMethodLabel(order.paymentMethod)}</span>
                            </div>
                            <div className="flex justify-between">
                                <span className="text-gray-700">Pickup Time:</span>
                                <span className="font-medium text-gray-900">
                                    {new Date(order.pickupTime).toLocaleString()}
                                </span>
                            </div>
                        </div>
                    </div>

                    {/* Cargo Description */}
                    {order.cargoDescription && (
                        <div className="bg-gray-50 rounded-lg p-6">
                            <h3 className="text-lg font-semibold mb-3 text-gray-900">Cargo Description</h3>
                            <p className="text-gray-700">{order.cargoDescription}</p>
                        </div>
                    )}

                    {/* Special Instructions */}
                    {order.comment && (
                        <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-6">
                            <h3 className="text-lg font-semibold mb-3 text-yellow-800">Special Instructions</h3>
                            <p className="text-yellow-700">{order.comment}</p>
                        </div>
                    )}

                    {/* Client Information */}
                    <div className="bg-gray-50 rounded-lg p-6">
                        <h3 className="text-lg font-semibold mb-4 text-gray-900">Client Information</h3>
                        <div className="space-y-3">
                            <div className="flex items-center space-x-3">
                                <span className="text-lg">👤</span>
                                <div>
                                    <p className="text-sm text-gray-700">Name</p>
                                    <p className="font-medium text-gray-900">{order.clientName}</p>
                                </div>
                            </div>
                            <div className="flex items-center space-x-3">
                                <span className="text-lg">📞</span>
                                <div>
                                    <p className="text-sm text-gray-700">Phone</p>
                                    <p className="font-medium text-gray-900">{order.clientPhoneNumber}</p>
                                </div>
                            </div>
                            <div className="flex items-center space-x-3">
                                <span className="text-lg">✉️</span>
                                <div>
                                    <p className="text-sm text-gray-700">Email</p>
                                    <p className="font-medium text-gray-900">{order.clientEmail}</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Vehicle Information */}
                    {order.licensePlate && (
                        <div className="bg-blue-50 border border-blue-200 rounded-lg p-6">
                            <h3 className="text-lg font-semibold mb-3 text-blue-800">Assigned Vehicle</h3>
                            <div className="flex items-center space-x-3">
                                <span className="text-lg">🚛</span>
                                <div>
                                    <p className="text-sm text-blue-700">License Plate</p>
                                    <p className="font-medium text-blue-900">{order.licensePlate}</p>
                                </div>
                            </div>
                        </div>
                    )}

                    {/* Action Buttons */}
                    <div className="flex space-x-3 pt-4 border-t">
                        {order.status === 'ASSIGNED' && onAccept && (
                            <Button
                                variant="success"
                                onClick={() => onAccept(order.id)}
                                disabled={isAcceptLoading}
                                className="flex-1"
                            >
                                {isAcceptLoading ? 'Accepting...' : '✓ Accept Order'}
                            </Button>
                        )}

                        {order.status === 'IN_PROGRESS' && onComplete && (
                            <Button
                                variant="primary"
                                onClick={() => onComplete(order.id)}
                                disabled={isCompleteLoading}
                                className="flex-1"
                            >
                                {isCompleteLoading ? 'Completing...' : '🏁 Complete Order'}
                            </Button>
                        )}

                        <Button variant="secondary" onClick={onClose}>
                            Close
                        </Button>
                    </div>
                </div>
            </div>
        </Modal>
    );
};

export default OrderDetails;