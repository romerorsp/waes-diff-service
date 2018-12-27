package com.wearewaes.diff.rs.service;

import com.wearewaes.diff.rs.model.Content;
import com.wearewaes.diff.rs.model.DiffEntry;
import com.wearewaes.diff.rs.request.DiffRequest;
import com.wearewaes.diff.rs.response.DiffResult;
import com.wearewaes.diff.rs.transform.DiffDirection;
import com.wearewaes.diff.rs.transform.DiffTransformer;
import com.wearewaes.diff.rs.util.DiffTool;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DiffService {

  private final MongoTemplate template;

  private final DiffTransformer transformer;

  private final DiffTool diffTool;

  public boolean createOrUpdateDiff(
      final String diffId,
      final DiffRequest request,
      final DiffDirection direction) {

    Optional<DiffEntry> optionalEntry = this.getDiffEntry(diffId);

    if (optionalEntry.isPresent()) {
      direction.setContent(optionalEntry.get(), new Content(request.getValue()));
      template.save(optionalEntry.get());
      return true;

    } else {
      final DiffEntry diffEntry = transformer
          .fromRequest(diffId, request, direction);
      return template.save(diffEntry) != null;
    }
  }

  public Optional<DiffResult> getDiffResult(String diffId) {
    final Optional<DiffEntry> entry = this.getDiffEntry(diffId);
    return entry.map(transformer::fromEntry)
        .map(diffTool::infer);
  }

  private Optional<DiffEntry> getDiffEntry(String diffId) {
    return Optional.ofNullable(template.findById(diffId, DiffEntry.class));
  }
}
