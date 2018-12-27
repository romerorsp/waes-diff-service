package com.wearewaes.diff.rs.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiffResult {

  private String rightSource;

  private String leftSource;

  @Default
  private List<KeyValue<String, String>> result = Collections.emptyList();


  @JsonProperty("equals")
  public boolean isEqual() {
    return result != null &&
        (result.isEmpty() || result.stream().map(KeyValue::getKey)
            .allMatch(value -> value.startsWith("EQUAL")));
  }
}
