import React, { useState } from 'react';
import { useVehicles } from '../../hooks/admin/useVehicles';
import VehicleTable from './components/VehicleTable.tsx';
import AddVehicleModal from './components/AddVehicleModal.tsx';
import Button from '../../components/ui/Button.tsx';
import Input from '../../components/ui/Input.tsx';
import Toast from '../../components/ui/Toast.tsx';
import type {VehicleDto, VehicleFormDto} from '../../lib/types/AdminDtos';

const VehiclesPage: React.FC = () => {
    const { vehiclesQuery, addVehicleMutation, editVehicleMutation, deleteVehicleMutation } = useVehicles();

    const [modalOpen, setModalOpen] = useState(false);
    const [editingVehicle, setEditingVehicle] = useState<VehicleDto | null>(null);
    const [toast, setToast] = useState<{ message: string; type?: 'success' | 'error' } | null>(null);
    const [searchQuery, setSearchQuery] = useState('');

    const openAddModal = () => {
        setEditingVehicle(null);
        setModalOpen(true);
    };

    const openEditModal = (vehicle: VehicleDto) => {
        setEditingVehicle(vehicle);
        setModalOpen(true);
    };

    const handleDelete = async (id: number) => {
        if (!confirm('Are you sure you want to delete this vehicle?')) return;

        try {
            await deleteVehicleMutation.mutateAsync(id);
            setToast({ message: 'Vehicle deleted successfully', type: 'success' });
        } catch (error) {
            setToast({ message: 'Failed to delete vehicle', type: 'error' });
        }
    };

    const handleSubmit = async (vehicle: VehicleFormDto) => {
        try {
            if (editingVehicle && editingVehicle.id) {
                await editVehicleMutation.mutateAsync({
                    id: editingVehicle.id,
                    data: vehicle,
                });
                setToast({ message: 'Vehicle updated successfully', type: 'success' });
            } else {
                await addVehicleMutation.mutateAsync(vehicle);
                setToast({ message: 'Vehicle added successfully', type: 'success' });
            }
            setModalOpen(false);
        } catch (error) {
            setToast({ message: 'Failed to save vehicle', type: 'error' });
        }
    };

    const filteredVehicles = React.useMemo(() => {
        if (!vehiclesQuery.data) return [];

        return vehiclesQuery.data.filter(vehicle => {
            const q = searchQuery.toLowerCase();
            return (
                vehicle.brand.toLowerCase().includes(q) ||
                vehicle.model.toLowerCase().includes(q) ||
                vehicle.licensePlate.toLowerCase().includes(q) ||
                vehicle.color.toLowerCase().includes(q)
            );
        });
    }, [vehiclesQuery.data, searchQuery]);

    if (vehiclesQuery.isLoading) {
        return (
            <div className="flex justify-center items-center h-64">
                <div className="text-lg text-gray-600">Loading vehicles...</div>
            </div>
        );
    }

    if (vehiclesQuery.isError) {
        return (
            <div className="bg-red-50 border border-red-200 rounded-lg p-4">
                <div className="text-red-800">Error loading vehicles</div>
            </div>
        );
    }

    return (
        <div className="space-y-6">
            {/* Header */}
            <div className="flex justify-between items-center">
                <div>
                    <h1 className="text-2xl font-semibold text-gray-900">Vehicles Management</h1>
                    <p className="text-gray-600">Manage delivery vehicles and assignments</p>
                </div>
                <Button variant="primary" onClick={openAddModal}>
                    Add New Vehicle
                </Button>
            </div>

            {/* Search */}
            <div className="bg-white rounded-lg shadow p-6">
                <Input
                    label="Search Vehicles"
                    placeholder="Search by brand, model, license plate, or color..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                />
            </div>

            {/* Stats Cards */}
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                <div className="bg-white rounded-lg shadow p-6">
                    <div className="flex items-center">
                        <div className="text-3xl font-bold text-blue-600">
                            {vehiclesQuery.data?.length || 0}
                        </div>
                        <div className="ml-4">
                            <div className="text-sm font-medium text-gray-500">Total Vehicles</div>
                        </div>
                    </div>
                </div>

                <div className="bg-white rounded-lg shadow p-6">
                    <div className="flex items-center">
                        <div className="text-3xl font-bold text-green-600">
                            {vehiclesQuery.data?.filter(v => v.driverId).length || 0}
                        </div>
                        <div className="ml-4">
                            <div className="text-sm font-medium text-gray-500">Assigned</div>
                        </div>
                    </div>
                </div>

                <div className="bg-white rounded-lg shadow p-6">
                    <div className="flex items-center">
                        <div className="text-3xl font-bold text-yellow-600">
                            {vehiclesQuery.data?.filter(v => !v.driverId).length || 0}
                        </div>
                        <div className="ml-4">
                            <div className="text-sm font-medium text-gray-500">Available</div>
                        </div>
                    </div>
                </div>

                <div className="bg-white rounded-lg shadow p-6">
                    <div className="flex items-center">
                        <div className="text-3xl font-bold text-purple-600">
                            {new Set(vehiclesQuery.data?.map(v => v.brand)).size || 0}
                        </div>
                        <div className="ml-4">
                            <div className="text-sm font-medium text-gray-500">Brands</div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Vehicles Table */}
            <div className="bg-white rounded-lg shadow">
                <div className="px-6 py-4 border-b">
                    <h2 className="text-lg font-medium text-gray-900">
                        Vehicles ({filteredVehicles.length})
                    </h2>
                </div>

                <div className="p-6">
                    {filteredVehicles.length === 0 ? (
                        <div className="text-center py-8 text-gray-500">
                            {searchQuery ? 'No vehicles found matching your search' : 'No vehicles found'}
                        </div>
                    ) : (
                        <VehicleTable
                            vehicles={filteredVehicles}
                            onEdit={openEditModal}
                            onDelete={handleDelete}
                        />
                    )}
                </div>
            </div>

            {/* Add/Edit Vehicle Modal */}
            <AddVehicleModal
                isOpen={modalOpen}
                onClose={() => setModalOpen(false)}
                onSubmit={handleSubmit}
                initialData={
                    editingVehicle
                        ? {
                            brand: editingVehicle.brand,
                            model: editingVehicle.model,
                            color: editingVehicle.color,
                            licensePlate: editingVehicle.licensePlate,
                            comment: editingVehicle.comment,
                            driverId: editingVehicle.driverId,
                        }
                        : null
                }
            />

            {/* Toast Notification */}
            {toast && (
                <Toast
                    message={toast.message}
                    type={toast.type}
                    onClose={() => setToast(null)}
                />
            )}
        </div>
    );
};

export default VehiclesPage;
