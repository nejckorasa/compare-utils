package io.github.nejckorasa.result;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.github.nejckorasa.TestObject;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CmpResultTest
{
  @Test
  public void resultMethods()
  {
    final CmpResult<TestObject, TestObject> result = new CmpResult<>(
        Arrays.asList(
            CmpPair.removed("1", new TestObject("1", 1)),
            CmpPair.removed("2", new TestObject("2", 2))),
        Arrays.asList(
            CmpPair.added("3", new TestObject("3", 3)),
            CmpPair.added("4", new TestObject("4", 4))),
        Collections.singletonList(CmpPair.updated("5", new TestObject("5", 5), new TestObject("5", 50))),
        Arrays.asList(
            CmpPair.unchanged("6", new TestObject("6", 6)),
            CmpPair.unchanged("7", new TestObject("7", 7))));

    assertTrue(result.hasChanges());
    assertTrue(result.hasDifferences());

    assertEquals(4, result.getDifferentCount());
    assertEquals(5, result.getChangesCount());

    assertEquals(7,  result.stream().count()) ;
    assertEquals(4, result.streamDifferent().count());
    assertEquals(2, result.streamUnchanged().count());
    assertEquals(5, result.streamChanged().count());

    assertTrue(result.streamDifferent().anyMatch(t -> t.getBase() != null && t.getBase().getIntField().equals(1)));
    assertTrue(result.streamDifferent().anyMatch(t -> t.getBase() != null && t.getBase().getIntField().equals(2)));

    assertTrue(result.streamDifferent().anyMatch(t -> t.getWorking() != null && t.getWorking().getIntField().equals(3)));
    assertTrue(result.streamDifferent().anyMatch(t -> t.getWorking() != null && t.getWorking().getIntField().equals(4)));

    final AtomicInteger forEachCount = new AtomicInteger();
    result.forEach(r -> forEachCount.getAndIncrement());
    assertEquals(7, forEachCount.get());

    forEachCount.set(0);
    result.forEachChanged(r -> forEachCount.getAndIncrement());
    assertEquals(5, forEachCount.get());

    final Map<Diff, Collection<CmpPair<TestObject, TestObject>>> resultMap = result.asMap();
    assertEquals(2, resultMap.get(Diff.ADDED).size());
    assertEquals(2, resultMap.get(Diff.REMOVED).size());
    assertEquals(1, resultMap.get(Diff.UPDATED).size());
    assertEquals(2, resultMap.get(Diff.UNCHANGED).size());
  }
}