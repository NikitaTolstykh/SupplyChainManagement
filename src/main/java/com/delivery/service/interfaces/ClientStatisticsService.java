package com.delivery.service.interfaces;

import com.delivery.dto.ClientStatisticsDto;

public interface ClientStatisticsService {
    ClientStatisticsDto getClientStatistics(String clientEmail);
}
