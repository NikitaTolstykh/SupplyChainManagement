import {useMutation, useQueryClient} from "@tanstack/react-query";
import type {OrderRequestDto, OrderDetailsDto} from "../../lib/types/ClientDtos.ts";
import * as clientApi from "../../lib/api/clientService.ts";

export function useCreateOrder() {
    const queryClient = useQueryClient();

    return useMutation<OrderDetailsDto, unknown, OrderRequestDto>(
        (orderRequest) => clientApi.createOrder(orderRequest),
        {
            onSuccess: () => {
                queryClient.invalidateQueries(["clientOrders"]);
                queryClient.invalidateQueries(["clientStatistics"]);
            },
        }
    );
}