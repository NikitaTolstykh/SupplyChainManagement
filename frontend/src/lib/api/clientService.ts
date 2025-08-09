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
export const fetchOrders = (): Promise<OrderListItemDto[]> =>
    API.get<OrderListItemDto[]>('/client/orders').then(res => res.data);

export const fetchOrderDetails = (id: number): Promise<OrderDetailsDto> =>
    API.get<OrderDetailsDto>(`/client/orders/${id}`).then(res => res.data);

export const fetchOrderStatusHistory = (id: number): Promise<OrderStatusHistoryDto[]> =>
    API.get<OrderStatusHistoryDto[]>(`/client/orders/${id}/status-history`).then(res => res.data);

export const createOrder = (order: OrderRequestDto): Promise<OrderDetailsDto> =>
    API.post<OrderDetailsDto>('/client/orders', order).then(res => res.data);

// RATINGS
export const rateOrder = (orderId: number, rating: OrderRatingRequestDto): Promise<OrderRatingResponseDto> =>
    API.post<OrderRatingResponseDto>(`/client/orders/${orderId}/rating`, rating).then(res => res.data);

export const fetchOrdersAvailableForRating = (): Promise<OrderListItemDto[]> =>
    API.get<OrderListItemDto[]>('/client/orders/available-for-rating').then(res => res.data);

// STATISTICS
export const fetchClientStatistics = (): Promise<ClientStatisticsDto> =>
    API.get<ClientStatisticsDto>('/client/orders/statistics').then(res => res.data);
