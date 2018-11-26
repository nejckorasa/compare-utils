package com.nkorasa.cmp;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.nkorasa.cmp.result.CmpResult;

@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
public class CollectionCmpBuilder<B, W>
{
  private final Collection<B> baseList;
  private final Collection<W> workingList;

  private BiFunction<B, W, Boolean> equalsFunction = EqualsUtils.DEFAULT_EQUALS_FUNCTION::apply;

  public CollectionCmpBuilder(final Collection<B> baseList, final Collection<W> workingList)
  {
    this.baseList = baseList;
    this.workingList = workingList;
  }

  public CollectionCmpBuilder<B, W> withEquals(final BiFunction<B, W, Boolean> equalsFunction)
  {
    this.equalsFunction = equalsFunction;
    return this;
  }

  public CmpResult<B, W> compare(
      final Function<B, Serializable> baseKeyExtractor,
      final Function<W, Serializable> workingKeyExtractor)
  {
    return new CollectionCmp<>(baseList, workingList).compare(baseKeyExtractor, workingKeyExtractor, equalsFunction);
  }
}