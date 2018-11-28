package com.nkorasa.cmp.result;

import java.io.Serializable;

/**
 * Compare pair that represents compare result between {@link #base} object from base collection and {@link #working} object
 * from working collection that are matched by the same key.
 * <p>Key is provided with {@link #key}.
 * Difference type is provided with {@link #diff}.
 * <p>If exists only in one collection and does not exist in another, it's value will be {@code null}. For example, all added
 * items will be
 * {@code null} in base collection, therefore {@link #base} will be {@code null}.
 *
 * @param <B> base object generic type
 * @param <W> working object generic type
 */
public class CmpPair<B, W>
{
  private final Serializable key;
  private final Diff diff;

  private final B base;
  private final W working;

  private CmpPair(final Serializable key, final B base, final W working, final Diff diff)
  {
    this.key = key;
    this.base = base;
    this.working = working;
    this.diff = diff;
  }

  public static <B, W> CmpPair<B, W> removed(final Serializable key, final B base)
  {
    return new CmpPair<>(key, base, null, Diff.REMOVED);
  }

  public static <B, W> CmpPair<B, W> updated(final Serializable key, final B base, final W working)
  {
    return new CmpPair<>(key, base, working, Diff.UPDATED);
  }

  public static <B, W> CmpPair<B, W> unchanged(final Serializable key, final B base)
  {
    return new CmpPair<>(key, base, null, Diff.UNCHANGED);
  }

  public static <B, W> CmpPair<B, W> added(final Serializable key, final W working)
  {
    return new CmpPair<>(key, null, working, Diff.ADDED);
  }

  /**
   * @return key that matched base and working objects
   */
  public Serializable getKey()
  {
    return key;
  }

  /**
   * @return base object or {@code null} if object does not exist in base collection
   */
  public B getBase()
  {
    return base;
  }

  /**
   * @return base object or {@code null} if object does not exist in working collection
   */
  public W getWorking()
  {
    return working;
  }

  /**
   * @return latest nonnull object - working is considered latest
   */
  public Object getLatest()
  {
    return working == null ? base : working;
  }

  /**
   * @return difference type between matched base and working objects
   */
  public Diff getDiff()
  {
    return diff;
  }

  @Override
  public String toString()
  {
    return String.format("DiffPair{key=%s, diff=%s, base=%s, working=%s}", key, diff, base, working);
  }
}
