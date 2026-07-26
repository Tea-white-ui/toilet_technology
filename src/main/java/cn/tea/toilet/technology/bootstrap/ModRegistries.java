package cn.tea.toilet.technology.bootstrap;

import cn.tea.toilet.technology.ModAttachments;
import cn.tea.toilet.technology.ModCreativeTabs;
import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.entity.ModEntityTypes;
import cn.tea.toilet.technology.fluid.ModFluids;
import cn.tea.toilet.technology.gui.ModMenuTypes;
import cn.tea.toilet.technology.item.ModItems;
import cn.tea.toilet.technology.particle.ModParticles;
import cn.tea.toilet.technology.recipe.ModRecipeSerializers;
import cn.tea.toilet.technology.recipe.ModRecipeTypes;
import cn.tea.toilet.technology.sound.ModSounds;
import net.neoforged.bus.api.IEventBus;

/**
 * Attaches every mod-owned DeferredRegister to the mod event bus.
 */
public final class ModRegistries {
    private ModRegistries() {
    }

    public static void register(IEventBus modEventBus) {
        ModFluids.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntityTypes.register(modEventBus);
        ModItems.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModSounds.register(modEventBus);
        ModParticles.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
        ModAttachments.register(modEventBus);
    }
}
