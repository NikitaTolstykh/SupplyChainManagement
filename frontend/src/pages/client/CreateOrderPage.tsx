import React, { useState} from "react";
import Button from "../../components/ui/Button.tsx";
import Input from "../../components/ui/Input.tsx";
import {useCreateOrder} from "../../hooks/client/useCreateOrder.ts";

const CreateOrderPage: React.FC = () => {
    const [orderDetails, setOrderDetails] = useState({
        pickupLocation: '',
        dropoffLocation: '',
        description: '',
    });

    const createOrderMutation = useCreateOrder();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setOrderDetails(prev => ({ ...prev, [e.target.name]: e.target.value }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        createOrderMutation.mutate(orderDetails, {
            onSuccess: () => {
                alert('Order successfully created!');
                setOrderDetails({ pickupLocation: '', dropoffLocation: '', description: '' });
            },
            onError: () => {
                alert('Failed to create order. Please try again.');
            },
        });
    };

    return (
        <div>
            <h1 className="text-2xl font-semibold mb-6">Create New Order</h1>
            <form onSubmit={handleSubmit} className="max-w-lg space-y-4">
                <Input
                    label="Pickup Location"
                    name="pickupLocation"
                    value={orderDetails.pickupLocation}
                    onChange={handleChange}
                    required
                    disabled={createOrderMutation.isLoading}
                />
                <Input
                    label="Dropoff Location"
                    name="dropoffLocation"
                    value={orderDetails.dropoffLocation}
                    onChange={handleChange}
                    required
                    disabled={createOrderMutation.isLoading}
                />
                <div>
                    <label htmlFor="description" className="block mb-1 text-gray-700">
                        Description
                    </label>
                    <textarea
                        id="description"
                        name="description"
                        value={orderDetails.description}
                        onChange={handleChange}
                        rows={4}
                        disabled={createOrderMutation.isLoading}
                        className="w-full border rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 border-gray-300"
                        placeholder="Additional details..."
                    />
                </div>

                {createOrderMutation.isError && (
                    <p className="text-red-500">Error creating order, please try again.</p>
                )}

                <Button type="submit" variant="primary" disabled={createOrderMutation.isLoading}>
                    {createOrderMutation.isLoading ? 'Creating...' : 'Submit Order'}
                </Button>
            </form>
        </div>
    );
};

export default CreateOrderPage;

