import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import * as api from '../../lib/api/admin';
import type { UserRequestDto, UserResponseDto } from '../../lib/types/AdminDtos';

export const useWorkers = () => {
    const queryClient = useQueryClient();

    const workersQuery = useQuery<UserResponseDto[], Error>({
        queryKey: ['workers'],
        queryFn: api.fetchWorkers,
    });

    const addWorkerMutation = useMutation<UserResponseDto, Error, UserRequestDto>({
        mutationFn: api.addWorker,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['workers'] });
        },
    });

    const editWorkerMutation = useMutation<UserResponseDto, Error, { id: number; data: UserRequestDto }>({
        mutationFn: ({ id, data }) => api.editWorker(id, data),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['workers'] });
        },
    });

    const deleteWorkerMutation = useMutation<void, Error, number>({
        mutationFn: api.deleteWorker,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['workers'] });
        },
    });

    const fetchByRole = (role: string) =>
        useQuery<UserResponseDto[], Error>({
            queryKey: ['workers', 'role', role],
            queryFn: () => api.fetchWorkersByRole(role),
            enabled: !!role,
        });

    const searchByQuery = (query: string) =>
        useQuery<UserResponseDto[], Error>({
            queryKey: ['workers', 'search', query],
            queryFn: () => api.searchWorkers(query),
            enabled: query.length > 0,
        });

    return {
        workersQuery,
        addWorkerMutation,
        editWorkerMutation,
        deleteWorkerMutation,
        fetchByRole,
        searchByQuery,
    };
};