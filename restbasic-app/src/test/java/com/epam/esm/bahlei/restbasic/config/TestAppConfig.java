package com.epam.esm.bahlei.restbasic.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm.bahlei.restbasic.dao")
@EntityScan("com.epam.esm.bahlei.restbasic")
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
public class TestAppConfig {
  @Bean
  public EmailValidator emailValidator() {
    return EmailValidator.getInstance();
  }

  @Bean
  public DataSource dataSource() {
    HikariConfig config = new HikariConfig("/rest_test.properties");

    return new HikariDataSource(config);
  }
}
