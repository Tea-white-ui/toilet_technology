package cn.tea.toilet.technology.compat.jei;

import cn.tea.toilet.technology.block.absorptiontower.AbsorptionTowerStructure;

final class AbsorptionTowerJeiLayout {
    static final int WIDTH = 176;
    static final int HEIGHT = 96;
    static final int STRUCTURE_BLOCK_COUNT = AbsorptionTowerStructure.HEIGHT;
    static final int BODY_BLOCK_COUNT = STRUCTURE_BLOCK_COUNT - 1;
    static final int BOTTOM_BLOCK_COUNT = 1;

    static final int GAS_INPUT_X = 2;
    static final int GAS_INPUT_Y = 8;
    static final int FLUID_INPUT_X = 2;
    static final int FLUID_INPUT_Y = 42;
    static final int ARROW_X = 40;
    static final int ARROW_Y = 25;
    static final int GAS_OUTPUT_X = 76;
    static final int GAS_OUTPUT_Y = 8;
    static final int FLUID_OUTPUT_X = 76;
    static final int FLUID_OUTPUT_Y = 42;
    static final int STRUCTURE_X = 142;
    static final int STRUCTURE_TOP_Y = 17;
    static final int STRUCTURE_BLOCK_SPACING = 16;

    private AbsorptionTowerJeiLayout() {
    }
}
