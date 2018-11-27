package com.nkorasa.cmp;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.nkorasa.cmp.result.CmpResult;

/**
 * Builder used to configure comparing of collections of same objects performed in {@link CollectionCmp}
 * @see CollectionCmpBuilder to compare objects with different types
 * @param <O> objects generic type
 */
@SuppressWarnings({"AssignmentOrReturnOfFieldWithMutableType", "OverloadedVarargsMethod"})
public class CollectionCmpSameBuilder<O>
{
  private final Collection<O> baseList;
  private final Collection<O> workingList;

  private BiFunction<O, O, Boolean> equalsFunction = EqualsUtils.DEFAULT_EQUALS_FUNCTION::apply;

  /**
   * Initialize builder base and working collections.
   * @param baseList base collection to compare
   * @param workingList working collection to compare
   */
  public CollectionCmpSameBuilder(final Collection<O> baseList, final Collection<O> workingList)
  {
    this.baseList = baseList;
    this.workingList = workingList;
  }

  /**
   * Add equals function to compare items matched by same key. Default equals function is {@link #equalsFunction}.
   * @param equalsFunction equals function to compare matched items with
   * @return builder instance
   */
  public CollectionCmpSameBuilder<O> withEquals(final BiFunction<O, O, Boolean> equalsFunction)
  {
    this.equalsFunction = equalsFunction;
    return this;
  }

  /**
   * Use different (simpler) objects to compare items matched by the same key.
   *
   * Default equals function {@link #equalsFunction} is used to define equality of extracted objects.
   *
   * Example would be to use String representations of items as equal objects.
   *
   * @param objectExtractor function to extract an equal object from item
   * @return builder instance
   */
  public CollectionCmpSameBuilder<O> withEqualObject(final Function<O, Object> objectExtractor)
  {
    equalsFunction = (b, w) -> objectExtractor.apply(b).equals(objectExtractor.apply(w));
    return this;
  }

  /**
   * Add objects fields based on which objects are compared. All other fields are ignored. Default equals function is {@link #equalsFunction}.
   * @param equalFields objects fields based on which objects are compared
   * @return builder instance
   */
  @SafeVarargs
  public final CollectionCmpSameBuilder<O> withEqualFields(final Function<O, ?>... equalFields)
  {
    equalsFunction = EqualsUtils.buildEqualsFunction(equalFields);
    return this;
  }

  /**
   * Calls {@link CollectionCmp#compare(Function, Function, BiFunction)} with provided key extractors.
   * @see CollectionCmp#compare(Function, Function, BiFunction)
   * @param keyExtractor key extractor used to extract keys from items inside {@link #baseList} and {@link #workingList}
   * @return compare result, containing all changes
   */
  public CmpResult<O, O> compare(final Function<O, Serializable> keyExtractor)
  {
    return new CollectionCmp<>(baseList, workingList).compare(keyExtractor, keyExtractor, equalsFunction);
  }
}