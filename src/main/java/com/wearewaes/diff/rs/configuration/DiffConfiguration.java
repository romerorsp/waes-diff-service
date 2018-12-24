package com.wearewaes.diff.rs.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiffConfiguration {

  @Bean
  public ModelMapper createModelMapper() {
    return new ModelMapper();
  }
}
