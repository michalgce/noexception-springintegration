package com.boldare.feeder;

import com.boldare.feeder.dto.MetricDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RandomMetricGenerator {

    private final MetricsGateway metricsGateway;

    @Scheduled(fixedDelay = 2000)
    public void generateRandomMetric() {
        double v = Math.random() * 3 + 1;
        MetricDTO metric;
        if (v >= 2) {
            metric = new MetricDTO("NORMAL", Math.random() * 3 + 1 >  2 ? "KM" : "KW", Math.random() * 150 + 1);
        } else {
            metric = new MetricDTO("ERROR", "", 0.0);
        }

        metricsGateway.publish(metric);
    }

}
