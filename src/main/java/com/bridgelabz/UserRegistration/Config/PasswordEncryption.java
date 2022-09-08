package com.bridgelabz.UserRegistration.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEncryption {
    @Bean
    public BCryptPasswordEncoder getPasswordEncoded(){return new BCryptPasswordEncoder();}
}