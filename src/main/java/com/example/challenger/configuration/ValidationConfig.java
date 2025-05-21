package com.example.challenger.configuration;

import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class ValidationConfig implements WebFluxConfigurer {

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }
}
