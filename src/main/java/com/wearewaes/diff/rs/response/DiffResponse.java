package com.wearewaes.diff.rs.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_EMPTY)
public class DiffResponse<T> {

  private boolean success;

  private List<Error> errors;

  private T payload;

}
