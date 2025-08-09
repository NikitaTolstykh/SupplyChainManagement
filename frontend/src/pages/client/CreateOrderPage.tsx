import React, { useState } from "react";
import { useCreateOrder } from "../../hooks/client/useCreateOrder.ts";
import type { DistanceCategory } from "../../lib/types/DistanceCategory.ts";
import type { PaymentMethod } from "../../lib/types/PaymentMethod.ts";
import { DistanceCategoryValues } from "../../lib/types/DistanceCategory.ts";
import { PaymentMethodValues } from "../../lib/types/PaymentMethod.ts";

export interface OrderRequestDto {
    fromAddress: string;
    toAddress: string;
    cargoType: string;
    cargoDescription?: string;
    weightKg: number;
    comment?: string;
    distanceCategory: DistanceCategory;
    paymentMethod: PaymentMethod;
    pickupTime: string;
}

const DISTANCE_CATEGORIES: DistanceCategory[] = [
    DistanceCategoryValues.SHORT,
    DistanceCategoryValues.MEDIUM,
    DistanceCategoryValues.LONG
];

const PAYMENT_METHODS: PaymentMethod[] = [
    PaymentMethodValues.CASH,
    PaymentMethodValues.CARD,
    PaymentMethodValues.BANK_TRANSACTION,
    PaymentMethodValues.POSTAL_PAYMENT
];

const CreateOrderPage: React.FC = () => {
    const [order, setOrder] = useState<OrderRequestDto>({
        fromAddress: "",
        toAddress: "",
        cargoType: "",
        cargoDescription: "",
        weightKg: 0,
        comment: "",
        distanceCategory: DISTANCE_CATEGORIES[0],
        paymentMethod: PAYMENT_METHODS[0],
        pickupTime: "",
    });

    const createOrderMutation = useCreateOrder();

    const handleChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
    ) => {
        const { name, value } = e.target;

        setOrder((prev) => ({
            ...prev,
            [name]:
                name === "weightKg"
                    ? Number(value)
                    : value,
        }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        if (
            !order.fromAddress ||
            !order.toAddress ||
            !order.cargoType ||
            order.weightKg <= 0 ||
            !order.distanceCategory ||
            !order.paymentMethod ||
            !order.pickupTime
        ) {
            alert("Please fill all required fields correctly");
            return;
        }

        createOrderMutation.mutate(order, {
            onSuccess: () => {
                alert("Order successfully created!");
                setOrder({
                    fromAddress: "",
                    toAddress: "",
                    cargoType: "",
                    cargoDescription: "",
                    weightKg: 0,
                    comment: "",
                    distanceCategory: DISTANCE_CATEGORIES[0],
                    paymentMethod: PAYMENT_METHODS[0],
                    pickupTime: "",
                });
            },
            onError: () => {
                alert("Error creating order, please try again.");
            },
        });
    };

    const getDistanceCategoryLabel = (category: DistanceCategory): string => {
        switch (category) {
            case DistanceCategoryValues.SHORT: return "Short Distance";
            case DistanceCategoryValues.MEDIUM: return "Medium Distance";
            case DistanceCategoryValues.LONG: return "Long Distance";
            default: return category;
        }
    };

    const getPaymentMethodLabel = (method: PaymentMethod): string => {
        switch (method) {
            case PaymentMethodValues.CASH: return "Cash";
            case PaymentMethodValues.CARD: return "Card";
            case PaymentMethodValues.BANK_TRANSACTION: return "Bank Transfer";
            case PaymentMethodValues.POSTAL_PAYMENT: return "Postal Payment";
            default: return method;
        }
    };

    return (
        <div className="max-w-2xl mx-auto">
            <h1 className="text-2xl font-semibold mb-6">Create Order</h1>

            <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow space-y-6">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Pickup Address *
                        </label>
                        <input
                            type="text"
                            name="fromAddress"
                            value={order.fromAddress}
                            onChange={handleChange}
                            maxLength={500}
                            required
                            className="w-full border border-gray-300 px-3 py-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                            placeholder="Where to pick up cargo"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Delivery Address *
                        </label>
                        <input
                            type="text"
                            name="toAddress"
                            value={order.toAddress}
                            onChange={handleChange}
                            maxLength={500}
                            required
                            className="w-full border border-gray-300 px-3 py-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                            placeholder="Where to deliver cargo"
                        />
                    </div>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Cargo Type *
                        </label>
                        <input
                            type="text"
                            name="cargoType"
                            value={order.cargoType}
                            onChange={handleChange}
                            maxLength={100}
                            required
                            className="w-full border border-gray-300 px-3 py-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                            placeholder="e.g. Electronics"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Weight (kg) *
                        </label>
                        <input
                            type="number"
                            name="weightKg"
                            value={order.weightKg || ""}
                            onChange={handleChange}
                            min={0.1}
                            max={50000}
                            step={0.01}
                            required
                            className="w-full border border-gray-300 px-3 py-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                            placeholder="0.1 - 50000 kg"
                        />
                    </div>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                        Cargo Description
                    </label>
                    <textarea
                        name="cargoDescription"
                        value={order.cargoDescription}
                        onChange={handleChange}
                        maxLength={1000}
                        className="w-full border border-gray-300 px-3 py-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                        placeholder="Additional details about the cargo"
                        rows={3}
                    />
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                        Comment
                    </label>
                    <textarea
                        name="comment"
                        value={order.comment}
                        onChange={handleChange}
                        className="w-full border border-gray-300 px-3 py-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                        placeholder="Additional comments"
                        rows={2}
                    />
                </div>

                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Distance Category *
                        </label>
                        <select
                            name="distanceCategory"
                            value={order.distanceCategory}
                            onChange={handleChange}
                            required
                            className="w-full border border-gray-300 px-3 py-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                        >
                            {DISTANCE_CATEGORIES.map((cat) => (
                                <option key={cat} value={cat}>
                                    {getDistanceCategoryLabel(cat)}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Payment Method *
                        </label>
                        <select
                            name="paymentMethod"
                            value={order.paymentMethod}
                            onChange={handleChange}
                            required
                            className="w-full border border-gray-300 px-3 py-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                        >
                            {PAYMENT_METHODS.map((pm) => (
                                <option key={pm} value={pm}>
                                    {getPaymentMethodLabel(pm)}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Pickup Time *
                        </label>
                        <input
                            type="datetime-local"
                            name="pickupTime"
                            value={order.pickupTime}
                            onChange={handleChange}
                            required
                            className="w-full border border-gray-300 px-3 py-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                        />
                    </div>
                </div>

                {createOrderMutation.isError && (
                    <div className="bg-red-50 border border-red-200 rounded-md p-4">
                        <p className="text-red-800">Error creating order. Please try again.</p>
                    </div>
                )}

                <div className="flex justify-end">
                    <button
                        type="submit"
                        disabled={createOrderMutation.isPending}
                        className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-md disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                    >
                        {createOrderMutation.isPending ? "Creating..." : "Create Order"}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default CreateOrderPage;