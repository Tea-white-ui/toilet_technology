package cn.tea.toilet.technology.block.automation;

import java.util.Objects;
import net.neoforged.neoforge.energy.IEnergyStorage;

/** Transfers energy between storages without loss by simulating both endpoints before execution. */
public final class EnergyTransfer {
    private EnergyTransfer() {
    }

    public static int transfer(IEnergyStorage source, IEnergyStorage destination, int limit) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(destination, "destination");
        if (limit <= 0) {
            return 0;
        }

        int offered = source.extractEnergy(limit, true);
        if (offered <= 0) {
            return 0;
        }

        int accepted = destination.receiveEnergy(offered, true);
        if (accepted <= 0) {
            return 0;
        }

        int extracted = source.extractEnergy(accepted, false);
        if (extracted <= 0) {
            return 0;
        }

        int received = destination.receiveEnergy(extracted, false);
        if (received != extracted) {
            throw new IllegalStateException("Energy storage changed between simulation and execution");
        }
        return extracted;
    }
}
