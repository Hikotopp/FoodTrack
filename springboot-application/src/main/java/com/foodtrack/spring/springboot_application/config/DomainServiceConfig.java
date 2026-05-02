package com.foodtrack.spring.springboot_application.config;

import com.foodtrack.spring.springboot_application.domain.service.OrderCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceConfig {

    @Bean
    OrderCalculator orderCalculator() {
        return new OrderCalculator();
    }
}
