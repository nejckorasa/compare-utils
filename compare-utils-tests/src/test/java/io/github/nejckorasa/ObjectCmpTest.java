package io.github.nejckorasa;

import java.util.Arrays;

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
  public void sameObjectsEqualityFunctionEquals()
  {
    assertTrue(ObjectCmp.equals(new TestObject("1", 1), new TestObject("1", 2), (testObject, testObject2) -> testObject.getStrField().equals(testObject2.getStrField())));
  }

  @Test
  public void sameObjectsEqualityFunctionNotEquals()
  {
    assertFalse(ObjectCmp.equals(new TestObject("2", 1), new TestObject("1", 2), (testObject, testObject2) -> testObject.getStrField().equals(testObject2.getStrField())));
  }

  @Test
  public void sameObjectsEqualitiesEquals()
  {
    assertTrue(ObjectCmp.equalEquality(new TestObject("1", 1), new TestObject("1", 2), TestObject::getStrField));
  }

  @Test
  public void sameObjectsEqualitiesNotEquals()
  {
    assertFalse(ObjectCmp.equalEquality(new TestObject("1", 1), new TestObject("1", 2), TestObject::getIntField));
  }

  @Test
  public void sameObjectsMultipleEqualitiesEquals()
  {
    assertTrue(ObjectCmp.equalEqualities(new TestObject("1", 1), new TestObject("1", 1), Arrays.asList(TestObject::getIntField, TestObject::getStrField)));
  }

  @Test
  public void sameObjectsMultipleEqualitiesNotEquals()
  {
    assertFalse(ObjectCmp.equalEqualities(new TestObject("1", 1), new TestObject("1", 2), Arrays.asList(TestObject::getIntField, TestObject::getStrField)));
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
    assertTrue(ObjectCmp.equalEqualityPairs(
        new TestObject("1", 1),
        new TestObject2("1", 1L),
        Arrays.asList(
            EqualityPair.of(TestObject::getStrField, TestObject2::getStrField),
            EqualityPair.of(TestObject::getIntField, o2 -> o2.getLongField().intValue()))));
  }

  @Test
  public void differentObjectsEqualityPairsNotEquals()
  {
    assertFalse(ObjectCmp.equalEqualityPairs(
        new TestObject("1", 1),
        new TestObject2("1", 10L),
        Arrays.asList(
            EqualityPair.of(TestObject::getStrField, TestObject2::getStrField),
            EqualityPair.of(TestObject::getIntField, o2 -> o2.getLongField().intValue()))));
  }

  @Test
  public void differentObjectsEqualityPairEquals()
  {
    assertTrue(ObjectCmp.equalEqualityPair(
        new TestObject("1", 10),
        new TestObject2("1", 10L),
        EqualityPair.of(TestObject::getIntField, t -> t.getLongField().intValue())));
  }

  @Test
  public void differentObjectsEqualityPairNotEquals()
  {
    assertFalse(ObjectCmp.equalEqualityPair(
        new TestObject("1", 1),
        new TestObject2("1", 10L),
        EqualityPair.of(TestObject::getIntField, t -> t.getLongField().intValue())));
  }

  @Test
  public void differentObjectsBothNull()
  {
    final TestObject obj1 = null;
    final TestObject2 obj2 = null;
    assertTrue(ObjectCmp.equalEqualityPair(
        obj1,
        obj2,
        EqualityPair.of(TestObject::getIntField, t -> t.getLongField().intValue())));
  }
  @Test
  public void differentObjectsOneNull()
  {
    final TestObject obj1 = null;
    final TestObject2 obj2 = new TestObject2("1", 1L);
    assertFalse(ObjectCmp.equalEqualityPair(
        obj1,
        obj2,
        EqualityPair.of(TestObject::getIntField, t -> t.getLongField().intValue())));
  }
}
