import React, {useState} from 'react';
import Modal from '../../../components/ui/Modal';
import Button from '../../../components/ui/Button';
import {useAvailableDrivers} from '../../../hooks/dispatcher/useAvailableDrivers';
import {useAssignDriver} from '../../../hooks/dispatcher/useOrderActions';

interface AssignDriverModalProps {
    isOpen: boolean;
    onClose: () => void;
    orderId: number;
    orderInfo?: string;
}

const AssignDriverModal: React.FC<AssignDriverModalProps> = ({
                                                                 isOpen,
                                                                 onClose,
                                                                 orderId,
                                                                 orderInfo
                                                             }) => {
    const [selectedDriverId, setSelectedDriverId] = useState<number | null>(null);

    const {data: drivers, isLoading, isError} = useAvailableDrivers();
    const assignDriverMutation = useAssignDriver();

    const handleAssign = () => {
        if (!selectedDriverId) return;

        assignDriverMutation.mutate(
            {orderId, data: {driverId: selectedDriverId}},
            {
                onSuccess: () => {
                    onClose();
                    setSelectedDriverId(null);
                },
            }
        );
    };

    const handleClose = () => {
        onClose();
        setSelectedDriverId(null);
    };

    return (
        <Modal isOpen={isOpen} onClose={handleClose} title={`Assign Driver to Order #${orderId}`}>
            <div className="space-y-4">
                {orderInfo && (
                    <div className="bg-blue-50 border border-blue-200 rounded-lg p-3">
                        <p className="text-sm text-blue-800">{orderInfo}</p>
                    </div>
                )}

                {isLoading && (
                    <div className="text-center py-4">
                        <div className="text-gray-600">Loading available drivers...</div>
                    </div>
                )}

                {isError && (
                    <div className="bg-red-50 border border-red-200 rounded-lg p-4">
                        <p className="text-red-800">Error loading drivers. Please try again.</p>
                    </div>
                )}

                {drivers && drivers.length === 0 && (
                    <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
                        <p className="text-yellow-800">No drivers available at the moment.</p>
                    </div>
                )}

                {drivers && drivers.length > 0 && (
                    <div className="space-y-3 max-h-64 overflow-y-auto">
                        {drivers.map((driver) => (
                            <div
                                key={driver.id}
                                className={`border rounded-lg p-4 cursor-pointer transition-colors ${
                                    selectedDriverId === driver.id
                                        ? 'border-blue-500 bg-blue-50'
                                        : 'border-gray-200 hover:border-gray-300'
                                }`}
                                onClick={() => setSelectedDriverId(driver.id)}
                            >
                                <div className="flex items-center justify-between">
                                    <div>
                                        <h4 className="font-semibold text-gray-900">
                                            {driver.firstName} {driver.lastName}
                                        </h4>
                                        <p className="text-sm text-gray-600">{driver.email}</p>
                                        <p className="text-sm text-gray-600">{driver.phone}</p>
                                    </div>
                                    <div className="text-right text-sm">
                                        <p className="text-gray-700">
                                            {driver.vehicleBrand} {driver.vehicleModel}
                                        </p>
                                        <p className="text-gray-600">{driver.licensePlate}</p>
                                    </div>
                                </div>
                                {selectedDriverId === driver.id && (
                                    <div className="mt-2 flex items-center justify-center">
                                        <div
                                            className="w-4 h-4 bg-blue-500 rounded-full flex items-center justify-center">
                                            <svg className="w-2 h-2 text-white" fill="currentColor" viewBox="0 0 8 8">
                                                <path d="M6.564.75l-3.59 3.612-1.538-1.55L0 4.26l2.974 2.99L8 2.193z"/>
                                            </svg>
                                        </div>
                                    </div>
                                )}
                            </div>
                        ))}
                    </div>
                )}

                {assignDriverMutation.isError && (
                    <div className="bg-red-50 border border-red-200 rounded-lg p-4">
                        <p className="text-red-800">Error assigning driver. Please try again.</p>
                    </div>
                )}

                <div className="flex justify-end space-x-3 pt-4 border-t">
                    <Button variant="secondary" onClick={handleClose}>
                        Cancel
                    </Button>
                    <Button
                        variant="primary"
                        onClick={handleAssign}
                        disabled={!selectedDriverId || assignDriverMutation.isPending}
                    >
                        {assignDriverMutation.isPending ? 'Assigning...' : 'Assign Driver'}
                    </Button>
                </div>
            </div>
        </Modal>
    );
};

export default AssignDriverModal;