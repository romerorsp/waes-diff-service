package com.wearewaes.diff.rs.transform;

import com.wearewaes.diff.rs.model.Content;
import com.wearewaes.diff.rs.model.DiffEntry;
import com.wearewaes.diff.rs.request.DiffRequest;
import com.wearewaes.diff.rs.response.DiffResult;
import java.util.Base64;
import java.util.Collections;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

@RunWith(MockitoJUnitRunner.class)
public class DiffTransformerTest {

  private DiffTransformer fixture = new DiffTransformer(new ModelMapper());

  @Test
  public void shouldParseFromRequest() {
    final String uuid = UUID.randomUUID().toString();
    final DiffRequest request = new DiffRequest();
    request.setValue(Base64.getEncoder().encodeToString("value".getBytes()));
    final DiffEntry result = fixture
        .fromRequest(uuid, request, DiffDirection.RIGHT);

    Assert.assertEquals(uuid, result.getId());
    Assert.assertEquals(Base64.getEncoder().encodeToString("value".getBytes()),
        result.getRight().getValue());
    Assert.assertNull(result.getLeft());
  }

  @Test
  public void shouldParseFromEntry() {
    final DiffEntry entry = new DiffEntry();
    entry.setId(UUID.randomUUID().toString());
    entry.setLeft(new Content("VALUE"));
    final DiffResult result = fixture.fromEntry(entry);

    Assert.assertEquals("VALUE", result.getLeftSource());
    Assert.assertNull(result.getRightSource());
    Assert.assertEquals(Collections.emptyList(), result.getResult());
  }
}
