import React, { useState } from 'react';
import { useWorkers } from '../../hooks/admin/useWorkers';
import WorkerTable from "./components/WorkerTable.tsx";
import AddWorkerModal from "./components/AddWorkerModal.tsx";
import Button from "../../components/ui/Button.tsx";
import Input from "../../components/ui/Input.tsx";
import Toast from "../../components/ui/Toast.tsx";
import type {UserResponseDto, UserRequestDto} from "../../lib/types/AdminDtos.ts";
import { Role } from '../../lib/types/Role';

const WorkersPage: React.FC = () => {
    const { workersQuery, addWorkerMutation, editWorkerMutation, deleteWorkerMutation } = useWorkers();

    const [modalOpen, setModalOpen] = useState(false);
    const [editingWorker, setEditingWorker] = useState<UserResponseDto | null>(null);
    const [toast, setToast] = useState<{ message: string; type?: 'success' | 'error' } | null>(null);
    const [searchQuery, setSearchQuery] = useState('');
    const [roleFilter, setRoleFilter] = useState<Role | ''>('');

    const openAddModal = () => {
        setEditingWorker(null);
        setModalOpen(true);
    };

    const openEditModal = (worker: UserResponseDto) => {
        setEditingWorker(worker);
        setModalOpen(true);
    };

    const handleDelete = async (id: number) => {
        if (!confirm('Are you sure you want to delete this worker?')) {
            return;
        }

        try {
            await deleteWorkerMutation.mutateAsync(id);
            setToast({ message: 'Worker deleted successfully', type: 'success' });
        } catch (error) {
            setToast({ message: 'Failed to delete worker', type: 'error' });
        }
    };

    const handleSubmit = async (worker: UserRequestDto) => {
        try {
            if (editingWorker) {
                await editWorkerMutation.mutateAsync({
                    id: editingWorker.id,
                    data: worker
                });
                setToast({ message: 'Worker updated successfully', type: 'success' });
            } else {
                await addWorkerMutation.mutateAsync(worker);
                setToast({ message: 'Worker added successfully', type: 'success' });
            }
            setModalOpen(false);
        } catch (error) {
            setToast({ message: 'Failed to save worker', type: 'error' });
        }
    };

    // Filter workers based on search query and role
    const filteredWorkers = React.useMemo(() => {
        if (!workersQuery.data) return [];

        return workersQuery.data.filter(worker => {
            const matchesSearch = searchQuery === '' ||
                worker.firstName.toLowerCase().includes(searchQuery.toLowerCase()) ||
                worker.lastName.toLowerCase().includes(searchQuery.toLowerCase()) ||
                worker.email.toLowerCase().includes(searchQuery.toLowerCase());

            const matchesRole = roleFilter === '' || worker.role === roleFilter;

            return matchesSearch && matchesRole;
        });
    }, [workersQuery.data, searchQuery, roleFilter]);

    if (workersQuery.isLoading) {
        return (
            <div className="flex justify-center items-center h-64">
                <div className="text-lg text-gray-600">Loading workers...</div>
            </div>
        );
    }

    if (workersQuery.isError) {
        return (
            <div className="bg-red-50 border border-red-200 rounded-lg p-4">
                <div className="text-red-800">Error loading workers</div>
            </div>
        );
    }

    const convertWorkerForEdit = (worker: UserResponseDto): UserRequestDto => ({
        email: worker.email,
        firstName: worker.firstName,
        lastName: worker.lastName,
        phone: worker.phone,
        role: worker.role,
        password: '', // Will be handled by backend for updates
    });

    return (
        <div className="space-y-6">
            {/* Header */}
            <div className="flex justify-between items-center">
                <div>
                    <h1 className="text-2xl font-semibold text-gray-900">Workers Management</h1>
                    <p className="text-gray-600">Manage drivers and dispatchers</p>
                </div>
                <Button variant="primary" onClick={openAddModal}>
                    Add New Worker
                </Button>
            </div>

            {/* Filters */}
            <div className="bg-white rounded-lg shadow p-6">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <Input
                        label="Search Workers"
                        placeholder="Search by name or email..."
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                    />

                    <div>
                        <label className="block text-gray-700 mb-1">Filter by Role</label>
                        <select
                            value={roleFilter}
                            onChange={(e) => setRoleFilter(e.target.value as Role | '')}
                            className="w-full border rounded py-2 px-3 focus:outline-none focus:ring-2 focus:ring-blue-500 border-gray-300"
                        >
                            <option value="">All Roles</option>
                            <option value={Role.DRIVER}>Driver</option>
                            <option value={Role.DISPATCHER}>Dispatcher</option>
                        </select>
                    </div>
                </div>
            </div>

            {/* Stats Cards */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div className="bg-white rounded-lg shadow p-6">
                    <div className="flex items-center">
                        <div className="text-3xl font-bold text-blue-600">
                            {workersQuery.data?.length || 0}
                        </div>
                        <div className="ml-4">
                            <div className="text-sm font-medium text-gray-500">Total Workers</div>
                        </div>
                    </div>
                </div>

                <div className="bg-white rounded-lg shadow p-6">
                    <div className="flex items-center">
                        <div className="text-3xl font-bold text-green-600">
                            {workersQuery.data?.filter(w => w.role === Role.DRIVER).length || 0}
                        </div>
                        <div className="ml-4">
                            <div className="text-sm font-medium text-gray-500">Drivers</div>
                        </div>
                    </div>
                </div>

                <div className="bg-white rounded-lg shadow p-6">
                    <div className="flex items-center">
                        <div className="text-3xl font-bold text-purple-600">
                            {workersQuery.data?.filter(w => w.role === Role.DISPATCHER).length || 0}
                        </div>
                        <div className="ml-4">
                            <div className="text-sm font-medium text-gray-500">Dispatchers</div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Workers Table */}
            <div className="bg-white rounded-lg shadow">
                <div className="px-6 py-4 border-b">
                    <h2 className="text-lg font-medium text-gray-900">
                        Workers ({filteredWorkers.length})
                    </h2>
                </div>

                <div className="p-6">
                    {filteredWorkers.length === 0 ? (
                        <div className="text-center py-8 text-gray-500">
                            {searchQuery || roleFilter ? 'No workers found matching your filters' : 'No workers found'}
                        </div>
                    ) : (
                        <WorkerTable
                            workers={filteredWorkers}
                            onEdit={openEditModal}
                            onDelete={handleDelete}
                        />
                    )}
                </div>
            </div>

            {/* Add/Edit Worker Modal */}
            <AddWorkerModal
                isOpen={modalOpen}
                onClose={() => setModalOpen(false)}
                onSubmit={handleSubmit}
                initialData={editingWorker ? convertWorkerForEdit(editingWorker) : null}
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

export default WorkersPage;