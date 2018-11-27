package com.nkorasa.cmp;

import org.junit.jupiter.api.Test;

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
  public void sameObjectsEqualFieldsEquals()
  {
    assertTrue(ObjectCmp.equals(new TestObject("1", 1), new TestObject("1", 2), TestObject::getStrField));
  }

  @Test
  public void sameObjectsEqualFieldsNotEquals()
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
}
