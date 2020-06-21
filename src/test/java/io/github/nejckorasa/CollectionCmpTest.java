package io.github.nejckorasa;

import org.junit.jupiter.api.Test;

import java.util.List;

import static io.github.nejckorasa.result.Diff.ADDED;
import static io.github.nejckorasa.result.Diff.REMOVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollectionCmpTest {

    @Test
    public void compareSameDefaultEqualsNoDifference() {
        var baseList = List.of(
                new TestObj("1", 1),
                new TestObj("2", 2),
                new TestObj("3", 3)
        );
        var workingList = List.of(
                new TestObj("1", 1),
                new TestObj("2", 2),
                new TestObj("3", 3)
        );

        var compareResult = CollectionCmp
                .of(baseList, workingList, TestObj::getStrField)
                .compare();

        assertEquals(0, compareResult.getChangesCount());
    }

    @Test
    public void compareSameEqualitiesNoDifference() {
        var baseList = List.of(
                new TestObj("1", 9),
                new TestObj("2", 9),
                new TestObj("3", 9)
        );
        var workingList = List.of(
                new TestObj("1", 8),
                new TestObj("2", 8),
                new TestObj("3", 8)
        );

        var compareResult = CollectionCmp
                .of(baseList, workingList, TestObj::getStrField)
                .compare(TestObj::getStrField);

        assertEquals(0, compareResult.getChangesCount());
    }

    @Test
    public void compareSameMultipleEqualitiesNoDifference() {
        var baseList = List.of(
                new TestObj("1", 9),
                new TestObj("2", 9),
                new TestObj("3", 9)
        );
        var workingList = List.of(
                new TestObj("1", 9),
                new TestObj("2", 9),
                new TestObj("3", 9)
        );

        var compareResult = CollectionCmp
                .of(baseList, workingList, TestObj::getStrField)
                .compare(TestObj::getStrField, TestObj::getIntField);

        assertEquals(0, compareResult.getChangesCount());
    }

    @Test
    public void compareSameEqualsNoDifference() {
        var baseList = List.of(
                new TestObj("1", 9),
                new TestObj("2", 9),
                new TestObj("3", 9)
        );
        var workingList = List.of(
                new TestObj("1", 9),
                new TestObj("2", 9),
                new TestObj("3", 9)
        );

        var compareResult = CollectionCmp
                .of(baseList, workingList, TestObj::getStrField)
                .compare((b, w) -> b.getStrField().equals(w.getStrField()));

        assertEquals(0, compareResult.getChangesCount());
    }

    @Test
    public void compareSameEqualitiesDifferenceAndUpdates() {
        var baseList = List.of(
                new TestObj("1", 1),
                new TestObj("2", 2),
                new TestObj("3", 3)
        );
        var workingList = List.of(
                new TestObj("2", 9),
                new TestObj("3", 3),
                new TestObj("4", 4)
        );

        var compareResult = CollectionCmp
                .of(baseList, workingList, TestObj::getStrField)
                .compare(TestObj::getStrField, TestObj::getIntField);

        assertEquals(3, compareResult.getChangesCount());

        assertEquals(1, compareResult.getRemoved().size());
        assertEquals("1", compareResult.getRemoved().get(0).getKey());
        assertEquals(REMOVED, compareResult.getRemoved().get(0).getDiff());
        assertEquals(new TestObj("1", 1), compareResult.getRemoved().get(0).getBase());
        assertTrue(compareResult.getRemoved().stream().allMatch(r -> r.getWorking() == null));

        assertEquals(1, compareResult.getAdded().size());
        assertEquals("4", compareResult.getAdded().get(0).getKey());
        assertEquals(ADDED, compareResult.getAdded().get(0).getDiff());
        assertEquals(new TestObj("4", 4), compareResult.getAdded().get(0).getWorking());
        assertTrue(compareResult.getAdded().stream().allMatch(r -> r.getBase() == null));

        assertEquals(2, compareResult.getDifferentCount());
        assertTrue(compareResult.getDifferent()
                .stream()
                .anyMatch(d -> d.getWorking() != null && d.getWorking().equals(new TestObj("4", 4)) && d.getDiff() == ADDED));
        assertTrue(compareResult.getDifferent()
                .stream()
                .anyMatch(d -> d.getBase() != null && d.getBase().equals(new TestObj("1", 1)) && d.getDiff() == REMOVED));
    }

    @Test
    public void compareDifferentWithEqualsDifference() {
        var baseList = List.of(
                new TestObj("1", 1),
                new TestObj("2", 2),
                new TestObj("3", 3)
        );
        final List<TestObj2> workingList = List.of(
                new TestObj2("2", 2L),
                new TestObj2("3", 30L),
                new TestObj2("4", 4L)
        );

        var compareResult = CollectionCmp
                .of(baseList, workingList, TestObj::getStrField, TestObj2::getStrField)
                .compare((o1, o2) -> o1.getIntField() == o2.getLongField().intValue());

        assertEquals(3, compareResult.getChangesCount());

        assertEquals(1, compareResult.getRemoved().size());
        assertEquals("1", compareResult.getRemoved().get(0).getKey());
        assertEquals(REMOVED, compareResult.getRemoved().get(0).getDiff());
        assertEquals(new TestObj("1", 1), compareResult.getRemoved().get(0).getBase());
        assertTrue(compareResult.getRemoved().stream().allMatch(r -> r.getWorking() == null));

        assertEquals(1, compareResult.getAdded().size());
        assertEquals("4", compareResult.getAdded().get(0).getKey());
        assertEquals(ADDED, compareResult.getAdded().get(0).getDiff());
        assertEquals(new TestObj2("4", 4L), compareResult.getAdded().get(0).getWorking());
        assertTrue(compareResult.getAdded().stream().allMatch(r -> r.getBase() == null));

        assertEquals(2, compareResult.getDifferentCount());
    }

    @Test
    public void compareDifferentMultipleEqualitiesDifference() {
        var baseList = List.of(
                new TestObj("1", 1),
                new TestObj("2", 2),
                new TestObj("3", 3)
        );
        var workingList = List.of(
                new TestObj2("2", 2L),
                new TestObj2("3", 30L),
                new TestObj2("4", 4L)
        );

        var compareResult = CollectionCmp
                .of(baseList, workingList, TestObj::getStrField, TestObj2::getStrField)
                .compare(
                        EqPair.of(TestObj::getIntField, o2 -> o2.getLongField().intValue()),
                        EqPair.of(TestObj::getStrField, TestObj2::getStrField)
                );

        assertEquals(3, compareResult.getChangesCount());

        assertEquals(1, compareResult.getRemoved().size());
        assertEquals("1", compareResult.getRemoved().get(0).getKey());
        assertEquals(REMOVED, compareResult.getRemoved().get(0).getDiff());
        assertEquals(new TestObj("1", 1), compareResult.getRemoved().get(0).getBase());
        assertTrue(compareResult.getRemoved().stream().allMatch(r -> r.getWorking() == null));

        assertEquals(1, compareResult.getAdded().size());
        assertEquals("4", compareResult.getAdded().get(0).getKey());
        assertEquals(ADDED, compareResult.getAdded().get(0).getDiff());
        assertEquals(new TestObj2("4", 4L), compareResult.getAdded().get(0).getWorking());
        assertTrue(compareResult.getAdded().stream().allMatch(r -> r.getBase() == null));

        assertEquals(2, compareResult.getDifferentCount());
    }

    @Test
    public void compareDifferentEqualitiesDifference() {
        var baseList = List.of(
                new TestObj("1", 1),
                new TestObj("2", 2),
                new TestObj("3", 3)
        );
        var workingList = List.of(
                new TestObj2("2", 2L),
                new TestObj2("3", 30L),
                new TestObj2("4", 4L)
        );

        var compareResult = CollectionCmp
                .of(baseList, workingList, TestObj::getStrField, TestObj2::getStrField)
                .compare(EqPair.of(TestObj::getIntField, o2 -> o2.getLongField().intValue()));

        assertEquals(3, compareResult.getChangesCount());

        assertEquals(1, compareResult.getRemoved().size());
        assertEquals("1", compareResult.getRemoved().get(0).getKey());
        assertEquals(REMOVED, compareResult.getRemoved().get(0).getDiff());
        assertEquals(new TestObj("1", 1), compareResult.getRemoved().get(0).getBase());
        assertTrue(compareResult.getRemoved().stream().allMatch(r -> r.getWorking() == null));

        assertEquals(1, compareResult.getAdded().size());
        assertEquals("4", compareResult.getAdded().get(0).getKey());
        assertEquals(ADDED, compareResult.getAdded().get(0).getDiff());
        assertEquals(new TestObj2("4", 4L), compareResult.getAdded().get(0).getWorking());
        assertTrue(compareResult.getAdded().stream().allMatch(r -> r.getBase() == null));

        assertEquals(2, compareResult.getDifferentCount());
    }
}
