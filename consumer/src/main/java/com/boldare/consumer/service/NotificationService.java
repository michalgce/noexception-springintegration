package com.boldare.consumer.service;

import com.boldare.dto.MetricDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NotificationService {
    public void notify(final List<MetricDTO> metrics) {
        log.info("Notifying about received last 10 metrics");
    }
}
