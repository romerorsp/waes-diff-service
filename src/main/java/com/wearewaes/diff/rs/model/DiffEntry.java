package com.wearewaes.diff.rs.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "DiffEntry")
public class DiffEntry {

  @Id
  private String id;

  private Content left;

  private Content right;

}
