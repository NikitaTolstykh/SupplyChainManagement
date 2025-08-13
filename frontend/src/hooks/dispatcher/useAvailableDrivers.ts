import { useQuery } from '@tanstack/react-query';
import * as dispatcherApi from "../../lib/api/dispatcherAPI.ts";

export const useAvailableDrivers = () => {
    return useQuery({
        queryKey: ['available-drivers'],
        queryFn: dispatcherApi.fetchAvailableDrivers,
        refetchInterval: 60000,
    });
};