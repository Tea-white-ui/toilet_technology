package cn.tea.toilet.technology.block.drying;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DryingRackProgressStateTest {
    @Test
    void resetsOnlyTheRequestedSlot() {
        DryingRackProgressState state = new DryingRackProgressState(4);
        state.setTotalTime(0, 20);
        state.advance(0);
        state.setTotalTime(1, 40);
        state.advance(1);

        assertTrue(state.reset(0));
        assertEquals(0, state.progress(0));
        assertEquals(0, state.totalTime(0));
        assertEquals(1, state.progress(1));
        assertEquals(40, state.totalTime(1));
        assertFalse(state.reset(0));
    }

    @Test
    void replacesProgressAndTotalTimeFromAValidatedNetworkSnapshot() {
        DryingRackProgressState state = new DryingRackProgressState(4);

        assertTrue(state.replace(new int[]{1, 2, 3, 4}, new int[]{10, 20, 30, 40}));
        assertArrayEquals(new int[]{1, 2, 3, 4}, state.progressValues());
        assertArrayEquals(new int[]{10, 20, 30, 40}, state.totalTimeValues());
        assertFalse(state.replace(new int[]{1}, new int[]{10}));
        assertArrayEquals(new int[]{1, 2, 3, 4}, state.progressValues());
    }
}
