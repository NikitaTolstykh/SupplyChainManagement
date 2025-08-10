export const useAvailableDrivers = () => {
    return useQuery({
        queryKey: ['available-drivers'],
        queryFn: dispatcherApi.fetchAvailableDrivers,
        refetchInterval: 60000,
    });
};