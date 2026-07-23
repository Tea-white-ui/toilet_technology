package cn.tea.toilet.technology.block.septictank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SepticTankSlotPolicyTest {

    @Test
    void machineCanInsertIntoEveryOutputSlot() {
        assertTrue(SepticTankSlotPolicy.isMachineOutput(3));
        assertTrue(SepticTankSlotPolicy.isMachineOutput(4));
        assertTrue(SepticTankSlotPolicy.isMachineOutput(5));
        assertTrue(SepticTankSlotPolicy.isMachineOutput(7));
    }

    @Test
    void inputSlotsAreNotMachineOutputs() {
        assertFalse(SepticTankSlotPolicy.isMachineOutput(0));
        assertFalse(SepticTankSlotPolicy.isMachineOutput(1));
        assertFalse(SepticTankSlotPolicy.isMachineOutput(2));
        assertFalse(SepticTankSlotPolicy.isMachineOutput(6));
    }

    @Test
    void externalInsertionNeverTargetsOutputSlots() {
        assertFalse(SepticTankSlotPolicy.allowsExternalInsertion(3));
        assertFalse(SepticTankSlotPolicy.allowsExternalInsertion(4));
        assertFalse(SepticTankSlotPolicy.allowsExternalInsertion(5));
        assertFalse(SepticTankSlotPolicy.allowsExternalInsertion(7));
        assertTrue(SepticTankSlotPolicy.allowsExternalInsertion(0));
        assertTrue(SepticTankSlotPolicy.allowsExternalInsertion(6));
    }
}
