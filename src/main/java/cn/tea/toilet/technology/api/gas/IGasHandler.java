package cn.tea.toilet.technology.api.gas;

/**
 * Capability contract for gas storage and transfer.
 *
 * <p>Methods receiving {@link GasAction#SIMULATE} must not mutate state. Implementations must
 * return {@link GasStack#EMPTY}, {@code 0}, or {@code false} for invalid tank indexes rather than
 * returning null or throwing an index exception.</p>
 */
public interface IGasHandler {
    int getGasTanks();

    GasStack getGasInTank(int tank);

    void setGasInTank(int tank, GasStack stack);

    long getGasTankCapacity(int tank);

    boolean isValid(int tank, GasStack stack);

    GasStack insertGas(int tank, GasStack stack, GasAction action);

    GasStack extractGas(int tank, long amount, GasAction action);
}
