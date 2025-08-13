import { useMutation, useQueryClient } from '@tanstack/react-query';
import * as dispatcherApi from "../../lib/api/dispatcherAPI.ts";
import type { AssignDriverRequestDto, UpdateOrderStatusRequestDto} from "../../lib/types/DispatcherDtos.ts";
import type {OrderRequestDto} from "../../lib/types/ClientDtos.ts";

export const useAssignDriver = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: ({ orderId, data }: { orderId: number; data: AssignDriverRequestDto }) =>
            dispatcherApi.assignDriver(orderId, data),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['dispatcher-orders'] });
            queryClient.invalidateQueries({ queryKey: ['order-details'] });
            queryClient.invalidateQueries({ queryKey: ['available-drivers'] });
        },
    });
};

export const useUpdateOrderStatus = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: ({ orderId, data }: { orderId: number; data: UpdateOrderStatusRequestDto }) =>
            dispatcherApi.updateOrderStatus(orderId, data),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['dispatcher-orders'] });
            queryClient.invalidateQueries({ queryKey: ['order-details'] });
        },
    });
};

export const useUpdateOrderInfo = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: ({ orderId, data }: { orderId: number; data: OrderRequestDto }) =>
            dispatcherApi.updateOrderInfo(orderId, data),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['dispatcher-orders'] });
            queryClient.invalidateQueries({ queryKey: ['order-details'] });
        },
    });
};

export const useCancelOrder = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (orderId: number) => dispatcherApi.cancelOrder(orderId),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['dispatcher-orders'] });
            queryClient.invalidateQueries({ queryKey: ['order-details'] });
        },
    });
};