import {useMutation, useQueryClient} from '@tanstack/react-query';
import * as driverApi from "../../lib/api/driverAPI.ts";

export const useAcceptOrder = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (orderId: number) => driverApi.acceptOrder(orderId),
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ['driver-assigned-orders']});
            queryClient.invalidateQueries({queryKey: ['driver-order-details']});
        },
    });
};

export const useCompleteOrder = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (orderId: number) => driverApi.completeOrder(orderId),
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ['driver-assigned-orders']});
            queryClient.invalidateQueries({queryKey: ['driver-completed-orders']});
            queryClient.invalidateQueries({queryKey: ['driver-order-details']});
        },
    });
};