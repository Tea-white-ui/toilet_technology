package cn.tea.toilet.technology.gas;

import java.util.Objects;

/**
 * Transfers gas between handlers without loss by simulating both endpoints before executing either.
 */
public final class GasTransfer {
    private GasTransfer() {
    }

    public static GasStack transfer(IGasHandler source, int sourceTank, IGasHandler destination, int destinationTank,
                                    long limit) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(destination, "destination");
        if (limit <= 0 || sourceTank < 0 || sourceTank >= source.getGasTanks()
                || destinationTank < 0 || destinationTank >= destination.getGasTanks()) {
            return GasStack.EMPTY;
        }

        GasStack offered = source.extractGas(sourceTank, limit, GasAction.SIMULATE);
        if (offered.isEmpty()) return GasStack.EMPTY;

        GasStack remainder = destination.insertGas(destinationTank, offered, GasAction.SIMULATE);
        long accepted = offered.amount() - remainder.amount();
        if (accepted <= 0) return GasStack.EMPTY;

        GasStack extracted = source.extractGas(sourceTank, accepted, GasAction.EXECUTE);
        if (extracted.isEmpty()) return GasStack.EMPTY;

        GasStack executionRemainder = destination.insertGas(destinationTank, extracted, GasAction.EXECUTE);
        long transferred = extracted.amount() - executionRemainder.amount();
        if (transferred != extracted.amount()) {
            throw new IllegalStateException("Gas handler changed between simulation and execution");
        }
        return extracted;
    }
}
