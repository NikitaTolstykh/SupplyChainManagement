import type {DistanceCategory} from "./DistanceCategory.ts";
import type {PaymentMethod} from "./PaymentMethod.ts";
import type {OrderStatus} from "./OrderStatus.ts";


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

export interface OrderListItemDto {
    id: number;
    fromAddress: string;
    toAddress: string;
    cargoType: string;
    weightKg: number;
    paymentMethod: PaymentMethod;
    status: OrderStatus;
    price: number;
    createdAt: string;
    pickupTime?: string;
}

export interface OrderDetailsDto {
    id: number;
    fromAddress: string;
    toAddress: string;
    cargoType: string;
    cargoDescription?: string;
    weightKg: number;
    comment?: string;
    distanceCategory: DistanceCategory;
    paymentMethod: PaymentMethod;
    pickupTime: string;
    status: OrderStatus;
    price: number;
    createdAt: string;
    updatedAt: string;
}

export interface OrderStatusHistoryDto {
    id: number;
    fromStatus: OrderStatus;
    toStatus: OrderStatus;
    changedBy: string;
    changedAt: string;
}

export interface OrderRatingRequestDto {
    stars: number; // 1-5
    comment?: string;
}

export interface OrderRatingResponseDto {
    id: number;
    orderId: number;
    stars: number;
    comment?: string;
    createdAt: string;
    clientFullName?: string;
    updatedAt?: string;
}

export interface ClientStatisticsDto {
    totalOrders: number;
    completedOrders: number;
    cancelledOrders: number;
    totalSpent: number;
    averageOrderValue: number;
    ordersByMonth: Record<string, number>;
    spentByMonth: Record<string, number>;
    averageRating: number;
    mostUsedRoute: string | null;
}
