package com.wearewaes.diff.rs;

import com.wearewaes.diff.rs.properties.EnvironmentProperties;
import java.net.URI;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles(DiffApplicationTest.ACTIVE_PROFILE)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DiffApplicationTest {

  public static final String ACTIVE_PROFILE = "unit";
  @Value("${local.server.port}")
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void contextLoads() {
  }


  @Test
  public void shouldReturnUnitEnvironment() {
    final URI uri = URI.create(String.format("http://localhost:%d/environment", port));
    ResponseEntity<EnvironmentProperties> response = restTemplate
        .getForEntity(uri, EnvironmentProperties.class);

    Assert.assertNotNull(response);

    final EnvironmentProperties environment = response.getBody();

    Assert.assertEquals(ACTIVE_PROFILE, environment.getValue());
  }
}
