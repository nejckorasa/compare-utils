package com.nkorasa.cmp;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Utils class to build equals function and hold defaults
 */
final class EqualsUtils
{
  static final BiFunction<Object, Object, Boolean> DEFAULT_EQUALS_FUNCTION = Objects::equals;

  private EqualsUtils() { }

  /**
   * Builds equals function from equalities
   * @param equalities equalities to compare
   * @param <O> objects generic type
   * @return equals function
   */
  static <O> BiFunction<O, O, Boolean> buildEqualsFunction(final Function<O, ?>[] equalities)
  {
    if (equalities == null || equalities.length == 0)
    {
      return DEFAULT_EQUALS_FUNCTION::apply;
    }

    return (o1, o2) -> {
      final EqualsBuilder eb = new EqualsBuilder();
      Arrays.stream(equalities).forEach(eq -> eb.append(eq.apply(o1), eq.apply(o2)));
      return eb.isEquals();
    };
  }

  /**
   * Builds equals function from equality pairs
   * @param equalityPairs equality pairs to compare
   * @param <B> base objects generic type
   * @param <W> working objects generic type
   * @return equals function
   */
  static <B, W> BiFunction<B, W, Boolean> buildEqualsFunction(final EqualityPair<B, W>[] equalityPairs)
  {
    if (equalityPairs == null || equalityPairs.length == 0)
    {
      return DEFAULT_EQUALS_FUNCTION::apply;
    }

    return (o1, o2) -> {
      final EqualsBuilder eb = new EqualsBuilder();
      Arrays.stream(equalityPairs).forEach(eqp -> eb.append(eqp.getBaseEquality().apply(o1), eqp.getWorkingEquality().apply(o2)));
      return eb.isEquals();
    };
  }

}
