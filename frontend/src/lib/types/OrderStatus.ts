export type OrderStatus = "CREATED" | "ACCEPTED" | "ASSIGNED" | "IN_PROGRESS" | "DELIVERED" | "CANCELLED";

export const OrderStatusValues = {
    CREATED: "CREATED",
    ACCEPTED: "ACCEPTED",
    ASSIGNED: "ASSIGNED",
    IN_PROGRESS: "IN_PROGRESS",
    DELIVERED: "DELIVERED",
    CANCELLED: "CANCELLED",
} as const;
