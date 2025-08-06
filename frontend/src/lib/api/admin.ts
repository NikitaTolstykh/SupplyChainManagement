import API from "./axios.ts";
import type { UserRequestDto, UserResponseDto, VehicleDto, VehicleFormDto } from "../types/AdminDtos.ts";

// WORKERS

export const fetchWorkers = () => API.get<UserResponseDto[]>('/admin/workers').then(res => res.data);

export const addWorker = (worker: UserRequestDto) =>
    API.post<UserResponseDto>('/admin/workers', worker).then(res => res.data);

export const editWorker = (id: number, worker: UserRequestDto) =>
    API.put<UserResponseDto>(`/admin/workers/${id}`, worker).then(res => res.data);

export const deleteWorker = (id: number): Promise<void> =>
    API.delete(`/admin/workers/${id}`).then(() => {});

export const fetchWorkersByRole = (role: string) =>
    API.get<UserResponseDto[]>(`/admin/workers/role/${role}`).then(res => res.data);

export const searchWorkers = (query: string) =>
    API.get<UserResponseDto[]>(`/admin/workers/search?query=${encodeURIComponent(query)}`).then(res => res.data);

// VEHICLES

export const fetchVehicles = () =>
    API.get<VehicleDto[]>('/admin/vehicles').then(res => res.data);

export const addVehicle = (vehicle: VehicleFormDto) =>
    API.post<VehicleDto>('/admin/vehicles', vehicle).then(res => res.data);

export const editVehicle = (id: number, vehicle: VehicleFormDto) =>
    API.put<VehicleDto>(`/admin/vehicles/${id}`, vehicle).then(res => res.data);

export const deleteVehicle = (id: number): Promise<void> =>
    API.delete(`/admin/vehicles/${id}`).then(() => {});
