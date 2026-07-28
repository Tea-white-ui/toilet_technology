package cn.tea.toilet.technology.block.drying;

import java.util.Arrays;

/** Owns the fixed-size drying progress snapshot used by the rack ticker, persistence, and client sync. */
final class DryingRackProgressState {
    private final int[] progress;
    private final int[] totalTime;

    DryingRackProgressState(int slots) {
        if (slots <= 0) {
            throw new IllegalArgumentException("Slot count must be positive");
        }
        progress = new int[slots];
        totalTime = new int[slots];
    }

    int progress(int slot) {
        return progress[slot];
    }

    int totalTime(int slot) {
        return totalTime[slot];
    }

    void setProgress(int slot, int ticks) {
        progress[slot] = Math.max(0, ticks);
    }

    void setTotalTime(int slot, int ticks) {
        totalTime[slot] = Math.max(0, ticks);
    }

    void advance(int slot) {
        progress[slot]++;
    }

    boolean reset(int slot) {
        if (progress[slot] == 0 && totalTime[slot] == 0) {
            return false;
        }
        progress[slot] = 0;
        totalTime[slot] = 0;
        return true;
    }

    boolean replace(int[] progress, int[] totalTime) {
        if (progress.length != this.progress.length || totalTime.length != this.totalTime.length) {
            return false;
        }
        System.arraycopy(progress, 0, this.progress, 0, this.progress.length);
        System.arraycopy(totalTime, 0, this.totalTime, 0, this.totalTime.length);
        return true;
    }

    int[] progressValues() {
        return Arrays.copyOf(progress, progress.length);
    }

    int[] totalTimeValues() {
        return Arrays.copyOf(totalTime, totalTime.length);
    }
}
