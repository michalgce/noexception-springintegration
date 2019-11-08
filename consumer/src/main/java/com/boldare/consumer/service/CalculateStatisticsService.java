package com.boldare.consumer.service;

import com.boldare.dto.MetricDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CalculateStatisticsService {

    public void calculateStatistics(final List<MetricDTO> metrics) {
        double average = metrics.stream()
                .mapToDouble(MetricDTO::getValue)
                .average().getAsDouble();

        log.info("Average value is {}", average);
    }

}
