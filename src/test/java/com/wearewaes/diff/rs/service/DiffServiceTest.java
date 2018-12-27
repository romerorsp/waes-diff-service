package com.wearewaes.diff.rs.service;

import com.wearewaes.diff.rs.model.DiffEntry;
import com.wearewaes.diff.rs.request.DiffRequest;
import com.wearewaes.diff.rs.response.DiffResult;
import com.wearewaes.diff.rs.transform.DiffDirection;
import com.wearewaes.diff.rs.transform.DiffTransformer;
import com.wearewaes.diff.rs.util.DiffTool;
import java.util.Base64;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;

@RunWith(MockitoJUnitRunner.class)
public class DiffServiceTest {

  @InjectMocks
  private DiffService fixture;

  @Mock
  private MongoTemplate template;

  @Mock
  private DiffTool diffTool;

  @Mock
  private DiffTransformer transformer;

  @Test
  public void shouldCreateDiff() {
    final String uuid = UUID.randomUUID().toString();
    final DiffRequest request = new DiffRequest();
    request.setValue(Base64.getEncoder().encodeToString("value".getBytes()));

    Mockito.when(template.save(Mockito.any(DiffEntry.class)))
        .thenReturn(Mockito.mock(DiffEntry.class));
    Mockito.when(transformer.fromRequest(Mockito.anyString(), Mockito.any(), Mockito.any()))
        .thenReturn(Mockito.mock(DiffEntry.class));

    final boolean result =
        fixture.createOrUpdateDiff(uuid, request, DiffDirection.LEFT);

    Assert.assertTrue(result);
  }
}
