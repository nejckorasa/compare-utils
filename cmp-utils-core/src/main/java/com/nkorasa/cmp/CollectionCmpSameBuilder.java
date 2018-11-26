package com.nkorasa.cmp;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.nkorasa.cmp.result.CmpResult;

@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
public class CollectionCmpSameBuilder<O>
{
  private final Collection<O> baseList;
  private final Collection<O> workingList;

  private BiFunction<O, O, Boolean> equalsFunction = EqualsUtils.DEFAULT_EQUALS_FUNCTION::apply;

  public CollectionCmpSameBuilder(final Collection<O> baseList, final Collection<O> workingList)
  {
    this.baseList = baseList;
    this.workingList = workingList;
  }

  public CollectionCmpSameBuilder<O> withEquals(final BiFunction<O, O, Boolean> equalsFunction)
  {
    this.equalsFunction = equalsFunction;
    return this;
  }

  @SafeVarargs
  public final CollectionCmpSameBuilder<O> withEquals(final Function<O, ?>... equalFields)
  {
    equalsFunction = EqualsUtils.buildEqualsFunction(equalFields);
    return this;
  }

  public CmpResult<O, O> compare(final Function<O, Serializable> keyExtractor)
  {
    return new CollectionCmp<>(baseList, workingList).compare(keyExtractor, keyExtractor, equalsFunction);
  }
}