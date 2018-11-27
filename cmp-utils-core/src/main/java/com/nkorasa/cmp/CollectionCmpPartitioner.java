package com.nkorasa.cmp;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utils class used to partition collection based on key extractor
 */
public final class CollectionCmpPartitioner
{
  private CollectionCmpPartitioner() { }

  /**
   * Checks if collection can be partitioned using key extracted using provided keyExtractor
   * @param collection collection to partition
   * @param keyExtractor key extractor used to extract keys from items in collection
   * @param <O> objects generic type
   * @return true if collection can be partitioned
   */
  public static <O> boolean canPartition(final Collection<O> collection, final Function<O, Serializable> keyExtractor)
  {
    final Map<Serializable, O> partition = buildPartition(collection, keyExtractor);
    return partition.keySet().size() == collection.size();
  }

  /**
   * Builds partition of collection using keyExtractor.
   *
   * <p>Partitioning is successful if all keys extracted from items in collection are unique and nonnull. If more than one item
   * has the same key, only the first item will exist in result partition.
   *
   * <p>Use {@link #canPartition(Collection, Function)} to check if collection can be partitioned.
   *
   * @param collection collection to partition
   * @param keyExtractor key extractor used to extract keys from items in collection
   * @param <O> objects generic type
   * @return map with collection items as values and it's keys as keys
   */
  public static <O> Map<Serializable, O> buildPartition(final Collection<O> collection, final Function<O, Serializable> keyExtractor)
  {
    final Map<Serializable, O> partition = new HashMap<>();
    collection.forEach(item -> partition.computeIfAbsent(keyExtractor.apply(item), k -> item));
    return partition;
  }
}
