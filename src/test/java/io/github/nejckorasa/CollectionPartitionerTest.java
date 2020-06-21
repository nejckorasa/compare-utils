package io.github.nejckorasa;

import org.junit.jupiter.api.Test;

import java.util.List;

import static io.github.nejckorasa.CollectionPartitioner.buildPartition;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
public class CollectionPartitionerTest {

    @Test
    public void partitionSuccessful() {
        var baseList = List.of(
                new TestObj("1", 1),
                new TestObj("2", 2),
                new TestObj("3", 3)
        );

        var partitionMap = buildPartition(baseList, TestObj::getStrField);

        assertEquals(3, partitionMap.size());
    }

    @Test
    public void partitionUnsuccessful() {
        var baseList = List.of(
                new TestObj("1", 1),
                new TestObj("2", 2),
                new TestObj("2", 9)
        );

        var partitionMap = buildPartition(baseList, TestObj::getStrField);

        assertEquals(2, partitionMap.size());
        assertEquals(2, partitionMap.get("2").getIntField().intValue());
    }

    @Test
    public void canPartition() {
        var baseList = List.of(
                new TestObj("1", 1),
                new TestObj("2", 2),
                new TestObj("3", 3)
        );

        assertTrue(CollectionPartitioner.canPartition(baseList, TestObj::getStrField));
    }

    @Test
    public void cannotPartition() {
        var baseList = List.of(
                new TestObj("1", 1),
                new TestObj("2", 2),
                new TestObj("2", 9)
        );

        assertFalse(CollectionPartitioner.canPartition(baseList, TestObj::getStrField));
    }
}