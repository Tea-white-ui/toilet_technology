package cn.tea.toilet.technology.gas;

import net.minecraft.network.chat.Component;

public record GasStack(Gas gas, long amount) {
    public static final GasStack EMPTY = new GasStack(null, 0);

    public GasStack {
        if (gas == null || amount <= 0) {
            gas = null;
            amount = 0;
        }
    }

    public boolean isEmpty() {
        return gas == null || amount <= 0;
    }

    public long getAmount() { return amount; }
    public Gas getGas() { return gas; }

    public boolean is(Gas candidate) {
        return !isEmpty() && gas.equals(candidate);
    }

    public GasStack copyWithAmount(long newAmount) {
        return new GasStack(gas, newAmount);
    }

    public Component getTextComponent() {
        return isEmpty() ? Component.empty() : gas.displayName();
    }
}