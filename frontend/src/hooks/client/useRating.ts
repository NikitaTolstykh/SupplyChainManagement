import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import type { OrderRatingRequestDto, OrderRatingResponseDto, OrderListItemDto } from "../../lib/types/ClientDtos.ts";
import * as clientApi from "../../lib/api/clientService.ts";

export function useRateOrder() {
    const queryClient = useQueryClient();

    return useMutation<
        OrderRatingResponseDto,
        unknown,
        { orderId: number; rating: OrderRatingRequestDto }
    >({
        mutationFn: ({ orderId, rating }) => clientApi.rateOrder(orderId, rating),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["clientOrdersAvailableForRating"] });
            queryClient.invalidateQueries({ queryKey: ["clientStatistics"] });
            queryClient.invalidateQueries({ queryKey: ["clientOrders"] });
        },
    });
}

export function useOrdersAvailableForRating() {
    return useQuery<OrderListItemDto[]>({
        queryKey: ["clientOrdersAvailableForRating"],
        queryFn: clientApi.fetchOrdersAvailableForRating,
    });
}