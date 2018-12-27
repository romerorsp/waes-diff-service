package com.wearewaes.diff.rs.transform;

import com.wearewaes.diff.rs.exception.TransformationException;
import com.wearewaes.diff.rs.model.Content;
import com.wearewaes.diff.rs.model.DiffEntry;
import com.wearewaes.diff.rs.request.DiffRequest;
import com.wearewaes.diff.rs.response.DiffResult;
import com.wearewaes.diff.rs.response.DiffResult.DiffResultBuilder;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DiffTransformer {

  private final ModelMapper modelMapper;

  public DiffEntry fromRequest(
      final String id,
      final DiffRequest request,
      final DiffDirection diffDirection) {
    final Content content = modelMapper.map(request, Content.class);
    final DiffEntry diffEntry = new DiffEntry();
    if (diffDirection == null) {
      throw new TransformationException("DiffContentType can't be null");
    }
    diffDirection.setContent(diffEntry, content);
    diffEntry.setId(id);
    return diffEntry;
  }

  public DiffResult fromEntry(final DiffEntry entry) {
    DiffResultBuilder diffResultBuilder = DiffResult.builder();
    if (!(entry.getLeft() == null)) {
      diffResultBuilder = diffResultBuilder.leftSource(entry.getLeft().getValue());
    }
    if (!(entry.getRight() == null)) {
      diffResultBuilder = diffResultBuilder.rightSource(entry.getRight().getValue());
    }
    return diffResultBuilder.build();
  }
}