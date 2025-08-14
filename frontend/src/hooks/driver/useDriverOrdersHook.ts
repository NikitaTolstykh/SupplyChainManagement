import {useQuery} from "@tanstack/react-query";
import * as driverApi from "../../lib/api/driverAPI";

export const useAssignedOrders = () => {
    return useQuery({
        queryKey: ['driver-assigned-orders'],
        queryFn: driverApi.fetchAssignedOrders,
        refetchInterval: 30000,
    });
};

export const useCompletedOrders = () => {
    return useQuery({
        queryKey: ['driver-completed-orders'],
        queryFn: driverApi.fetchCompletedOrders,
        refetchInterval: 60000,
    });
};

export const useOrderDetails = (id: number) => {
    return useQuery({
        queryKey: ['driver-order-details', id],
        queryFn: () => driverApi.fetchOrderDetails(id),
        enabled: !!id,
    });
};