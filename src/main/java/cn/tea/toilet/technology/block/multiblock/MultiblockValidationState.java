package cn.tea.toilet.technology.block.multiblock;

/**
 * Tracks a fixed multiblock's validated state and bounded revalidation schedule.
 */
public final class MultiblockValidationState {
    private final int validationInterval;
    private boolean valid;
    private int remainingTicks = 1;

    public MultiblockValidationState(int validationInterval) {
        if (validationInterval <= 0) {
            throw new IllegalArgumentException("Validation interval must be positive");
        }
        this.validationInterval = validationInterval;
    }

    public boolean isValid() {
        return valid;
    }

    /**
     * Advances the fallback validation schedule.
     *
     * @return true when the controller should validate its world structure now.
     */
    public boolean tickAndShouldValidate() {
        if (--remainingTicks > 0) {
            return false;
        }
        remainingTicks = validationInterval;
        return true;
    }

    /**
     * Stores a validation result.
     *
     * @return true only when the validity state changed.
     */
    public boolean updateValidity(boolean valid) {
        if (this.valid == valid) {
            return false;
        }
        this.valid = valid;
        return true;
    }

    /** Resets persisted/transient state so the next server tick validates immediately. */
    public void reset() {
        valid = false;
        remainingTicks = 1;
    }
}
