import type {OrderStatus} from "./OrderStatus.ts";

export interface DriverOrderListItemDto {
    id: number;
    fromAddress: string;
    toAddress: string;
    status: OrderStatus;
    pickupTime: string;
    price: number;
}