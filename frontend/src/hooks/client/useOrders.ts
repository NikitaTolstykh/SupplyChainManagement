import { useQuery } from "@tanstack/react-query";
import type { OrderListItemDto, OrderDetailsDto, OrderStatusHistoryDto } from "../../lib/types/ClientDtos.ts";
import * as clientApi from "../../lib/api/clientService.ts";

export function useOrders() {
    return useQuery<OrderListItemDto[]>({
        queryKey: ["clientOrders"],
        queryFn: clientApi.fetchOrders,
    });
}

export function useOrderDetails(orderId: number) {
    return useQuery<OrderDetailsDto>({
        queryKey: ["clientOrderDetails", orderId],
        queryFn: () => clientApi.fetchOrderDetails(orderId),
        enabled: !!orderId && orderId > 0,
    });
}

export function useOrderStatusHistory(orderId: number) {
    return useQuery<OrderStatusHistoryDto[]>({
        queryKey: ["orderStatusHistory", orderId],
        queryFn: () => clientApi.fetchOrderStatusHistory(orderId),
        enabled: !!orderId && orderId > 0,
    });
}