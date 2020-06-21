package io.github.nejckorasa;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

final class EqualsUtils {
    static final BiFunction<Object, Object, Boolean> DEFAULT_EQUALS_FUNCTION = Objects::equals;

    static <O> BiFunction<O, O, Boolean> buildEqualsFunctionFromEqualities(List<Function<O, ?>> equalities) {
        if (equalities == null || equalities.isEmpty()) {
            return DEFAULT_EQUALS_FUNCTION::apply;
        }
        return (b, w) -> equalities.stream().allMatch(eq -> Objects.equals(eq.apply(b), eq.apply(w)));
    }

    static <B, W> BiFunction<B, W, Boolean> buildEqualsFunctionFromEqualityPairs(List<EqPair<B, W>> eqPairs) {
        if (eqPairs == null || eqPairs.isEmpty()) {
            return DEFAULT_EQUALS_FUNCTION::apply;
        }
        return (b, w) -> eqPairs.stream().allMatch(ep -> ep.isEqual(b, w));
    }
}
