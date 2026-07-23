package cn.tea.toilet.technology.block.septictank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SepticTankChemicalAccessTest {

    @Test
    void chemicalTransfersRequireAValidMultiblock() {
        assertFalse(SepticTankChemicalAccess.allowsTransfer(false));
        assertTrue(SepticTankChemicalAccess.allowsTransfer(true));
    }
}
