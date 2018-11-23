package com.nkorasa.cmp;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings({"OverloadedVarargsMethod", "ObjectEquality"})
public final class ObjectCmp
{
  private ObjectCmp() { }

  public static <O> boolean equals(final O base, final O working)
  {
    return isEquals(base, working, null);
  }

  @SafeVarargs
  public static <O> boolean equals(final O base, final O working, final Function<O, ?>... equalFields)
  {
    return isEquals(base, working, EqualsUtils.buildEqualsFunction(equalFields));
  }

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
