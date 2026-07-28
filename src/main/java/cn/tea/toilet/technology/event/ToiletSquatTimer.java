package cn.tea.toilet.technology.event;

/** Pure timer policy for a player's continuous crouching interaction with a toilet. */
final class ToiletSquatTimer {
    private final int defaultInterval;

    ToiletSquatTimer(int defaultInterval) {
        if (defaultInterval <= 0) {
            throw new IllegalArgumentException("Default interval must be positive");
        }
        this.defaultInterval = defaultInterval;
    }

    boolean advance(int currentCount) {
        return nextCount(currentCount, defaultInterval) == 0;
    }

    int nextCount(int currentCount) {
        return nextCount(currentCount, defaultInterval);
    }

    int nextCount(int currentCount, int interval) {
        int effectiveInterval = Math.max(1, interval);
        return Math.max(0, currentCount) + 1 >= effectiveInterval ? 0 : Math.max(0, currentCount) + 1;
    }
}
