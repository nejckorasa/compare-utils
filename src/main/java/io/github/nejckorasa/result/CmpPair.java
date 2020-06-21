package io.github.nejckorasa.result;

import java.io.Serializable;

import static java.lang.String.format;

/**
 * Compare pair that represents compare result between {@link #base} object from base collection and {@link #working} object
 * from working collection that are matched by the same key.
 */
public class CmpPair<B, W> {
    private final Serializable key;
    private final Diff diff;
    private final B base;
    private final W working;

    private CmpPair(Serializable key, B base, W working, Diff diff) {
        this.key = key;
        this.base = base;
        this.working = working;
        this.diff = diff;
    }

    public static <B, W> CmpPair<B, W> removed(Serializable key, B base) {
        return new CmpPair<>(key, base, null, Diff.REMOVED);
    }

    public static <B, W> CmpPair<B, W> updated(Serializable key, B base, W working) {
        return new CmpPair<>(key, base, working, Diff.UPDATED);
    }

    public static <B, W> CmpPair<B, W> unchanged(Serializable key, B base) {
        return new CmpPair<>(key, base, null, Diff.UNCHANGED);
    }

    public static <B, W> CmpPair<B, W> added(Serializable key, W working) {
        return new CmpPair<>(key, null, working, Diff.ADDED);
    }

    /**
     * @return key that matched base and working objects
     */
    public Serializable getKey() {
        return key;
    }

    /**
     * @return base object or {@code null} if object does not exist in base collection
     */
    public B getBase() {
        return base;
    }

    /**
     * @return base object or {@code null} if object does not exist in working collection
     */
    public W getWorking() {
        return working;
    }

    /**
     * @return latest nonnull object - working is considered latest
     */
    public Object getLatest() {
        return working == null ? base : working;
    }

    /**
     * @return difference type between matched base and working objects
     */
    public Diff getDiff() {
        return diff;
    }

    @Override
    public String toString() {
        return format("DiffPair{key=%s, diff=%s, base=%s, working=%s}", key, diff, base, working);
    }
}
