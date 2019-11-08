package com.boldare.consumer;

import com.boldare.dto.MetricDTO;
import com.boldare.consumer.service.CalculateStatisticsService;
import com.boldare.consumer.service.MetricTransformer;
import com.boldare.consumer.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.Message;

@Slf4j
@Configuration
@EnableIntegration
public class FlowConfiguration {

    @Bean
    PublishSubscribeChannel normalMetricChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    PublishSubscribeChannel collectedMetersChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public IntegrationFlow noExceptionFlow(final ConnectionFactory connectionFactory) {
        return IntegrationFlows
                .from(Amqp.inboundAdapter(connectionFactory, "noexception"))
                //.log(LoggingHandler.Level.INFO, "Before enrich execution ", Message::getHeaders)
                .enrichHeaders(h -> h.headerExpression("type", "payload.type.toString()"))
                //.log(LoggingHandler.Level.INFO, "After enrich header execution ",message -> message)
                .route(Message.class, router -> router.getHeaders().get("type"), configure -> configure
                        .channelMapping("NORMAL", "normalMetricChannel")
                        .channelMapping("ERROR", "errorMetricChannel"))
                .get();
    }

    @Bean
    public IntegrationFlow errorFlow() {
        return IntegrationFlows.from("errorMetricChannel")
                .log(LoggingHandler.Level.ERROR, "Received error metric", Message::getPayload)
                .get();
    }

    @Bean
    IntegrationFlow normalMeterFlow() {
        return IntegrationFlows
                .from("normalMetricChannel")
                //.log(LoggingHandler.Level.INFO, "NORMAL METRIC", message -> message)
                .route(MetricDTO.class, MetricDTO::getUnit, configure ->
                        configure.channelMapping("KM", "kmMeterChannel")
                                 .channelMapping("KW", "kwMeterChannel"))
                .get();
    }

    @Bean
    public IntegrationFlow handleNormalMetric() {
        return IntegrationFlows.from("normalMetricChannel")
                .aggregate(aggregatorSpec -> aggregatorSpec.correlationStrategy(message -> ((MetricDTO)message.getPayload()).getType())
                .releaseStrategy(rs -> rs.size() > 9)
                .expireGroupsUponCompletion(true))
                .channel("collectedMetersChannel")
                .get();
    }

    @Bean
    @ServiceActivator(inputChannel = "collectedMetersChannel")
    public NotificationService handleMultipleMetrics(final NotificationService notificationService) {
        return notificationService;
    }

    @Bean
    @ServiceActivator(inputChannel = "collectedMetersChannel")
    public CalculateStatisticsService handleMultipleMetricsTwo(final CalculateStatisticsService calculateStatisticsService) {
        return calculateStatisticsService;
    }

    @Bean
    @Transformer(inputChannel = "kwMeterChannel", outputChannel = "kmMeterChannel")
    public MetricTransformer transformToKMFromKW(final MetricTransformer metricTransformer) {
        return metricTransformer;
    }

    @ServiceActivator(inputChannel = "kmMeterChannel")
    public void handleKMMeterChannel(final MetricDTO metricDTO) {
        log.info("Success! We got clear metric  {}", metricDTO);
    }

    /* TODO Implement missing saving ANY incoming metric from queue  */
}
