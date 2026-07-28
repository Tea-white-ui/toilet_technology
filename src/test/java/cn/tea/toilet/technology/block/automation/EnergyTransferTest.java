package cn.tea.toilet.technology.block.automation;

import net.neoforged.neoforge.energy.EnergyStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnergyTransferTest {
    @Test
    void transfersOnlyEnergyAcceptedByTheDestination() {
        EnergyStorage source = new EnergyStorage(1_000, 0, 1_000) {
            {
                energy = 700;
            }
        };
        EnergyStorage destination = new EnergyStorage(500, 500, 0) {
            {
                energy = 300;
            }
        };

        int transferred = EnergyTransfer.transfer(source, destination, 1_000);

        assertEquals(200, transferred);
        assertEquals(500, source.getEnergyStored());
        assertEquals(500, destination.getEnergyStored());
    }

    @Test
    void leavesSourceUntouchedWhenDestinationCannotReceiveEnergy() {
        EnergyStorage source = new EnergyStorage(1_000, 0, 1_000) {
            {
                energy = 700;
            }
        };
        EnergyStorage destination = new EnergyStorage(1_000, 0, 0);

        int transferred = EnergyTransfer.transfer(source, destination, 1_000);

        assertEquals(0, transferred);
        assertEquals(700, source.getEnergyStored());
        assertEquals(0, destination.getEnergyStored());
    }
}
