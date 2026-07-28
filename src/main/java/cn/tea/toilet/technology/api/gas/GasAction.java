package cn.tea.toilet.technology.api.gas;

/**
 * Controls whether a gas operation mutates its target.
 *
 * <p>{@link #SIMULATE} must not modify handlers, item NBT, block entities, world state, or caches.
 * {@link #EXECUTE} commits a previously simulated operation.</p>
 */
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
