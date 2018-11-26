package com.nkorasa.cmp;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Class to compare objects of same or different type
 */
@SuppressWarnings({"OverloadedVarargsMethod", "ObjectEquality"})
public final class ObjectCmp
{
  private ObjectCmp() { }

  /**
   * Compares base and working objects using default equals function. Same as calling {@link Objects#compare(Object, Object, Comparator)}
   *
   * To compare objects of different types call {@link #equals(Object, Object, BiFunction)}
   *
   * @param base base object to compare
   * @param working working object to compare
   * @param <O> objects generic type
   * @return true/false weather objects are equal
   */
  public static <O> boolean equals(final O base, final O working)
  {
    return isEquals(base, working, null);
  }

  /**
   * Compares {base and working objects using equals function equalsFunction
   *
   * To compare objects of different types call {@link #equals(Object, Object, BiFunction)}
   *
   * @param base base object to compare
   * @param working working object to compare
   * @param equalFields objects fields based on which objects are confirmed
   * @param <O> objects generic type
   * @return true/false weather objects are equal
   */
  @SafeVarargs
  public static <O> boolean equals(final O base, final O working, final Function<O, ?>... equalFields)
  {
    return isEquals(base, working, EqualsUtils.buildEqualsFunction(equalFields));
  }

  /**
   * Compares base and working objects using equals function equalsFunction
   *
   * To compare objects of same type call {@link #equals(Object, Object)} or {@link #equals(Object, Object, Function[])}
   *
   * @param base base object to compare
   * @param working working object to compare
   * @param equalsFunction equals function to compare objects with
   * @param <B> base objects generic type
   * @param <M> working objects generic type
   * @return true/false weather objects are equal
   */
  public static <B, M> boolean equals(final B base, final M working, final BiFunction<B, M, Boolean> equalsFunction)
  {
    return isEquals(base, working, equalsFunction);
  }

  private static <B, M> boolean isEquals(final B base, final M working, final BiFunction<B, M, Boolean> equalsFunction)
  {
    if (equalsFunction == null)
    {
      return Objects.equals(base, working);
    }

    if (base == working)
    {
      return true;
    }

    if (base == null ^ working == null)
    {
      return false;
    }

    return equalsFunction.apply(base, working);
  }
}
