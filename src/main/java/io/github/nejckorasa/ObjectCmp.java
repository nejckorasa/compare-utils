package io.github.nejckorasa;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.github.nejckorasa.EqualsUtils.buildEqualsFunctionFromEqualities;
import static io.github.nejckorasa.EqualsUtils.buildEqualsFunctionFromEqualityPairs;

/**
 * Class to compare objects of same or different type
 */
public final class ObjectCmp {

    /**
     * Compares base and working objects using default equals function. Same as calling
     * {@link Objects#equals(Object, Object)}
     * <p>Comparison by reference is always performed first, also if one of the objects is {@code null}, false is returned.
     *
     * @param base    base object to compare
     * @param working working object to compare
     * @return true/false whether objects are equal
     */
    public static <O> boolean equals(O base, O working) {
        return isEquals(base, working, null);
    }

    /**
     * Compares base and working objects using equals function equalsFunction. Similar as calling
     * {@link Objects#compare(Object, Object, Comparator)}
     * <p>Comparison by reference is always performed first, also if one of the objects is {@code null}, false is returned.
     *
     * @param base           base object to compare
     * @param working        working object to compare
     * @param equalsFunction equals function to compare objects with
     * @return true/false whether objects are equal
     */
    public static <B, W> boolean equals(B base, W working, BiFunction<B, W, Boolean> equalsFunction) {
        return isEquals(base, working, equalsFunction);
    }

    /**
     * Compares base and working objects using equality
     * <p>Comparison by reference is always performed first, also if one of the objects is {@code null}, false is returned.
     *
     * @param base     base object to compare
     * @param working  working object to compare
     * @param equality equality based on which objects are compared
     * @param <O>      objects generic type
     * @return true/false whether objects are equal
     * @see #equals(Object, Object, List)
     */
    public static <O> boolean equals(O base, O working, Function<O, ?> equality) {
        return isEquals(base, working, buildEqualsFunctionFromEqualities(List.of(equality)));
    }

    /**
     * Compares base and working objects using equalities. All equalities must match in order for objects to be considered
     * equal.
     * <p>Equality is a function that takes object as an input parameter and returns a value.
     * Two objects are considered equal if the results of all it's equalities are equal.
     * <p>You can use this option to compare objects of same type based on a few of their fields.
     * <p>Comparison by reference is always performed first, also if one of the objects is {@code null}, false is returned.
     *
     * @param base       base object to compare
     * @param working    working object to compare
     * @param equalities equalities based on which objects are compared
     * @param <O>        objects generic type
     * @return true/false whether objects are equal
     */
    public static <O> boolean equals(O base, O working, List<Function<O, ?>> equalities) {
        return isEquals(base, working, buildEqualsFunctionFromEqualities(equalities));
    }

    /**
     * Compares base and working objects using equality pair
     * <p>Comparison by reference is always performed first, also if one of the objects is {@code null}, false is returned.
     *
     * @param base    base object to compare
     * @param working working object to compare
     * @param eqPair  equality pair based on which objects are compared
     * @return true/false whether objects are equal
     * @see EqPair
     */
    @SafeVarargs
    public static <B, W> boolean equals(B base, W working, EqPair<B, W>... eqPair) {
        return isEquals(base, working, buildEqualsFunctionFromEqualityPairs(List.of(eqPair)));
    }

    private static <B, W> boolean isEquals(B base, W working, BiFunction<B, W, Boolean> equalsFunction) {
        if (equalsFunction == null) return Objects.equals(base, working);
        if (base == working) return true;
        if (base == null ^ working == null) return false;
        return equalsFunction.apply(base, working);
    }
}
