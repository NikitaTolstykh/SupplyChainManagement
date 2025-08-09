import {useMutation, useQueryClient} from "@tanstack/react-query";
import {useNavigate} from "react-router-dom";
import type {OrderRequestDto, OrderDetailsDto} from "../../lib/types/ClientDtos.ts";
import * as clientApi from "../../lib/api/clientService.ts";

export function useCreateOrder() {
    const queryClient = useQueryClient();
    const navigate = useNavigate();

    return useMutation<OrderDetailsDto, unknown, OrderRequestDto>({
        mutationFn: (orderRequest) => clientApi.createOrder(orderRequest),
        onSuccess: (data) => {
            queryClient.invalidateQueries({queryKey: ["clientOrders"]});
            queryClient.invalidateQueries({queryKey: ["clientStatistics"]});


            navigate(`/client/orders/${data.id}`);
        },
        onError: (error) => {
            console.error("Failed to create order:", error);
        },
    });
}