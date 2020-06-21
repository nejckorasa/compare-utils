package io.github.nejckorasa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.github.nejckorasa.result.CmpPair;
import io.github.nejckorasa.result.CmpResult;
import io.github.nejckorasa.result.Diff;

import static io.github.nejckorasa.CollectionPartitioner.buildPartition;

/**
 * Class to compare collections of objects
 */
public final class CollectionCmp<B, W> {
    private final Collection<B> baseList;
    private final Collection<W> workingList;

    CollectionCmp(Collection<B> baseList, Collection<W> workingList) {
        this.baseList = baseList;
        this.workingList = workingList;
    }

    /**
     * Initializes builder {@link CollectionCmpBuilder} to compare collections of different object types (baseList and
     * workingList).
     * <p>
     * See {@link #of(Collection, Collection, Function)} to compare collections of objects of the same type.
     *
     * @param baseList    base list to compare
     * @param workingList working list to compare base list with
     * @return builder to configure
     */
    public static <B, W> CollectionCmpBuilder<B, W> of(
            Collection<B> baseList, Collection<W> workingList,
            Function<B, Serializable> baseKeyExtractor,
            Function<W, Serializable> workingKeyExtractor
    ) {
        return new CollectionCmpBuilder<>(baseList, workingList, baseKeyExtractor, workingKeyExtractor);
    }

    /**
     * Initializes builder {@link CollectionCmpBuilder} to compare collections of same object types (baseList and workingList).
     * <p>
     * See {@link #of(Collection, Collection, Function, Function)} to compare collections of objects of different type.
     *
     * @param baseList    base list to compare
     * @param workingList working list to compare base list with
     * @return builder to configure
     */
    public static <B> CollectionCmpSameBuilder<B> of(
            Collection<B> baseList,
            Collection<B> workingList,
            Function<B, Serializable> keyExtractor
    ) {
        return new CollectionCmpSameBuilder<>(baseList, workingList, keyExtractor);
    }

    /**
     * Compares collections.
     * <p>Items from {@link #baseList} and {@link #workingList} are compared and merged into pairs based on keys configured by
     * baseKeyExtractor and workingKeyExtractor. Items matched together by the same key are later compared using
     * equalsFunction.
     * <p>Make sure that collections {@link #baseList} and {@link #workingList} can be partitioned
     * using {@link CollectionPartitioner#canPartition(Collection, Function)}. If keys collide and partitioning is not
     * successful, compare result will not always be correct.
     * <p>The end result contains all changes found between collections - added, updated, removed, same and different items.
     * See {@link Diff} and {@link CmpResult}.
     *
     * @param baseKeyExtractor    key extractor used to extract keys from items inside {@link #baseList}
     * @param workingKeyExtractor key extractor used to extract keys from items inside {@link #workingList}
     * @param equalsFunction      equals function to compare items matched by key
     * @return compare result, containing all changes - information of added, updated, removed, different objects...
     */
    public CmpResult<B, W> compare(
            Function<B, Serializable> baseKeyExtractor,
            Function<W, Serializable> workingKeyExtractor,
            BiFunction<B, W, Boolean> equalsFunction
    ) {
        var basePartition = buildPartition(baseList, baseKeyExtractor);
        var workingPartition = buildPartition(workingList, workingKeyExtractor);

        List<CmpPair<B, W>> added = new ArrayList<>();
        List<CmpPair<B, W>> removed = new ArrayList<>();
        List<CmpPair<B, W>> updated = new ArrayList<>();
        List<CmpPair<B, W>> unchanged = new ArrayList<>();

        basePartition.forEach((key, base) -> {
            if (workingPartition.containsKey(key)) {
                final W working = workingPartition.get(key);
                if (ObjectCmp.equals(base, working, equalsFunction)) {
                    unchanged.add(CmpPair.unchanged(key, base));
                } else {
                    updated.add(CmpPair.updated(key, base, working));
                }
            } else {
                removed.add(CmpPair.removed(key, base));
            }
        });

        if (baseList.size() != workingList.size() || !removed.isEmpty()) {
            var addedKeys = new HashSet<>(workingPartition.keySet());
            addedKeys.removeAll(basePartition.keySet());
            addedKeys.forEach(multiKey -> added.add(CmpPair.added(multiKey, workingPartition.get(multiKey))));
        }
        return new CmpResult<>(removed, added, updated, unchanged);
    }
}
