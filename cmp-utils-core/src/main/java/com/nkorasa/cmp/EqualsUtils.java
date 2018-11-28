package com.nkorasa.cmp;

import java.util.List;
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
   *
   * @param equalities equalities to compare
   * @param <O> objects generic type
   *
   * @return equals function
   */
  static <O> BiFunction<O, O, Boolean> buildEqualsFunctionFromEqualities(final List<Function<O, ?>> equalities)
  {
    if (equalities == null || equalities.isEmpty())
    {
      return DEFAULT_EQUALS_FUNCTION::apply;
    }

    return (o1, o2) -> {
      final EqualsBuilder eb = new EqualsBuilder();
      equalities.forEach(eq -> eb.append(eq.apply(o1), eq.apply(o2)));
      return eb.isEquals();
    };
  }

  /**
   * Builds equals function from equality pairs
   *
   * @param equalityPairs equality pairs to compare
   * @param <B> base objects generic type
   * @param <W> working objects generic type
   *
   * @return equals function
   */
  static <B, W> BiFunction<B, W, Boolean> buildEqualsFunctionFromEqualityPairs(final List<EqualityPair<B, W>> equalityPairs)
  {
    if (equalityPairs == null || equalityPairs.isEmpty())
    {
      return DEFAULT_EQUALS_FUNCTION::apply;
    }

    return (o1, o2) -> {
      final EqualsBuilder eb = new EqualsBuilder();
      equalityPairs.forEach(eqp -> eb.append(eqp.getBaseEquality().apply(o1), eqp.getWorkingEquality().apply(o2)));
      return eb.isEquals();
    };
  }
}
