package com.nkorasa.cmp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import com.nkorasa.cmp.result.CmpPair;
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
}
