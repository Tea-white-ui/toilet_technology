package cn.tea.toilet.technology.gas;

public enum GasAction {
    EXECUTE,
    SIMULATE;

    public boolean executes() {
        return this == EXECUTE;
    }

    public boolean execute() {
        return executes();
    }
}