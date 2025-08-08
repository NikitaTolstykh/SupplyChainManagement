import {useQuery} from "@tanstack/react-query";
import type {OrderListItemDto, OrderDetailsDto, OrderStatusHistoryDto} from "../../lib/types/ClientDtos.ts";
import * as clientApi from "../../lib/api/clientService.ts";

export function useOrders() {
    return useQuery<OrderListItemDto[]>(["clientOrders"], clientApi.fetchOrders());
}

export function useOrderDetails(orderId: number) {
    return useQuery<OrderDetailsDto>(
        ["clientOrderDetails", orderId],
        () => clientApi.fetchOrderDetails(orderId),
        {enabled: !!orderId}
    );
}


export function useOrderStatusHistory(orderId: number) {
    return useQuery<OrderStatusHistoryDto[]>(
        ["orderStatusHistory", orderId],
        () => clientApi.fetchOrderStatusHistory(orderId),
        {enabled: !!orderId}
    );
}