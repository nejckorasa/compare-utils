package com.nkorasa.cmp;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import com.nkorasa.cmp.exceptions.KeyCollisionException;

public final class CollectionCmpPartitioner
{
  private CollectionCmpPartitioner() { }

  public static <O> boolean canPartition(final Collection<O> collection, final Function<O, Serializable> keyExtractor)
  {
    try
    {
      buildPartition(collection, keyExtractor);
    }
    catch (final KeyCollisionException e)
    {
      return false;
    }

    return true;
  }

  public static <O> Map<Serializable, O> buildPartition(final Collection<O> collection, final Function<O, Serializable> keyExtractor)
  {
    final AtomicBoolean collision = new AtomicBoolean(false);
    final AtomicReference<Serializable> collisionKey = null;

    final Map<Serializable, O> partition = new HashMap<>();
    collection.forEach(item -> partition.compute(keyExtractor.apply(item), (k, v) -> {
      if (v != null)
      {
        collision.set(true);
        collisionKey.set(k);
      }
      return item;
    }));

    if (collision.get())
    {
      throw new KeyCollisionException("2 or more objects have the same key value: " + collisionKey + ", collection: " + collection);
    }

    return partition;
  }
}
