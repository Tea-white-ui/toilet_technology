package cn.tea.toilet.technology.block.biogaspond;

public enum BiogasPondPortType {
    GAS_OUTPUT,
    ITEM_INPUT,
    FLUID_INPUT,
    ITEM_OUTPUT;

    public boolean allowsCapabilityFrom(boolean queriedFromTop, boolean queriedFromFront) {
        return this == GAS_OUTPUT ? queriedFromTop : queriedFromFront;
    }
}
