package com.nkorasa.compares;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.nkorasa.compares.result.CmpResult;

/**
 * Builder used to configure comparing of collections of same objects performed in {@link CollectionCmp}
 *
 * @param <O> objects generic type
 *
 * @see CollectionCmpBuilder to compare objects with different types
 */
@SuppressWarnings({"AssignmentOrReturnOfFieldWithMutableType", "OverloadedVarargsMethod"})
public class CollectionCmpSameBuilder<O>
{
  private final Collection<O> baseList;
  private final Collection<O> workingList;

  private BiFunction<O, O, Boolean> equalsFunction = EqualsUtils.DEFAULT_EQUALS_FUNCTION::apply;

  /**
   * Initialize builder base and working collections.
   *
   * @param baseList base collection to compare
   * @param workingList working collection to compare
   */
  CollectionCmpSameBuilder(final Collection<O> baseList, final Collection<O> workingList)
  {
    this.baseList = baseList;
    this.workingList = workingList;
  }

  /**
   * Add equals function to compare items matched by same key. Default equals function is {@link #equalsFunction}.
   *
   * @param equalsFunction equals function to compare matched items with
   *
   * @return builder instance
   */
  public CollectionCmpSameBuilder<O> withEquals(final BiFunction<O, O, Boolean> equalsFunction)
  {
    this.equalsFunction = equalsFunction;
    return this;
  }

  /**
   * Compare matched items based on equality
   *
   * @param equality equality based on which objects are compared
   *
   * @return builder instance
   *
   * @see #withEqualities(List)
   */
  public final CollectionCmpSameBuilder<O> withEquality(final Function<O, ?> equality)
  {
    equalsFunction = EqualsUtils.buildEqualsFunctionFromEqualities(Collections.singletonList(equality));
    return this;
  }

  /**
   * Compare matched items based on equalities. All equalities must match in order for items to be considered equal.
   * <p>Equality is a function that takes item as an input parameter and returns a value.
   * Two items are considered equal if the results of all it's equalities are equal.
   * <p>You can use this option to compare items of same type based on a few of their fields.
   *
   * @param equalities equalities based on which objects are compared
   *
   * @return builder instance
   */
  public final CollectionCmpSameBuilder<O> withEqualities(final List<Function<O, ?>> equalities)
  {
    equalsFunction = EqualsUtils.buildEqualsFunctionFromEqualities(equalities);
    return this;
  }

  /**
   * Calls {@link CollectionCmp#compare(Function, Function, BiFunction)} with provided key extractors.
   *
   * @param keyExtractor key extractor used to extract keys from items inside {@link #baseList} and {@link #workingList}
   *
   * @return compare result, containing all changes
   *
   * @see CollectionCmp#compare(Function, Function, BiFunction)
   */
  public CmpResult<O, O> compare(final Function<O, Serializable> keyExtractor)
  {
    return new CollectionCmp<>(baseList, workingList).compare(keyExtractor, keyExtractor, equalsFunction);
  }
}