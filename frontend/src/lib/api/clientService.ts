import API from "./axios.ts";

import type {
    OrderRequestDto,
    OrderListItemDto,
    OrderDetailsDto,
    OrderStatusHistoryDto,
    OrderRatingRequestDto,
    OrderRatingResponseDto,
    ClientStatisticsDto
} from "../types/ClientDtos.ts";

// ORDERS

export const fetchOrders = () =>
    API.get<OrderListItemDto>('/client/orders').then(res => res.data);

export const fetchOrderDetails = (id: number) =>
    API.get<OrderDetailsDto>(`/client/orders/${id}`).then(res => res.data);

export const fetchOrderStatusHistory = (id: number) =>
    API.get<OrderStatusHistoryDto[]>(`/client/orders/${id}/status-history`).then(res => res.data);

export const createOrder = (order: OrderRequestDto) =>
    API.post<OrderDetailsDto>('/client/orders', order).then(res => res.data);

// RATINGS

export const rateOrder = (orderId: number, rating: OrderRatingRequestDto) =>
    API.post<OrderRatingResponseDto>(`/client/orders/${orderId}/rating`, rating).then(res => res.data);

export const fetchOrdersAvailableForRating = () =>
    API.get<OrderListItemDto[]>('/client/orders/available-for-rating').then(res => res.data);

// STATISTICS

export const fetchClientStatistics = () =>
    API.get<ClientStatisticsDto>('/client/orders/statistics').then(res => res.data);
