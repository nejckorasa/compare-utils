package io.github.nejckorasa;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObjectCmpTest {

    @Test
    public void sameObjectsEquals() {
        assertTrue(ObjectCmp.equals(new TestObj("1", 1), new TestObj("1", 1)));
    }

    @Test
    public void sameObjectsNotEquals() {
        assertFalse(ObjectCmp.equals(new TestObj("1", 1), new TestObj("1", 2)));
    }

    @Test
    public void sameObjectsEqualityFunctionEquals() {
        assertTrue(ObjectCmp.equals(new TestObj("1", 1), new TestObj("1", 2), (testObject, testObject2) -> testObject.getStrField().equals(testObject2.getStrField())));
    }

    @Test
    public void sameObjectsEqualityFunctionNotEquals() {
        assertFalse(ObjectCmp.equals(new TestObj("2", 1), new TestObj("1", 2), (testObject, testObject2) -> testObject.getStrField().equals(testObject2.getStrField())));
    }

    @Test
    public void sameObjectsEqualitiesEquals() {
        assertTrue(ObjectCmp.equals(new TestObj("1", 1), new TestObj("1", 2), TestObj::getStrField));
    }

    @Test
    public void sameObjectsEqualitiesNotEquals() {
        assertFalse(ObjectCmp.equals(new TestObj("1", 1), new TestObj("1", 2), TestObj::getIntField));
    }

    @Test
    public void sameObjectsMultipleEqualitiesEquals() {
        assertTrue(ObjectCmp.equals(new TestObj("1", 1), new TestObj("1", 1), Arrays.asList(TestObj::getIntField, TestObj::getStrField)));
    }

    @Test
    public void sameObjectsMultipleEqualitiesNotEquals() {
        assertFalse(ObjectCmp.equals(new TestObj("1", 1), new TestObj("1", 2), List.of((TestObj::getIntField), TestObj::getStrField)));
    }

    @Test
    public void differentObjectsEqualFunctionEquals() {
        assertTrue(ObjectCmp.equals(new TestObj("1", 1), new TestObj2("1", 1L), (o1, o2) -> o1.getStrField().equals(o2.getStrField())));
    }

    @Test
    public void differentObjectsEqualFunctionNotEquals() {
        assertFalse(ObjectCmp.equals(new TestObj("1", 1), new TestObj2("1", 2L), (o1, o2) -> o1.getIntField().equals(o2.getLongField().intValue())));
    }

    @Test
    public void differentObjectsEqualityPairsEquals() {
        assertTrue(ObjectCmp.equals(
                new TestObj("1", 1),
                new TestObj2("1", 1L),
                EqPair.of(TestObj::getStrField, TestObj2::getStrField),
                EqPair.of(TestObj::getIntField, o2 -> o2.getLongField().intValue())));
    }

    @Test
    public void differentObjectsEqualityPairsNotEquals() {
        assertFalse(ObjectCmp.equals(
                new TestObj("1", 1),
                new TestObj2("1", 10L),
                EqPair.of(TestObj::getStrField, TestObj2::getStrField),
                EqPair.of(TestObj::getIntField, o2 -> o2.getLongField().intValue())));
    }

    @Test
    public void differentObjectsEqualityPairEquals() {
        assertTrue(ObjectCmp.equals(
                new TestObj("1", 10),
                new TestObj2("1", 10L),
                EqPair.of(TestObj::getIntField, t -> t.getLongField().intValue())));
    }

    @Test
    public void differentObjectsEqualityPairNotEquals() {
        assertFalse(ObjectCmp.equals(
                new TestObj("1", 1),
                new TestObj2("1", 10L),
                EqPair.of(TestObj::getIntField, t -> t.getLongField().intValue())));
    }

    @Test
    public void differentObjectsBothNull() {
        TestObj2 object2 = null;
        assertTrue(ObjectCmp.equals(
                null,
                object2,
                EqPair.of(TestObj::getIntField, t -> t.getLongField().intValue())));
    }

    @Test
    public void differentObjectsOneNull() {
        assertFalse(ObjectCmp.equals(
                null,
                new TestObj2("1", 1L),
                EqPair.of(TestObj::getIntField, t -> t.getLongField().intValue())));
    }
}
