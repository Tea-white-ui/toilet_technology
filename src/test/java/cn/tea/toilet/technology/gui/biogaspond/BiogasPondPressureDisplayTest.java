package cn.tea.toilet.technology.gui.biogaspond;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BiogasPondPressureDisplayTest {
    @Test
    void alignsTheGaugeWithTheFourPixelWideTextureSlot() {
        assertEquals(9, BiogasPondPressureDisplay.X);
        assertEquals(4, BiogasPondPressureDisplay.WIDTH);
    }

    @Test
    void definesTheGreenBaselineBelowTheDynamicGauge() {
        assertEquals(67, BiogasPondPressureDisplay.BASELINE_START_Y);
        assertEquals(73, BiogasPondPressureDisplay.BASELINE_END_Y);
        assertEquals(0xFF00FF00, BiogasPondPressureDisplay.BASELINE_COLOR);
    }

    @Test
    void keepsAnEmptyGaugeUnfilled() {
        assertEquals(BiogasPondPressureDisplay.EMPTY_Y + 1,
                BiogasPondPressureDisplay.fillTop(0, 128_000));
    }

    @Test
    void fillsFromTheConfiguredBaselineToTheConfiguredFullHeight() {
        assertEquals(50, BiogasPondPressureDisplay.fillTop(64_000, 128_000));
        assertEquals(BiogasPondPressureDisplay.FULL_Y,
                BiogasPondPressureDisplay.fillTop(128_000, 128_000));
    }

    @Test
    void shiftsFromGreenToYellowOrangeWithoutReachingRed() {
        assertEquals(0xFF00FF00, BiogasPondPressureDisplay.colorAt(BiogasPondPressureDisplay.EMPTY_Y));
        assertEquals(0xFFFFBF00, BiogasPondPressureDisplay.colorAt(BiogasPondPressureDisplay.FULL_Y));
    }
}