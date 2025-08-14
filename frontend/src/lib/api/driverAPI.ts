import API from "./axios.ts";
import type {DriverOrderListItemDto} from "../types/DriverOrderListItemDto.ts";
import type {DispatcherOrderDetailsDto} from "../types/DispatcherDtos.ts";


// ORDERS
export const fetchAssignedOrders = (): Promise<DriverOrderListItemDto[]> =>
    API.get<DriverOrderListItemDto[]>('/driver/orders/assigned').then(res => res.data);

export const fetchCompletedOrders = (): Promise<DriverOrderListItemDto[]> =>
    API.get<DriverOrderListItemDto[]>('/driver/orders/completed').then(res => res.data);

export const fetchOrderDetails = (id: number): Promise<DispatcherOrderDetailsDto> =>
    API.get<DispatcherOrderDetailsDto>(`/driver/orders/${id}`).then(res => res.data);

// ACTIONS
export const acceptOrder = (id: number): Promise<void> =>
    API.post<void>(`/driver/orders/${id}/accept`).then(res => res.data);

export const completeOrder = (id: number): Promise<void> =>
    API.post<void>(`/driver/orders/${id}/complete`).then(res => res.data);