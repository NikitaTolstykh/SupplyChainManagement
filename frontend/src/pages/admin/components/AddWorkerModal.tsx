import React, { useState, useEffect } from 'react';
import Modal from "../../../components/ui/Modal.tsx";
import Input from "../../../components/ui/Input.tsx";
import Button from "../../../components/ui/Button.tsx";
import type {UserRequestDto} from "../../../lib/types/AdminDtos.ts";
import { Role} from "../../../lib/types/Role.ts";

interface AddWorkerModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSubmit: (worker: UserRequestDto) => void;
    initialData?: UserRequestDto | null;
}

const AddWorkerModal: React.FC<AddWorkerModalProps> = ({
                                                           isOpen,
                                                           onClose,
                                                           onSubmit,
                                                           initialData
                                                       }) => {
    const [form, setForm] = useState<UserRequestDto>({
        email: '',
        firstName: '',
        lastName: '',
        phone: '',
        password: '',
        role: Role.DRIVER,
    });

    const [errors, setErrors] = useState<Record<string, string>>({});

    useEffect(() => {
        if (initialData) {
            setForm({ ...initialData });
        } else {
            setForm({
                email: '',
                firstName: '',
                lastName: '',
                phone: '',
                password: '',
                role: Role.DRIVER,
            });
        }
        setErrors({});
    }, [initialData, isOpen]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setForm(prev => ({
            ...prev,
            [name]: name === 'role' ? value as Role : value
        }));

        // Clear error when user starts typing
        if (errors[name]) {
            setErrors(prev => ({ ...prev, [name]: '' }));
        }
    };

    const validate = () => {
        const newErrors: Record<string, string> = {};

        if (!form.email) newErrors.email = 'Email is required';
        if (!form.email.includes('@')) newErrors.email = 'Please enter a valid email';

        if (!form.firstName) newErrors.firstName = 'First name is required';
        if (!form.lastName) newErrors.lastName = 'Last name is required';
        if (!form.phone) newErrors.phone = 'Phone is required';

        if (!initialData && !form.password) {
            newErrors.password = 'Password is required';
        }
        if (form.password && form.password.length < 6) {
            newErrors.password = 'Password must be at least 6 characters';
        }

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
            title={initialData ? 'Edit Worker' : 'Add Worker'}
        >
            <div className="space-y-4">
                <Input
                    label="Email"
                    name="email"
                    type="email"
                    value={form.email}
                    onChange={handleChange}
                    error={errors.email}
                />

                <Input
                    label="First Name"
                    name="firstName"
                    value={form.firstName}
                    onChange={handleChange}
                    error={errors.firstName}
                />

                <Input
                    label="Last Name"
                    name="lastName"
                    value={form.lastName}
                    onChange={handleChange}
                    error={errors.lastName}
                />

                <Input
                    label="Phone"
                    name="phone"
                    value={form.phone}
                    onChange={handleChange}
                    error={errors.phone}
                />

                <div className="mb-4">
                    <label className="block text-gray-700 mb-1">Role</label>
                    <select
                        name="role"
                        value={form.role}
                        onChange={handleChange}
                        className="w-full border rounded py-2 px-3 focus:outline-none focus:ring-2 focus:ring-blue-500 border-gray-300"
                    >
                        <option value={Role.DRIVER}>Driver</option>
                        <option value={Role.DISPATCHER}>Dispatcher</option>
                    </select>
                </div>

                {!initialData && (
                    <Input
                        label="Password"
                        name="password"
                        type="password"
                        value={form.password}
                        onChange={handleChange}
                        error={errors.password}
                    />
                )}

                <div className="flex justify-end gap-2 mt-6">
                    <Button variant="secondary" onClick={handleClose}>
                        Cancel
                    </Button>
                    <Button variant="primary" onClick={handleSubmit}>
                        {initialData ? 'Save Changes' : 'Add Worker'}
                    </Button>
                </div>
            </div>
        </Modal>
    );
};

export default AddWorkerModal;