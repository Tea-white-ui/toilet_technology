package cn.tea.toilet.technology.item;

import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BiogasResidueItemTest {
    @Test
    void behavesAsBoneMealWhenUsedOnGrowableBlocks() {
        assertEquals(BoneMealItem.class, BiogasResidueItem.class.getSuperclass());
    }
}
