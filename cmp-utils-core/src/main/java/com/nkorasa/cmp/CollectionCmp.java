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

/**
 * Class to compare collections of objects
 *
 * @param <B> base objects generic type
 * @param <W> working objects generic type
 */
@SuppressWarnings({"OverloadedVarargsMethod"})
public final class CollectionCmp<B, W>
{
  private final Collection<B> baseList;
  private final Collection<W> workingList;

  @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
  CollectionCmp(final Collection<B> baseList, final Collection<W> workingList)
  {
    this.baseList = baseList;
    this.workingList = workingList;
  }

  /**
   * Initializes builder {@link CollectionCmpBuilder} to compare collections of different object types (baseList and
   * workingList). See {@link #ofSame(Collection, Collection)} to compare collections of objects of the same type.
   *
   * @param baseList base list to compare
   * @param workingList working list to compare base list with
   * @param <B> base objects generic type
   * @param <W> working objects generic type
   *
   * @return builder to configure
   */
  public static <B, W> CollectionCmpBuilder<B, W> ofDifferent(final Collection<B> baseList, final Collection<W> workingList)
  {
    return new CollectionCmpBuilder<>(baseList, workingList);
  }

  /**
   * Initializes builder {@link CollectionCmpBuilder} to compare collections of same object types (baseList and workingList).
   * See {@link #ofDifferent(Collection, Collection)} to compare collections of objects of different type.
   *
   * @param baseList base list to compare
   * @param workingList working list to compare base list with
   * @param <B> object generic type
   *
   * @return builder to configure
   */
  public static <B> CollectionCmpSameBuilder<B> ofSame(final Collection<B> baseList, final Collection<B> workingList)
  {
    return new CollectionCmpSameBuilder<>(baseList, workingList);
  }

  /**
   * Compares collections.
   * <p>Items from {@link #baseList} and {@link #workingList} are compared and merged into pairs based on keys configured by
   * baseKeyExtractor and workingKeyExtractor. Items matched together by the same key are later compared using
   * equalsFunction.
   * <p>Make sure that collections {@link #baseList} and {@link #workingList} can be partitioned
   * using {@link CollectionCmpPartitioner#canPartition(Collection, Function)}. If keys collide and partitioning is not
   * successful, compare result will not always be correct.
   * <p>The end result contains all changes found between collections - added, updated, removed, same and different items.
   * See {@link com.nkorasa.cmp.result.Diff} and {@link CmpResult}.
   *
   * @param baseKeyExtractor key extractor used to extract keys from items inside {@link #baseList}
   * @param workingKeyExtractor key extractor used to extract keys from items inside {@link #workingList}
   * @param equalsFunction equals function to compare items matched by key
   *
   * @return compare result, containing all changes - information of added, updated, removed, different objects...
   */
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
