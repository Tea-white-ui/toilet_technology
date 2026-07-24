package cn.tea.toilet.technology.gui.biogaspond;

final class BiogasPondPressureDisplay {
    static final int X = 9;
    static final int WIDTH = 4;
    static final int EMPTY_Y = 66;
    static final int FULL_Y = 32;
    static final int BASELINE_START_Y = 67;
    static final int BASELINE_END_Y = 73;
    static final int BASELINE_COLOR = 0xFF00FF00;

    private BiogasPondPressureDisplay() {
    }

    static int fillTop(long amount, long capacity) {
        int fillHeight = BiogasPondChemicalDisplay.fillHeight(amount, capacity, EMPTY_Y - FULL_Y + 1);
        return EMPTY_Y - fillHeight + 1;
    }

    static int colorAt(int y) {
        float progress = (float) (EMPTY_Y - y) / (EMPTY_Y - FULL_Y);
        float red = Math.min(1.0F, 2.0F * progress);
        float green = 1.0F - 0.25F * progress;
        return 0xFF000000 | ((int) (red * 255.0F) << 16) | ((int) (green * 255.0F) << 8);
    }
}
