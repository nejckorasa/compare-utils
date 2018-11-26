package com.nkorasa.cmp;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.nkorasa.cmp.result.CmpResult;

/**
 * Builder used to configure comparing of collections of different objects performed in {@link CollectionCmp}
 * @see CollectionCmpSameBuilder to compare objects with same types
 * @param <B> base objects generic type
 * @param <W> working objects generic type
 */
@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
public class CollectionCmpBuilder<B, W>
{
  private final Collection<B> baseList;
  private final Collection<W> workingList;

  private BiFunction<B, W, Boolean> equalsFunction = EqualsUtils.DEFAULT_EQUALS_FUNCTION::apply;

  /**
   * Initialize builder base and working collections.
   * @param baseList base collection to compare
   * @param workingList working collection to compare
   */
  public CollectionCmpBuilder(final Collection<B> baseList, final Collection<W> workingList)
  {
    this.baseList = baseList;
    this.workingList = workingList;
  }

  /**
   * Add equals function to compare items matched by same key. Default equals function is {@link #equalsFunction}
   * @param equalsFunction equals function to compare matched items with
   * @return builder instance
   */
  public CollectionCmpBuilder<B, W> withEquals(final BiFunction<B, W, Boolean> equalsFunction)
  {
    this.equalsFunction = equalsFunction;
    return this;
  }

  /**
   * Calls {@link CollectionCmp#compare(Function, Function, BiFunction)} with provided key extractors.
   * @see CollectionCmp#compare(Function, Function, BiFunction)
   * @param baseKeyExtractor key extractor used to extract keys from items inside {@link #baseList}
   * @param workingKeyExtractor key extractor used to extract keys from items inside {@link #workingList}
   * @return compare result, containing all changes
   */
  public CmpResult<B, W> compare(
      final Function<B, Serializable> baseKeyExtractor,
      final Function<W, Serializable> workingKeyExtractor)
  {
    return new CollectionCmp<>(baseList, workingList).compare(baseKeyExtractor, workingKeyExtractor, equalsFunction);
  }
}