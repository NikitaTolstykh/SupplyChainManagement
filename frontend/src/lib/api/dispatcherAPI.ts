import API from "./axios.ts";

import type {
    OrderListItemDto,
    DispatcherOrderDetailsDto,
    OrderStatusHistoryDto,
    AvailableDriverDto,
    OrderRatingResponseDto,
    AssignDriverRequestDto,
    UpdateOrderStatusRequestDto,
    OrderRequestDto
} from "../types/DispatcherDtos";

// ORDERS
export const fetchAllOrders = (): Promise<OrderListItemDto[]> =>
    API.get<OrderListItemDto[]>('/dispatcher/orders').then(res => res.data);

export const fetchOrderDetails = (id: number): Promise<DispatcherOrderDetailsDto> =>
    API.get<DispatcherOrderDetailsDto>(`/dispatcher/orders/${id}`).then(res => res.data);

export const fetchOrderStatusHistory = (id: number): Promise<OrderStatusHistoryDto[]> =>
    API.get<OrderStatusHistoryDto[]>(`/dispatcher/orders/${id}/status-history`).then(res => res.data);

// DRIVERS
export const fetchAvailableDrivers = (): Promise<AvailableDriverDto[]> =>
    API.get<AvailableDriverDto[]>('/dispatcher/available-drivers').then(res => res.data);

// RATINGS
export const fetchOrderRatings = (): Promise<OrderRatingResponseDto[]> =>
    API.get<OrderRatingResponseDto[]>('/dispatcher/orders/ratings').then(res => res.data);

// ACTIONS
export const assignDriver = (orderId: number, data: AssignDriverRequestDto): Promise<void> =>
    API.post<void>(`/dispatcher/orders/${orderId}/assign-driver`, data).then(res => res.data);

export const updateOrderStatus = (orderId: number, data: UpdateOrderStatusRequestDto): Promise<void> =>
    API.patch<void>(`/dispatcher/orders/${orderId}/status`, data).then(res => res.data);

export const updateOrderInfo = (orderId: number, data: OrderRequestDto): Promise<void> =>
    API.put<void>(`/dispatcher/orders/${orderId}`, data).then(res => res.data);

export const cancelOrder = (orderId: number): Promise<void> =>
    API.patch<void>(`/dispatcher/orders/${orderId}/cancel-order`).then(res => res.data);