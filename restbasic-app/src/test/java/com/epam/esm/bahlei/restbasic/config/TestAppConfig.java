package com.epam.esm.bahlei.restbasic.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.epam.esm.bahlei.restbasic.dao")
@EntityScan("com.epam.esm.bahlei.restbasic.model")
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
public class TestAppConfig {}
