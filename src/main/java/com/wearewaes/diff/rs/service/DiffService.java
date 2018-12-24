package com.wearewaes.diff.rs.service;

import com.wearewaes.diff.rs.request.DiffRequest;
import com.wearewaes.diff.rs.response.DiffResult;
import com.wearewaes.diff.rs.transform.DiffDirection;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class DiffService {

  private final MongoTemplate template;

  public DiffService(final MongoTemplate template) {
    this.template = template;
  }

  public boolean createOrUpdateDiff(
      final String diffId,
      final DiffRequest request,
      final DiffDirection direction) {
    return false;
  }

  public DiffResult getDiffResult(String diffId) {
    return null;
  }
}
