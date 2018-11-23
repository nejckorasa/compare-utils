package com.nkorasa.cmp.result;

import java.io.Serializable;

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

  public Serializable getKey()
  {
    return key;
  }

  public B getBase()
  {
    return base;
  }

  public W getWorking()
  {
    return working;
  }

  public Object getLatest()
  {
    return working == null ? base : working;
  }

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
