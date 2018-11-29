package com.nkorasa.cmp;

import java.util.Arrays;
import java.util.List;

import com.nkorasa.cmp.result.CmpResult;
import com.nkorasa.cmp.result.Diff;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CollectionCmpTest
{
  @Test
  public void compareSameDefaultEqualsNoDifference()
  {
    final List<TestObject> baseList = Arrays.asList(
        new TestObject("1", 1),
        new TestObject("2", 2),
        new TestObject("3", 3)
    );

    final List<TestObject> workingList = Arrays.asList(
        new TestObject("1", 1),
        new TestObject("2", 2),
        new TestObject("3", 3)
    );

    final CmpResult<TestObject, TestObject> compareResult = CollectionCmp
        .ofSame(baseList, workingList)
        .compare(TestObject::getStrField);

    assertEquals(0, compareResult.getChangesCount());
  }

  @Test
  public void compareSameEqualitiesNoDifference()
  {
    final List<TestObject> baseList = Arrays.asList(
        new TestObject("1", 9),
        new TestObject("2", 9),
        new TestObject("3", 9)
    );

    final List<TestObject> workingList = Arrays.asList(
        new TestObject("1", 8),
        new TestObject("2", 8),
        new TestObject("3", 8)
    );

    final CmpResult<TestObject, TestObject> compareResult = CollectionCmp
        .ofSame(baseList, workingList)
        .withEquality(TestObject::getStrField)
        .compare(TestObject::getStrField);

    assertEquals(0, compareResult.getChangesCount());
  }

  @Test
  public void compareSameMultipleEqualitiesNoDifference()
  {
    final List<TestObject> baseList = Arrays.asList(
        new TestObject("1", 9),
        new TestObject("2", 9),
        new TestObject("3", 9)
    );

    final List<TestObject> workingList = Arrays.asList(
        new TestObject("1", 9),
        new TestObject("2", 9),
        new TestObject("3", 9)
    );

    final CmpResult<TestObject, TestObject> compareResult = CollectionCmp
        .ofSame(baseList, workingList)
        .withEquality(Arrays.asList(TestObject::getStrField, TestObject::getIntField))
        .compare(TestObject::getStrField);

    assertEquals(0, compareResult.getChangesCount());
  }

  @Test
  public void compareSameEqualitiesDifferenceAndUpdates()
  {
    final List<TestObject> baseList = Arrays.asList(
        new TestObject("1", 1),
        new TestObject("2", 2),
        new TestObject("3", 3)
    );

    final List<TestObject> workingList = Arrays.asList(
        new TestObject("2", 9),
        new TestObject("3", 3),
        new TestObject("4", 4)
    );

    final CmpResult<TestObject, TestObject> compareResult = CollectionCmp
        .ofSame(baseList, workingList)
        .withEquality(Arrays.asList(TestObject::getStrField, TestObject::getIntField))
        .compare(TestObject::getStrField);

    assertEquals(3, compareResult.getChangesCount());

    assertEquals(1, compareResult.getRemoved().size());
    assertEquals("1", compareResult.getRemoved().get(0).getKey());
    assertEquals(Diff.REMOVED, compareResult.getRemoved().get(0).getDiff());
    assertEquals(new TestObject("1", 1), compareResult.getRemoved().get(0).getBase());
    assertTrue(compareResult.getRemoved().stream().allMatch(r -> r.getWorking() == null));

    assertEquals(1, compareResult.getAdded().size());
    assertEquals("4", compareResult.getAdded().get(0).getKey());
    assertEquals(Diff.ADDED, compareResult.getAdded().get(0).getDiff());
    assertEquals(new TestObject("4", 4), compareResult.getAdded().get(0).getWorking());
    assertTrue(compareResult.getAdded().stream().allMatch(r -> r.getBase() == null));

    assertEquals(2, compareResult.getDifferentCount());
    assertTrue(compareResult.getDifferent()
                   .stream()
                   .anyMatch(d -> d.getWorking() != null && d.getWorking().equals(new TestObject("4", 4)) && d.getDiff() == Diff.ADDED));
    assertTrue(compareResult.getDifferent()
                   .stream()
                   .anyMatch(d -> d.getBase() != null && d.getBase().equals(new TestObject("1", 1)) && d.getDiff() == Diff.REMOVED));
  }

  @Test
  public void compareDifferentWithEqualsDifference()
  {
    final List<TestObject> baseList = Arrays.asList(
        new TestObject("1", 1),
        new TestObject("2", 2),
        new TestObject("3", 3)
    );

    final List<TestObject2> workingList = Arrays.asList(
        new TestObject2("2", 2L),
        new TestObject2("3", 30L),
        new TestObject2("4", 4L)
    );

    final CmpResult<TestObject, TestObject2> compareResult = CollectionCmp
        .ofDifferent(baseList, workingList)
        .withEquals((o1, o2) -> o1.getIntField() == o2.getLongField().intValue())
        .compare(TestObject::getStrField, TestObject2::getStrField);

    assertEquals(3, compareResult.getChangesCount());

    assertEquals(1, compareResult.getRemoved().size());
    assertEquals("1", compareResult.getRemoved().get(0).getKey());
    assertEquals(Diff.REMOVED, compareResult.getRemoved().get(0).getDiff());
    assertEquals(new TestObject("1", 1), compareResult.getRemoved().get(0).getBase());
    assertTrue(compareResult.getRemoved().stream().allMatch(r -> r.getWorking() == null));

    assertEquals(1, compareResult.getAdded().size());
    assertEquals("4", compareResult.getAdded().get(0).getKey());
    assertEquals(Diff.ADDED, compareResult.getAdded().get(0).getDiff());
    assertEquals(new TestObject2("4", 4L), compareResult.getAdded().get(0).getWorking());
    assertTrue(compareResult.getAdded().stream().allMatch(r -> r.getBase() == null));

    assertEquals(2, compareResult.getDifferentCount());
  }

  @Test
  public void compareDifferentMultipleEqualitiesDifference()
  {
    final List<TestObject> baseList = Arrays.asList(
        new TestObject("1", 1),
        new TestObject("2", 2),
        new TestObject("3", 3)
    );

    final List<TestObject2> workingList = Arrays.asList(
        new TestObject2("2", 2L),
        new TestObject2("3", 30L),
        new TestObject2("4", 4L)
    );

    final CmpResult<TestObject, TestObject2> compareResult = CollectionCmp
        .ofDifferent(baseList, workingList)
        .withEqualityPair(Arrays.asList(
            EqualityPair.of(TestObject::getIntField, o2 -> o2.getLongField().intValue()),
            EqualityPair.of(TestObject::getStrField, TestObject2::getStrField)))
        .compare(TestObject::getStrField, TestObject2::getStrField);

    assertEquals(3, compareResult.getChangesCount());

    assertEquals(1, compareResult.getRemoved().size());
    assertEquals("1", compareResult.getRemoved().get(0).getKey());
    assertEquals(Diff.REMOVED, compareResult.getRemoved().get(0).getDiff());
    assertEquals(new TestObject("1", 1), compareResult.getRemoved().get(0).getBase());
    assertTrue(compareResult.getRemoved().stream().allMatch(r -> r.getWorking() == null));

    assertEquals(1, compareResult.getAdded().size());
    assertEquals("4", compareResult.getAdded().get(0).getKey());
    assertEquals(Diff.ADDED, compareResult.getAdded().get(0).getDiff());
    assertEquals(new TestObject2("4", 4L), compareResult.getAdded().get(0).getWorking());
    assertTrue(compareResult.getAdded().stream().allMatch(r -> r.getBase() == null));

    assertEquals(2, compareResult.getDifferentCount());
  }

}
