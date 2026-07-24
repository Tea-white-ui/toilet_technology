package cn.tea.toilet.technology.gas;

public interface IGasHandler {
    int getGasTanks();
    GasStack getGasInTank(int tank);
    void setGasInTank(int tank, GasStack stack);
    long getGasTankCapacity(int tank);
    boolean isValid(int tank, GasStack stack);
    GasStack insertGas(int tank, GasStack stack, GasAction action);
    GasStack extractGas(int tank, long amount, GasAction action);
}