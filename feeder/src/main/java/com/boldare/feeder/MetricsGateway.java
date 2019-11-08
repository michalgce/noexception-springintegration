package com.boldare.feeder;

import com.boldare.feeder.dto.MetricDTO;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface MetricsGateway {

    @Gateway(requestChannel = "ampqChannel")
    void publish(MetricDTO metricDTO);
}
