package com.nkorasa.cmp;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
class CollectionCmpPartitionerTest
{

  @Test
  public void partitionSuccessful()
  {
    final List<TestObject> baseList = Arrays.asList(
        new TestObject("1", 1),
        new TestObject("2", 2),
        new TestObject("3", 3)
    );

    final Map<Serializable, TestObject> partitionMap = CollectionCmpPartitioner.buildPartition(
        baseList,
        TestObject::getStrField);

    assertEquals(3, partitionMap.size());
  }

  @Test
  public void partitionUnsuccessful()
  {
    final List<TestObject> baseList = Arrays.asList(
        new TestObject("1", 1),
        new TestObject("2", 2),
        new TestObject("2", 9)
    );

    final Map<Serializable, TestObject> partitionMap = CollectionCmpPartitioner.buildPartition(
        baseList,
        TestObject::getStrField);

    assertEquals(2, partitionMap.size());
    assertEquals(2, partitionMap.get("2").getIntField().intValue());
  }

  @Test
  public void canPartition()
  {
    final List<TestObject> baseList = Arrays.asList(
        new TestObject("1", 1),
        new TestObject("2", 2),
        new TestObject("3", 3)
    );

    final boolean canPartition = CollectionCmpPartitioner.canPartition(baseList, TestObject::getStrField);
    assertTrue(canPartition);
  }

  @Test
  public void cannotPartition()
  {
    final List<TestObject> baseList = Arrays.asList(
        new TestObject("1", 1),
        new TestObject("2", 2),
        new TestObject("2", 9)
    );

    final boolean canPartition = CollectionCmpPartitioner.canPartition(baseList, TestObject::getStrField);
    assertFalse(canPartition);
  }
}