package com.nkorasa.cmp;

import java.util.Arrays;
import java.util.List;

import com.nkorasa.cmp.result.CmpResult;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

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
  public void compareSameCustomEqualFieldsNoDifference()
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
        .withEquals(TestObject::getStrField)
        .compare(TestObject::getStrField);

    assertEquals(0, compareResult.getChangesCount());
  }

  @Test
  public void compareSameCustomEqualFieldsDifferenceAndUpdates()
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
        .withEquals(TestObject::getStrField, TestObject::getIntField)
        .compare(TestObject::getStrField);

    assertEquals(3, compareResult.getChangesCount());
    // TODO TEST ADDED, UPDATED, ...
  }
}
