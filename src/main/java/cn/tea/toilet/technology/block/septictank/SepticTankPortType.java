package cn.tea.toilet.technology.block.septictank;

public enum SepticTankPortType {
    LIQUID_INPUT,
    GAS_OUTPUT;

    public boolean allowsCapabilityFrom(boolean queriedFromTop, boolean queriedFromFront) {
        return this == GAS_OUTPUT ? queriedFromTop : queriedFromFront;
    }

    public boolean allowsInsertion() {
        return this == LIQUID_INPUT;
    }

    public boolean allowsExtraction() {
        return this == GAS_OUTPUT;
    }
}
