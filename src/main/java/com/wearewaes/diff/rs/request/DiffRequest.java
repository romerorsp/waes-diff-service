package com.wearewaes.diff.rs.request;

import com.wearewaes.diff.rs.validation.ValidDiffRequest;
import lombok.Data;

@Data
@ValidDiffRequest
public class DiffRequest {

  private String value;
}
