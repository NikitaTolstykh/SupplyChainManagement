import {useQuery} from "@tanstack/react-query";
import * as dispatcherApi from "../../lib/api/dispatcherAPI.ts";

export const useDispatcherOrders = () => {
    return useQuery({
        queryKey: ['dispatcher-orders'],
        queryFn: dispatcherApi.fetchAllOrders,
        refetchInterval: 30000,
    });
};

export const useOrderDetails = (id: number) => {
    return useQuery({
        queryKey: ['order-details', id],
        queryFn: () => dispatcherApi.fetchOrderDetails(id),
        enabled: !!id,
    });
};

export const useOrderStatusHistory = (id: number) => {
    return useQuery({
        queryKey: ['order-status-history', id],
        queryFn: () => dispatcherApi.fetchOrderStatusHistory(id),
        enabled: !!id,
    });
};

export const useOrderRatings = () => {
    return useQuery({
        queryKey: ['order-ratings'],
        queryFn: dispatcherApi.fetchOrderRatings,
    });
};