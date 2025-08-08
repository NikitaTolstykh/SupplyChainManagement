import React, { useState, useEffect } from 'react';
import Modal from "../../../components/ui/Modal.tsx";
import Input from "../../../components/ui/Input.tsx";
import Button from "../../../components/ui/Button.tsx";
import type {VehicleFormDto} from "../../../lib/types/AdminDtos.ts";

interface AddVehicleModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSubmit: (vehicle: VehicleFormDto) => void;
    initialData?: VehicleFormDto | null;
}

const AddVehicleModal: React.FC<AddVehicleModalProps> = ({
                                                             isOpen,
                                                             onClose,
                                                             onSubmit,
                                                             initialData
                                                         }) => {
    const [form, setForm] = useState<VehicleFormDto>({
        brand: '',
        model: '',
        color: '',
        licensePlate: '',
        comment: '',
        driverId: 0,
    });

    const [errors, setErrors] = useState<Record<string, string>>({});

    useEffect(() => {
        if (initialData) {
            setForm({ ...initialData });
        } else {
            setForm({
                brand: '',
                model: '',
                color: '',
                licensePlate: '',
                comment: '',
                driverId: 0,
            });
        }
        setErrors({});
    }, [initialData, isOpen]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setForm(prev => ({
            ...prev,
            [name]: name === 'driverId' ? Number(value) : value,
        }));

        // Clear error when user starts typing
        if (errors[name]) {
            setErrors(prev => ({ ...prev, [name]: '' }));
        }
    };

    const validate = () => {
        const newErrors: Record<string, string> = {};

        if (!form.brand) newErrors.brand = 'Brand is required';
        if (!form.model) newErrors.model = 'Model is required';
        if (!form.color) newErrors.color = 'Color is required';
        if (!form.licensePlate) newErrors.licensePlate = 'License plate is required';
        if (!form.driverId || form.driverId <= 0) newErrors.driverId = 'Driver ID is required';

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = () => {
        if (!validate()) return;
        onSubmit(form);
    };

    const handleClose = () => {
        setErrors({});
        onClose();
    };

    return (
        <Modal
            isOpen={isOpen}
            onClose={handleClose}
            title={initialData ? 'Edit Vehicle' : 'Add Vehicle'}
        >
            <div className="space-y-4">
                <Input
                    label="Brand"
                    name="brand"
                    value={form.brand}
                    onChange={handleChange}
                    error={errors.brand}
                />

                <Input
                    label="Model"
                    name="model"
                    value={form.model}
                    onChange={handleChange}
                    error={errors.model}
                />

                <Input
                    label="Color"
                    name="color"
                    value={form.color}
                    onChange={handleChange}
                    error={errors.color}
                />

                <Input
                    label="License Plate"
                    name="licensePlate"
                    value={form.licensePlate}
                    onChange={handleChange}
                    error={errors.licensePlate}
                />

                <Input
                    label="Driver ID"
                    name="driverId"
                    type="number"
                    value={form.driverId}
                    onChange={handleChange}
                    error={errors.driverId}
                />

                <div className="mb-4">
                    <label className="block text-gray-700 mb-1">Comment (Optional)</label>
                    <textarea
                        name="comment"
                        value={form.comment || ''}
                        onChange={handleChange}
                        className="w-full border rounded py-2 px-3 focus:outline-none focus:ring-2 focus:ring-blue-500 border-gray-300"
                        rows={3}
                        placeholder="Enter any additional comments..."
                    />
                </div>

                <div className="flex justify-end gap-2 mt-6">
                    <Button variant="secondary" onClick={handleClose}>
                        Cancel
                    </Button>
                    <Button variant="primary" onClick={handleSubmit}>
                        {initialData ? 'Save Changes' : 'Add Vehicle'}
                    </Button>
                </div>
            </div>
        </Modal>
    );
};

export default AddVehicleModal;