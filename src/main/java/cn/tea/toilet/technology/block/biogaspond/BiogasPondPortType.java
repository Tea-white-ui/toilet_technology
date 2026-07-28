package cn.tea.toilet.technology.block.biogaspond;

public enum BiogasPondPortType {
    GAS_OUTPUT,
    ITEM_INPUT,
    FLUID_INPUT,
    ITEM_OUTPUT;

    public boolean allowsCapabilityFrom(boolean queriedFromTop, boolean queriedFromFront) {
        return this == GAS_OUTPUT ? queriedFromTop : queriedFromFront;
    }

    public boolean allowsInsertion() {
        return this == ITEM_INPUT || this == FLUID_INPUT;
    }

    public boolean allowsExtraction() {
        return this == ITEM_OUTPUT || this == GAS_OUTPUT;
    }
}
