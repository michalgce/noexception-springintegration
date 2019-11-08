package com.boldare.consumer.service;

import com.boldare.dto.MetricDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MetricTransformer {

    public MetricDTO transform(final MetricDTO metricDTO) {
        log.info("Received KW and transforming to KM. Recevied: {}", metricDTO);
        MetricDTO transformedMetric = new MetricDTO(metricDTO.getType(), "KM", metricDTO.getValue() * 10);
        log.info("Transformed: {}", transformedMetric);

        return transformedMetric;
    }
}
