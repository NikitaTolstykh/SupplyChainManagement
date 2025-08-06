import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import * as api from '../../lib/api/admin';
import type { VehicleDto, VehicleFormDto } from '../../lib/types/AdminDtos';

export const useVehicles = () => {
    const queryClient = useQueryClient();

    const vehiclesQuery = useQuery<VehicleDto[], Error>({
        queryKey: ['vehicles'],
        queryFn: api.fetchVehicles,
    });

    const addVehicleMutation = useMutation<VehicleDto, Error, VehicleFormDto>({
        mutationFn: api.addVehicle,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['vehicles'] });
        },
    });

    const editVehicleMutation = useMutation<VehicleDto, Error, { id: number; data: VehicleFormDto }>({
        mutationFn: ({ id, data }) => api.editVehicle(id, data),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['vehicles'] });
        },
    });

    const deleteVehicleMutation = useMutation<void, Error, number>({
        mutationFn: api.deleteVehicle,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['vehicles'] });
        },
    });

    return {
        vehiclesQuery,
        addVehicleMutation,
        editVehicleMutation,
        deleteVehicleMutation,
    };
};
