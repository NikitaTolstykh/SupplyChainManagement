package com.delivery.service;

import com.delivery.dto.ClientStatisticsDto;

public interface ClientStatisticsService {
    ClientStatisticsDto getClientStatistics(String clientEmail);
}
