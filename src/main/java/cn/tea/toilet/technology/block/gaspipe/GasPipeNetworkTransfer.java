package cn.tea.toilet.technology.block.gaspipe;

import cn.tea.toilet.technology.gas.GasAction;
import cn.tea.toilet.technology.gas.GasStack;
import cn.tea.toilet.technology.gas.GasTransfer;
import cn.tea.toilet.technology.gas.IGasHandler;

import java.util.List;

/** Selects one stable source/destination pair for a whole pipe network. */
final class GasPipeNetworkTransfer {
    private GasPipeNetworkTransfer() {
    }

    static GasStack transferOne(List<IGasHandler> endpoints, long limit) {
        if (limit <= 0) return GasStack.EMPTY;
        for (int sourceIndex = 0; sourceIndex < endpoints.size(); sourceIndex++) {
            IGasHandler source = endpoints.get(sourceIndex);
            GasStack offered = source.extractGas(0, limit, GasAction.SIMULATE);
            if (offered.isEmpty()) continue;

            for (int destinationIndex = 0; destinationIndex < endpoints.size(); destinationIndex++) {
                if (sourceIndex == destinationIndex) continue;
                IGasHandler destination = endpoints.get(destinationIndex);
                GasStack remainder = destination.insertGas(0, offered, GasAction.SIMULATE);
                long accepted = offered.amount() - remainder.amount();
                if (accepted <= 0) continue;

                long transferLimit = Math.min(limit, accepted);
                if (isBidirectional(source, destination, offered)) {
                    transferLimit = Math.min(transferLimit, equalizationLimit(source, destination));
                }
                if (transferLimit <= 0) continue;
                return GasTransfer.transfer(source, 0, destination, 0, transferLimit);
            }
        }
        return GasStack.EMPTY;
    }

    static GasStack transferFrom(IGasHandler source, List<IGasHandler> destinations, long limit) {
        for (IGasHandler destination : destinations) {
            GasStack moved = GasTransfer.transfer(source, 0, destination, 0, limit);
            if (!moved.isEmpty()) return moved;
        }
        return GasStack.EMPTY;
    }

    private static boolean isBidirectional(IGasHandler source, IGasHandler destination, GasStack gas) {
        GasStack probe = gas.copyWithAmount(1);
        return source.isValid(0, probe) && destination.isValid(0, probe);
    }

    private static long equalizationLimit(IGasHandler source, IGasHandler destination) {
        long sourceCapacity = source.getGasTankCapacity(0);
        long destinationCapacity = destination.getGasTankCapacity(0);
        if (sourceCapacity <= 0 || destinationCapacity <= 0) return 0;

        long sourceAmount = source.getGasInTank(0).amount();
        long destinationAmount = destination.getGasInTank(0).amount();
        double numerator = (double) sourceAmount * destinationCapacity
                - (double) destinationAmount * sourceCapacity;
        if (numerator <= 0) return 0;
        return (long) Math.floor(numerator / (sourceCapacity + (double) destinationCapacity));
    }
}
