package io.github.nejckorasa;

import io.github.nejckorasa.result.CmpResult;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.github.nejckorasa.EqualsUtils.DEFAULT_EQUALS_FUNCTION;
import static io.github.nejckorasa.EqualsUtils.buildEqualsFunctionFromEqualities;

/**
 * Builder used to configure comparing of collections of same objects performed in {@link CollectionCmp}
 *
 * @see CollectionCmpBuilder to compare objects with different types
 */
public class CollectionCmpSameBuilder<O> {
    private final Collection<O> baseList;
    private final Collection<O> workingList;
    private final Function<O, Serializable> keyExtractor;

    private BiFunction<O, O, Boolean> equalsFunction = DEFAULT_EQUALS_FUNCTION::apply;

    /**
     * Initialize builder base and working collections.
     *
     * @param baseList     base collection to compare
     * @param workingList  working collection to compare
     * @param keyExtractor key extractor used to extract keys from items inside {@link #baseList} and {@link #workingList}
     */
    CollectionCmpSameBuilder(Collection<O> baseList, Collection<O> workingList, Function<O, Serializable> keyExtractor) {
        this.baseList = baseList;
        this.workingList = workingList;
        this.keyExtractor = keyExtractor;
    }

    /**
     * @param equalsFunction based on which objects are compared
     * @return compare result, containing all changes
     */
    public CmpResult<O, O> compare(BiFunction<O, O, Boolean> equalsFunction) {
        this.equalsFunction = equalsFunction;
        return new CollectionCmp<>(baseList, workingList).compare(keyExtractor, keyExtractor, equalsFunction);
    }

    /**
     * @param equality based on which objects are compared
     * @return compare result, containing all changes
     */
    @SafeVarargs
    public final CmpResult<O, O> compare(Function<O, ?>... equality) {
        equalsFunction = buildEqualsFunctionFromEqualities(List.of(equality));
        return new CollectionCmp<>(baseList, workingList).compare(keyExtractor, keyExtractor, equalsFunction);
    }
}