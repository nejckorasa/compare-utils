package io.github.nejckorasa;

import java.util.Objects;
import java.util.function.Function;

/**
 * Structure that defined if two objects are equal.
 * Objects are equal if the results of {@link #base} {@link #working} are equal.
 */
public class EqPair<B, W> {
    private final Function<B, ?> base;
    private final Function<W, ?> working;

    public EqPair(Function<B, ?> base, Function<W, ?> working) {
        this.base = base;
        this.working = working;
    }

    public static <B, W> EqPair<B, W> of(Function<B, ?> base, Function<W, ?> working) {
        return new EqPair<>(base, working);
    }

    public Function<B, ?> getBase() {
        return base;
    }

    public Function<W, ?> getWorking() {
        return working;
    }

    public boolean isEqual(B base, W working) {
        return Objects.equals(getBase().apply(base), getWorking().apply(working));
    }
}
