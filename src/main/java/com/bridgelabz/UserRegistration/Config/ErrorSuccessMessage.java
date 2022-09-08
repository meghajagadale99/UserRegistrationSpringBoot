package com.bridgelabz.UserRegistration.Config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class ErrorSuccessMessage {
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/message/error-message", "classpath:/message/success-message");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}