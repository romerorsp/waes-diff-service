package com.wearewaes.diff.rs.controller;

import com.wearewaes.diff.rs.properties.EnvironmentProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class GetEnvironmentController {


  private final EnvironmentProperties environmentProperties;

  public GetEnvironmentController(
      final EnvironmentProperties environmentProperties) {
    this.environmentProperties = environmentProperties;
  }

  @RequestMapping("/environment")
  public ResponseEntity<EnvironmentProperties> getEnvironment() {
    return ResponseEntity.ok(environmentProperties);
  }
}