import {useMutation, useQueryClient} from "@tanstack/react-query";
import type {OrderRatingRequestDto, OrderRatingResponseDto} from "../../lib/types/ClientDtos.ts";
import * as clientApi from "../../lib/api/clientService.ts";

export function useRateOrder() {
    const queryClient = useQueryClient();

    return useMutation<
        OrderRatingResponseDto,
        unknown,
        { orderId: number; rating: OrderRatingRequestDto }
    >(
        ({orderId, rating}) => clientApi.rateOrder(orderId, rating),
        {
            onSuccess: () => {
                queryClient.invalidateQueries(["clientOrdersAvailableForRating"]);
                queryClient.invalidateQueries(["clientStatistics"]);
            },
        }
    );
}

export function useOrdersAvailableForRating() {
    return useQuery(
        ["clientOrdersAvailableForRating"],
        clientApi.fetchOrdersAvailableForRating
    );
}
