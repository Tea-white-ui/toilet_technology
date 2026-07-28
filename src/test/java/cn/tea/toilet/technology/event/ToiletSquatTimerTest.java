package cn.tea.toilet.technology.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToiletSquatTimerTest {
    @Test
    void triggersOnlyAtTheConfiguredIntervalAndResetsAfterTriggering() {
        ToiletSquatTimer timer = new ToiletSquatTimer(20);

        assertFalse(timer.advance(18));
        assertTrue(timer.advance(19));
        assertEquals(0, timer.nextCount(19));
    }

    @Test
    void clampsIntervalsToAtLeastOneTick() {
        ToiletSquatTimer timer = new ToiletSquatTimer(20);

        assertEquals(0, timer.nextCount(0, 0));
    }
}
