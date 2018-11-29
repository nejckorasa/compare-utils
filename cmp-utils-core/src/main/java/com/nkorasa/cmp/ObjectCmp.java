package com.nkorasa.cmp;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Class to compare objects of same or different type
 */
@SuppressWarnings({"OverloadedVarargsMethod", "ObjectEquality", "BooleanMethodNameMustStartWithQuestion"})
public final class ObjectCmp
{
  private ObjectCmp() { }

  /**
   * Compares base and working objects using default equals function. Same as calling {@link Objects#compare(Object, Object,
   * Comparator)}.
   *
   * @param base base object to compare
   * @param working working object to compare
   * @param <O> objects generic type
   *
   * @return true/false whether objects are equal
   */
  public static <O> boolean equals(final O base, final O working)
  {
    return isEquals(base, working, null);
  }

  /**
   * Compares base and working objects using equals function equalsFunction.
   *
   * @param base base object to compare
   * @param working working object to compare
   * @param equalsFunction equals function to compare objects with
   * @param <B> base objects generic type
   * @param <W> working objects generic type
   *
   * @return true/false whether objects are equal
   */
  public static <B, W> boolean equals(final B base, final W working, final BiFunction<B, W, Boolean> equalsFunction)
  {
    return isEquals(base, working, equalsFunction);
  }

  /**
   * Compares base and working objects using equality
   *
   * @param base base object to compare
   * @param working working object to compare
   * @param equality equality based on which objects are compared
   * @param <O> objects generic type
   *
   * @return true/false whether objects are equal
   *
   * @see #equalEquality(Object, Object, List)
   */
  public static <O> boolean equalEquality(final O base, final O working, final Function<O, ?> equality)
  {
    return isEquals(base, working, EqualsUtils.buildEqualsFunctionFromEqualities(Collections.singletonList(equality)));
  }

  /**
   * Compares base and working objects using equalities. All equalities must match in order for objects to be considered
   * equal.
   * <p>Equality is a function that takes object as an input parameter and returns a value.
   * Two objects are considered equal if the results of all it's equalities are equal.
   * <p>You can use this option to compare objects of same type based on a few of their fields.
   *
   * @param base base object to compare
   * @param working working object to compare
   * @param equalities equalities based on which objects are compared
   * @param <O> objects generic type
   *
   * @return true/false whether objects are equal
   */
  public static <O> boolean equalEquality(final O base, final O working, final List<Function<O, ?>> equalities)
  {
    return isEquals(base, working, EqualsUtils.buildEqualsFunctionFromEqualities(equalities));
  }

  /**
   * Compares base and working objects using equality pair
   *
   * @param base base object to compare
   * @param working working object to compare
   * @param equalityPair equality pair based on which objects are compared
   * @param <B> objects generic type
   * @param <W> working generic type
   *
   * @return true/false whether objects are equal
   *
   * @see EqualityPair
   * @see #equalEqualityPair(Object, Object, List)
   */
  public static <B, W> boolean equalEqualityPair(final B base, final W working, final EqualityPair<B, W> equalityPair)
  {
    return isEquals(
        base,
        working,
        EqualsUtils.buildEqualsFunctionFromEqualityPairs(Collections.singletonList(equalityPair)));
  }

  /**
   * Compares base and working objects using equality pairs. All equality pairs must match in order for objects to be
   * considered equal.
   * <p>Equality is a function that takes object as an input parameter and returns a value.
   * Two objects are considered equal if the results of all it's equalities are equal.
   * <p>You can use this option to compare objects of same type based on a few of their fields.
   *
   * @param base base object to compare
   * @param working working object to compare
   * @param equalityPairs equality pairs based on which objects are compared
   * @param <B> objects generic type
   * @param <W> working generic type
   *
   * @return true/false whether objects are equal
   *
   * @see EqualityPair
   */
  public static <B, W> boolean equalEqualityPair(final B base, final W working, final List<EqualityPair<B, W>> equalityPairs)
  {
    return isEquals(base, working, EqualsUtils.buildEqualsFunctionFromEqualityPairs(equalityPairs));
  }

  private static <B, W> boolean isEquals(final B base, final W working, final BiFunction<B, W, Boolean> equalsFunction)
  {
    if (equalsFunction == null)
    {
      return Objects.equals(base, working);
    }

    return equalsFunction.apply(base, working);
  }
}
