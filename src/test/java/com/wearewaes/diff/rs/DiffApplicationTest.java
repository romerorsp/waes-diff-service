package com.wearewaes.diff.rs;

import com.wearewaes.diff.rs.matcher.KeyValueCollectionComparator;
import com.wearewaes.diff.rs.model.DiffEntry;
import com.wearewaes.diff.rs.properties.EnvironmentProperties;
import com.wearewaes.diff.rs.request.DiffRequest;
import com.wearewaes.diff.rs.response.DiffResponse;
import com.wearewaes.diff.rs.response.DiffResult;
import com.wearewaes.diff.rs.response.KeyValue;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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

  @Autowired
  private MongoTemplate template;

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

  @Test
  public void shouldSetDiffRight() {
    trySettingDiffRight(UUID.randomUUID().toString());
  }

  @Test
  public void shouldReturnErrorDueToNonBase64CodedText() {
    final String uuid = UUID.randomUUID().toString();
    final DiffRequest request = new DiffRequest();

    final String textToBeDiff =
        "I hope this text will never change\n"
            + "But if eventually it does\n"
            + "I'd like to see the changes through the /diff/{uuid} endpoint";

    request.setValue(textToBeDiff);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<DiffRequest> httpEntity = new HttpEntity<>(request, headers);
    final URI uri = URI.create(String.format("http://localhost:%d/v1/diff/%s/right", port, uuid));
    DiffResponse<Void> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity,
        new ParameterizedTypeReference<DiffResponse<Void>>() {
        }).getBody();

    Assert.assertNotNull(response);
    Assert.assertFalse(response.isSuccess());
    Assert.assertNotNull(response.getErrors());
    Assert.assertFalse(response.getErrors().isEmpty());
    Assert.assertEquals("Sources content needs to be base64 encoded.",
        response.getErrors().get(0).getMessage());
  }

  @Test
  public void shouldReturnErrorDueToEmptyText() {
    final String uuid = UUID.randomUUID().toString();
    final DiffRequest request = new DiffRequest();

    final String textToBeDiff = "";

    request.setValue(textToBeDiff);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<DiffRequest> httpEntity = new HttpEntity<>(request, headers);
    final URI uri = URI.create(String.format("http://localhost:%d/v1/diff/%s/right", port, uuid));
    DiffResponse<Void> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity,
        new ParameterizedTypeReference<DiffResponse<Void>>() {
        }).getBody();

    Assert.assertNotNull(response);
    Assert.assertFalse(response.isSuccess());
    Assert.assertNotNull(response.getErrors());
    Assert.assertFalse(response.getErrors().isEmpty());
    Assert.assertEquals("Source value of the diff cant be empty.",
        response.getErrors().get(0).getMessage());
  }

  @Test
  public void shouldSetDiffLeft() {
    trySettingDiffLeft(UUID.randomUUID().toString());
  }

  @Test
  public void shouldReturnAValidDiffPayloadWhenRightAndLeftSourcesSet() {
    final String uuid = UUID.randomUUID().toString();

    trySettingDiffRight(uuid);
    trySettingDiffLeft(uuid);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    final URI uri = URI.create(String.format("http://localhost:%d/v1/diff/%s", port, uuid));
    DiffResponse<DiffResult> response =
        restTemplate.exchange(uri, HttpMethod.GET, null,
            new ParameterizedTypeReference<DiffResponse<DiffResult>>() {
            }).getBody();

    Assert.assertNotNull(response);
    Assert.assertTrue(response.isSuccess());
    Assert.assertNull(response.getErrors());
    Assert.assertNotNull(response.getPayload());
    final DiffResult diffResult = response.getPayload();

    Assert.assertEquals("I hope this text will never change\n"
            + "But if eventually it does\n"
            + "I'd like to see the changes through the /diff/{uuid} endpoint",
        diffResult.getRightSource());
    Assert.assertEquals("I hope this text will always change\n"
            + "But if eventually it doesn't\n"
            + "I'd like to see a flag telling me the files are equal through the /diff/{uuid} endpoint.",
        diffResult.getLeftSource());

    final List<KeyValue<String, String>> expectedMapping = Arrays.asList(
        KeyValue.create("EQUAL", "I hope this text will "),
        KeyValue.create("DELETE", "always"),
        KeyValue.create("INSERT", "never"),
        KeyValue.create("EQUAL", " change\nBut if eventually it does"),
        KeyValue.create("DELETE", "n't"),
        KeyValue.create("EQUAL", "\nI'd like to see "),
        KeyValue.create("DELETE", "a flag "),
        KeyValue.create("EQUAL", "t"),
        KeyValue.create("INSERT", "h"),
        KeyValue.create("EQUAL", "e"),
        KeyValue.create("DELETE", "lli"),
        KeyValue.create("INSERT", " cha"),
        KeyValue.create("EQUAL", "ng"),
        KeyValue.create("DELETE", " me the fil"),
        KeyValue.create("EQUAL", "es"),
        KeyValue.create("DELETE", " are equal"),
        KeyValue.create("EQUAL", " through the /diff/{uuid} endpoint"),
        KeyValue.create("DELETE", ".")
    );

    Assert.assertThat(diffResult.getResult(), Matchers.is(KeyValueCollectionComparator.taking(expectedMapping)));
  }

  private void trySettingDiffRight(final String uuid) {
    final DiffRequest request = new DiffRequest();

    final String textToBeDiff =
        "I hope this text will never change\n"
            + "But if eventually it does\n"
            + "I'd like to see the changes through the /diff/{uuid} endpoint";

    final String base64TextToBeDiff = Base64.getEncoder()
        .encodeToString(textToBeDiff.getBytes(Charset.forName("UTF-8")));

    request.setValue(base64TextToBeDiff);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<DiffRequest> httpEntity = new HttpEntity<>(request, headers);
    final URI uri = URI.create(String.format("http://localhost:%d/v1/diff/%s/right", port, uuid));
    DiffResponse<Void> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity,
        new ParameterizedTypeReference<DiffResponse<Void>>() {
        }).getBody();

    Assert.assertNotNull(response);
    Assert.assertTrue(response.isSuccess());

    final Optional<DiffEntry> diffResult = Optional
        .ofNullable(template.findById(uuid, DiffEntry.class));
    Assert.assertTrue(diffResult.isPresent());
    Assert.assertNotNull(diffResult.get().getRight());
    Assert.assertEquals(base64TextToBeDiff, diffResult.get().getRight().getValue());
  }

  private void trySettingDiffLeft(final String uuid) {
    final DiffRequest request = new DiffRequest();

    final String textToBeDiff =
        "I hope this text will always change\n"
            + "But if eventually it doesn't\n"
            + "I'd like to see a flag telling "
            + "me the files are equal through the /diff/{uuid} endpoint.";

    final String base64TextToBeDiff = Base64.getEncoder()
        .encodeToString(textToBeDiff.getBytes(Charset.forName("UTF-8")));

    request.setValue(base64TextToBeDiff);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<DiffRequest> httpEntity = new HttpEntity<>(request, headers);
    final URI uri = URI.create(String.format("http://localhost:%d/v1/diff/%s/left", port, uuid));
    DiffResponse<Void> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity,
        new ParameterizedTypeReference<DiffResponse<Void>>() {
        }).getBody();

    Assert.assertNotNull(response);
    Assert.assertTrue(response.isSuccess());

    final Optional<DiffEntry> diffResult = Optional
        .ofNullable(template.findById(uuid, DiffEntry.class));
    Assert.assertTrue(diffResult.isPresent());
    Assert.assertNotNull(diffResult.get().getLeft());
    Assert.assertEquals(base64TextToBeDiff, diffResult.get().getLeft().getValue());
  }
}
