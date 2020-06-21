package io.github.nejckorasa;

import io.github.nejckorasa.result.CmpResult;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.github.nejckorasa.EqualsUtils.DEFAULT_EQUALS_FUNCTION;
import static io.github.nejckorasa.EqualsUtils.buildEqualsFunctionFromEqualityPairs;

/**
 * Builder used to configure comparing of collections of different objects performed in {@link CollectionCmp}
 *
 * @see CollectionCmpSameBuilder to compare objects with same types
 */
public class CollectionCmpBuilder<B, W> {
    private final Collection<B> baseList;
    private final Collection<W> workingList;
    private final Function<B, Serializable> baseKeyExtractor;
    private final Function<W, Serializable> workingKeyExtractor;

    private BiFunction<B, W, Boolean> equalsFunction = DEFAULT_EQUALS_FUNCTION::apply;

    /**
     * Initialize builder base and working collections.
     *
     * @param baseKeyExtractor    key extractor used to extract keys from items inside {@link #baseList}
     * @param workingKeyExtractor key extractor used to extract keys from items inside {@link #workingList}
     * @param baseList            base collection to compare
     * @param workingList         working collection to compare
     */
    CollectionCmpBuilder(
            Collection<B> baseList,
            Collection<W> workingList,
            Function<B, Serializable> baseKeyExtractor,
            Function<W, Serializable> workingKeyExtractor
    ) {
        this.baseList = baseList;
        this.workingList = workingList;
        this.baseKeyExtractor = baseKeyExtractor;
        this.workingKeyExtractor = workingKeyExtractor;
    }

    /**
     * Use equals function to compare items matched by same key. Default equals function is {@link #equalsFunction}
     *
     * @param equalsFunction equals function to compare matched items with
     * @return compare result, containing all changes
     */
    public CmpResult<B, W> compare(BiFunction<B, W, Boolean> equalsFunction) {
        this.equalsFunction = equalsFunction;
        return new CollectionCmp<>(baseList, workingList).compare(baseKeyExtractor, workingKeyExtractor, equalsFunction);
    }

    /**
     * Compare matched items based on equality pair
     *
     * @param eqPair equality pair based on which objects are compared
     * @return compare result, containing all changes
     */
    @SafeVarargs
    public final CmpResult<B, W> compare(EqPair<B, W>... eqPair) {
        equalsFunction = buildEqualsFunctionFromEqualityPairs(List.of(eqPair));
        return new CollectionCmp<>(baseList, workingList).compare(baseKeyExtractor, workingKeyExtractor, equalsFunction);
    }
}