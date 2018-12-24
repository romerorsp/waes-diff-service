package com.wearewaes.diff.rs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class DiffApplication {

  public static void main(String[] args) {
    SpringApplication.run(DiffApplication.class, args);
  }
}
