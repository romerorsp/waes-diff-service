package com.wearewaes.diff.rs.transform;

import com.wearewaes.diff.rs.model.Content;
import com.wearewaes.diff.rs.model.DiffEntry;
import java.util.function.BiConsumer;

public enum DiffDirection {

  RIGHT(DiffEntry::setRight),
  LEFT(DiffEntry::setLeft);

  private final BiConsumer<DiffEntry, Content> setter;

  DiffDirection(final BiConsumer<DiffEntry, Content> setter) {
    this.setter = setter;
  }

  public void setContent(final DiffEntry diffEntry, final Content content) {
    setter.accept(diffEntry, content);
  }
}
