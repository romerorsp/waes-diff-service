package com.wearewaes.diff.rs.matcher;

import com.wearewaes.diff.rs.response.KeyValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.springframework.util.comparator.Comparators;

public class KeyValueCollectionComparator<K extends Comparable, V extends Comparable> extends BaseMatcher<List<KeyValue<K, V>>> {

  private final List<KeyValue<K, V>> expected;

  private KeyValueCollectionComparator(final List<KeyValue<K, V>> expected) {
    this.expected = expected;
  }

  public static <K extends Comparable, V extends Comparable> KeyValueCollectionComparator<K, V> taking(
      final List<KeyValue<K, V>> expected) {
    return new KeyValueCollectionComparator<>(expected);
  }


  @SuppressWarnings("unchecked")
  @Override
  public boolean matches(Object item) {
    if(item == null && expected == null) {
      return true;
    }

    if(!(item instanceof List)) {
      return false;
    }

    try {
      final List<KeyValue<K, V>> compared = (List<KeyValue<K, V>>) item;

      if (compared.size() != expected.size()) {
        return false;
      }

      // create sorted copies to avoid modifying the original lists
      List<KeyValue<K, V>> copy1 = new ArrayList<>(compared);
      List<KeyValue<K, V>> copy2 = new ArrayList<>(expected);

      final Comparator comparator =
          Comparator.<KeyValue<K, V>, K>comparing(KeyValue::getKey).thenComparing(KeyValue::getValue);

      Collections.sort(copy1, comparator);
      Collections.sort(copy2, comparator);

      Iterator<KeyValue<K, V>> it1 = copy1.iterator();
      Iterator<KeyValue<K, V>> it2 = copy2.iterator();
      while (it1.hasNext()) {
        KeyValue<K, V> t1 = it1.next();
        KeyValue<K, V> t2 = it2.next();
        if (comparator.compare(t1, t2) != 0) {
          return false;
        }
      }
      return true;
    } catch(ClassCastException e) {
      return false;
    }
  }

  @Override
  public void describeTo(Description description) {

  }
}
