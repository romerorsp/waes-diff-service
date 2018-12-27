package com.wearewaes.diff.rs.util;

import com.wearewaes.diff.rs.exception.DiffException;
import com.wearewaes.diff.rs.response.DiffResult;
import com.wearewaes.diff.rs.response.KeyValue;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.Diff;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@AllArgsConstructor
public class DiffTool {

  private final DiffMatchPatch diffMatchPatch;

  public DiffResult infer(final DiffResult diffResult) {
    if (StringUtils.isEmpty(diffResult.getLeftSource())) {
      throw new DiffException("Left side of the diff was never set!");
    }

    if (StringUtils.isEmpty(diffResult.getRightSource())) {
      throw new DiffException("Right side of the diff was never set!");
    }

    final String decodedLeft = decode(diffResult.getLeftSource());
    final String decodedRight = decode(diffResult.getRightSource());

    LinkedList<Diff> results = diffMatchPatch.diffMain(decodedLeft, decodedRight);
    List<KeyValue<String, String>> mappedResults = mapResults(results);

    return DiffResult.builder()
        .leftSource(decodedLeft)
        .rightSource(decodedRight)
        .result(mappedResults)
        .build();
  }

  private List<KeyValue<String, String>> mapResults(final List<Diff> results) {
    return results.stream()
        .map(diff -> KeyValue.create(diff.operation.toString(), diff.text))
        .collect(Collectors.toList());
  }

  private String decode(String source) {
    return new String(Base64.getDecoder().decode(source), Charset.forName("UTF-8"));
  }
}
