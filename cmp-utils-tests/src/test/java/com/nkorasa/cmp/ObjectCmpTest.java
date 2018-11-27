package com.nkorasa.cmp;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ObjectCmpTest
{
  @Test
  public void sameObjectsEquals()
  {
    assertTrue(ObjectCmp.equals(new TestObject("1", 1), new TestObject("1", 1)));
  }

  @Test
  public void sameObjectsNotEquals()
  {
    assertFalse(ObjectCmp.equals(new TestObject("1", 1), new TestObject("1", 2)));
  }

  @Test
  public void sameObjectsEqualitiesEquals()
  {
    assertTrue(ObjectCmp.equals(new TestObject("1", 1), new TestObject("1", 2), TestObject::getStrField));
  }

  @Test
  public void sameObjectsEqualitiesNotEquals()
  {
    assertFalse(ObjectCmp.equals(new TestObject("1", 1), new TestObject("1", 2), TestObject::getIntField));
  }

  @Test
  public void differentObjectsEqualFunctionEquals()
  {
    assertTrue(ObjectCmp.equals(new TestObject("1", 1), new TestObject2("1", 1L), (o1, o2) -> o1.getStrField().equals(o2.getStrField())));
  }

  @Test
  public void differentObjectsEqualFunctionNotEquals()
  {
    assertFalse(ObjectCmp.equals(new TestObject("1", 1), new TestObject2("1", 2L), (o1, o2) -> o1.getIntField().equals(o2.getLongField().intValue())));
  }

  @Test
  public void differentObjectsEqualityPairsEquals()
  {
    assertTrue(ObjectCmp.equals(
        new TestObject("1", 1),
        new TestObject2("1", 1L),
        EqualityPair.of(TestObject::getStrField, TestObject2::getStrField),
        EqualityPair.of(TestObject::getIntField, o2 -> o2.getLongField().intValue())));
  }

  @Test
  public void differentObjectsEqualityPairsNotEquals()
  {
    assertFalse(ObjectCmp.equals(
        new TestObject("1", 1),
        new TestObject2("1", 10L),
        EqualityPair.of(TestObject::getStrField, TestObject2::getStrField),
        EqualityPair.of(TestObject::getIntField, o2 -> o2.getLongField().intValue())));
  }
}
