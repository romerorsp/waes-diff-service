package com.wearewaes.diff.rs.util;

import com.wearewaes.diff.rs.exception.DiffException;
import com.wearewaes.diff.rs.response.DiffResult;
import com.wearewaes.diff.rs.response.KeyValue;
import java.util.Base64;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DiffToolTest {

  private DiffTool fixture = new DiffTool(new DiffMatchPatch());

  @Test
  public void shouldReturnASetOfDifferencesBetweenTexts() {
    final DiffResult diffResult = DiffResult.builder()
        .leftSource(Base64.getEncoder().encodeToString("Hello world!".getBytes()))
        .rightSource(Base64.getEncoder().encodeToString("Goodbye world!".getBytes()))
        .build();
    final DiffResult finalResult = fixture.infer(diffResult);

    Assert.assertEquals("Hello world!", finalResult.getLeftSource());
    Assert.assertEquals("Goodbye world!", finalResult.getRightSource());

    Assert.assertNotNull(finalResult.getResult());
    Assert.assertEquals(5, finalResult.getResult().size());

    KeyValue<String, String> kv = finalResult.getResult().get(0);
    Assert.assertEquals("DELETE", kv.getKey());
    Assert.assertEquals("Hell", kv.getValue());

    kv = finalResult.getResult().get(1);
    Assert.assertEquals("INSERT", kv.getKey());
    Assert.assertEquals("G", kv.getValue());

    kv = finalResult.getResult().get(2);
    Assert.assertEquals("EQUAL", kv.getKey());
    Assert.assertEquals("o", kv.getValue());

    kv = finalResult.getResult().get(3);
    Assert.assertEquals("INSERT", kv.getKey());
    Assert.assertEquals("odbye", kv.getValue());

    kv = finalResult.getResult().get(4);
    Assert.assertEquals("EQUAL", kv.getKey());
    Assert.assertEquals(" world!", kv.getValue());
  }

  @Test(expected = DiffException.class)
  public void shouldThrowDiffExceptionWhenRightSourceNotFilled() {
    final DiffResult diffResult = DiffResult.builder()
        .leftSource(Base64.getEncoder().encodeToString("Hello world!".getBytes()))
        .build();
    fixture.infer(diffResult);
  }

  @Test(expected = DiffException.class)
  public void shouldThrowDiffExceptionWhenLeftSourceNotFilled() {
    final DiffResult diffResult = DiffResult.builder()
        .rightSource(Base64.getEncoder().encodeToString("Goodbye world!".getBytes()))
        .build();
    fixture.infer(diffResult);
  }

}
