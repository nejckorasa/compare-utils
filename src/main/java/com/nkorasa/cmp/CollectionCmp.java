package com.nkorasa.cmp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.nkorasa.cmp.result.CmpPair;
import com.nkorasa.cmp.result.CmpResult;

@SuppressWarnings({"unused", "OverloadedVarargsMethod"})
public final class CollectionCmp<B, W>
{
  // TODO -> intersect only warning?

  private final Collection<B> baseList;
  private final Collection<W> workingList;

  @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
  CollectionCmp(final Collection<B> baseList, final Collection<W> workingList)
  {
    this.baseList = baseList;
    this.workingList = workingList;
  }

  public static <B, W> CollectionCmpBuilder<B, W> of(final Collection<B> baseList, final Collection<W> workingList)
  {
    return new CollectionCmpBuilder<>(baseList, workingList);
  }

  public static <B> CollectionCmpSameBuilder<B> ofSame(final Collection<B> baseList, final Collection<B> workingList)
  {
    return new CollectionCmpSameBuilder<>(baseList, workingList);
  }

  public CmpResult<B, W> compare(
      final Function<B, Serializable> baseKeyExtractor,
      final Function<W, Serializable> workingKeyExtractor,
      final BiFunction<B, W, Boolean> equalsFunction)
  {
    final Map<Serializable, B> basePartition = CollectionCmpPartitioner.buildPartition(baseList, baseKeyExtractor);
    final Map<Serializable, W> workingPartition = CollectionCmpPartitioner.buildPartition(workingList, workingKeyExtractor);

    //noinspection TooBroadScope
    final List<CmpPair<B, W>> added = new ArrayList<>();
    final List<CmpPair<B, W>> removed = new ArrayList<>();
    final List<CmpPair<B, W>> updated = new ArrayList<>();
    final List<CmpPair<B, W>> unchanged = new ArrayList<>();

    basePartition.forEach((key, base) -> {

      if (workingPartition.containsKey(key))
      {
        final W working = workingPartition.get(key);
        if (ObjectCmp.equals(base, working, equalsFunction))
        {
          unchanged.add(CmpPair.unchanged(key, base));
        }
        else
        {
          updated.add(CmpPair.updated(key, base, working));
        }
      }
      else
      {
        removed.add(CmpPair.removed(key, base));
      }
    });

    if (baseList.size() != workingList.size() || !removed.isEmpty())
    {
      final Set<Serializable> addedKeys = new HashSet<>(workingPartition.keySet());
      addedKeys.removeAll(basePartition.keySet());
      addedKeys.forEach(multiKey -> added.add(CmpPair.added(multiKey, workingPartition.get(multiKey))));
    }

    return new CmpResult<>(removed, added, updated, unchanged);
  }
}
