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
   * Builds equals function from equalFields
   * @param equalFields fields to compare
   * @param <O> objects generic type
   * @return equals function
   */
  static <O> BiFunction<O, O, Boolean> buildEqualsFunction(final Function<O, ?>[] equalFields)
  {
    if (equalFields == null || equalFields.length == 0)
    {
      return DEFAULT_EQUALS_FUNCTION::apply;
    }

    return (o1, o2) -> {
      final EqualsBuilder eb = new EqualsBuilder();
      Arrays.stream(equalFields).forEach(cf -> eb.append(cf.apply(o1), cf.apply(o2)));
      return eb.isEquals();
    };
  }

}
