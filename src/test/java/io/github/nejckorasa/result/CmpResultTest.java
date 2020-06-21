package io.github.nejckorasa.result;

import io.github.nejckorasa.TestObj;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CmpResultTest {

    @Test
    public void resultMethods() {
        var result = new CmpResult<>(
                List.of(
                        CmpPair.removed("1", new TestObj("1", 1)),
                        CmpPair.removed("2", new TestObj("2", 2))),
                List.of(
                        CmpPair.added("3", new TestObj("3", 3)),
                        CmpPair.added("4", new TestObj("4", 4))),
                List.of(
                        CmpPair.updated("5", new TestObj("5", 5), new TestObj("5", 50))),
                List.of(
                        CmpPair.unchanged("6", new TestObj("6", 6)),
                        CmpPair.unchanged("7", new TestObj("7", 7))));

        assertTrue(result.hasChanges());
        assertTrue(result.hasDifferences());

        assertEquals(4, result.getDifferentCount());
        assertEquals(5, result.getChangesCount());

        assertEquals(7, result.stream().count());
        assertEquals(4, result.streamDifferent().count());
        assertEquals(2, result.streamUnchanged().count());
        assertEquals(5, result.streamChanged().count());

        assertTrue(result.streamDifferent().anyMatch(t -> t.getBase() != null && t.getBase().getIntField().equals(1)));
        assertTrue(result.streamDifferent().anyMatch(t -> t.getBase() != null && t.getBase().getIntField().equals(2)));

        assertTrue(result.streamDifferent().anyMatch(t -> t.getWorking() != null && t.getWorking().getIntField().equals(3)));
        assertTrue(result.streamDifferent().anyMatch(t -> t.getWorking() != null && t.getWorking().getIntField().equals(4)));

        var forEachCount = new AtomicInteger();
        result.forEach(r -> forEachCount.getAndIncrement());
        assertEquals(7, forEachCount.get());

        forEachCount.set(0);
        result.forEachChanged(r -> forEachCount.getAndIncrement());
        assertEquals(5, forEachCount.get());

        var resultMap = result.asMap();
        assertEquals(2, resultMap.get(Diff.ADDED).size());
        assertEquals(2, resultMap.get(Diff.REMOVED).size());
        assertEquals(1, resultMap.get(Diff.UPDATED).size());
        assertEquals(2, resultMap.get(Diff.UNCHANGED).size());
    }
}