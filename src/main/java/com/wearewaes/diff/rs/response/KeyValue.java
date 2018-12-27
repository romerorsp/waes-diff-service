package com.wearewaes.diff.rs.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class KeyValue<K, V> {

  private K key;

  private V value;

  private KeyValue(final K key, final V value) {
    this.key = key;
    this.value = value;
  }

  public static final <K, V> KeyValue<K, V> create(final K key, final V value) {
    return new KeyValue<>(key, value);
  }
}
