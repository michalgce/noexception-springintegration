package com.boldare.feeder;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import java.util.logging.Level;

@Configuration
@EnableIntegration
public class FeederIntegrationConfiguration {

    @Bean
    public IntegrationFlow feederFlowConfiguration(final RabbitTemplate rabbitTemplate) {
        return IntegrationFlows.from("ampqChannel")
                .handle(Amqp.outboundAdapter(rabbitTemplate).routingKey("noexception"))
                .get();
    }

    @Bean
    public IntegrationFlow errorChannelLogger() {
        return IntegrationFlows.from("errorChannel")
        .log(Level.ALL.getLocalizedName())
        .get();
    }
}
