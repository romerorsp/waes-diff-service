package com.wearewaes.diff.rs.transform;

import com.wearewaes.diff.rs.exception.TransformationException;
import com.wearewaes.diff.rs.model.Content;
import com.wearewaes.diff.rs.model.DiffEntry;
import com.wearewaes.diff.rs.request.DiffRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DiffTransformer {

  private final ModelMapper modelMapper;

  public DiffEntry fromRequest(final DiffRequest request, final DiffDirection diffContentType) {
    final Content content = modelMapper.map(request, Content.class);
    final DiffEntry diffEntry = new DiffEntry();
    if (diffContentType == null) {
      throw new TransformationException("DiffContentType can't be null");
    }
    diffContentType.setContent(diffEntry, content);
    return diffEntry;
  }
}