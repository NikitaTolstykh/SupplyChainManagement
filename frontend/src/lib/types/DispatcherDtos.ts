import type {OrderStatus} from "./OrderStatus.ts";
import type {PaymentMethod} from "./PaymentMethod.ts";

export interface DispatcherOrderDetailsDto {
    id: number;
    fromAddress: string;
    toAddress: string;
    cargoType: string;
    cargoDescription?: string;
    weightKg: number;
    comment?: string;
    price: number;
    paymentMethod: PaymentMethod;
    status: OrderStatus;
    pickupTime: string;
    createdAt: string;
    updatedAt: string;

    clientId: number;
    clientName: string;
    clientEmail: string;
    clientPhoneNumber: string;

    driverId?: number;
    driverName?: string;

    vehicleId?: number;
    licensePlate?: string;
}

export interface AvailableDriverDto {
    id: number;
    firstName: string;
    lastName: string;
    phone: string;
    email: string;
    vehicleBrand: string;
    vehicleModel: string;
    licensePlate: string;
}

export interface OrderRatingResponseDto {
    id: number;
    stars: number;
    comment?: string;
    createdAt: string;
    orderId: number;
    clientFullName: string;
}

export interface AssignDriverRequestDto {
    driverId: number;
}

export interface UpdateOrderStatusRequestDto {
    status: OrderStatus;
}

export interface OrderStatusHistoryDto {
    id: number;
    fromStatus: OrderStatus;
    toStatus: OrderStatus;
    changedBy: string;
    changedAt: string;
}