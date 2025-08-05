import API from "./axios.ts";
import {UserRequestDto, UserResponseDto, VehicleDto} from "../types/AdminDtos.ts";

//WORKERS

export const fetchWorkers = () => API.get<UserResponseDto[]>('/admin/workers').then(res => res.data);

export const addWorker = (worker: UserRequestDto) => API.post<UserResponseDto>('/admin/workers', worker).then(res => res.data);

export const editWorker = (id: number, worker: UserRequestDto) => API.put<UserResponseDto>(`/admin/workers/${id}`, worker).then(res => res.data);

export const deleteWorker = (id: number) => API.delete(`/admin/workers/${id}`);

// VEHICLES

export const fetchVehicles = () => API.get<VehicleDto[]>('/admin/vehicles').then(res => res.data);

export const addVehicle = (vehicle: VehicleDto) => API.post<VehicleDto>('/admin/vehicles', vehicle).then(res => res.data);

export const editVehicle = (id: number, vehicle: VehicleDto) => API.put<VehicleDto>(`/admin/vehicles/${id}`, vehicle).then(res => res.data);

export const deleteVehicle = (id: number) => API.delete(`/admin/vehicles/${id}`);