package cn.tea.toilet.technology.fluid;

import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * 流体注册类
 * 负责注册模组中的所有流体类型和流体实例，包括：
 * - 粪便液体类型（密度、粘度等物理属性）
 * - 粪便液体源方块和流动方块
 * - 流体属性配置（关联方块和桶物品）
 */
public class ModFluids {
    /** 流体类型延迟注册表：定义流体的物理属性 */
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, ToiletTechnology.MOD_ID);

    /** 流体延迟注册表：定义流体实例 */
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(BuiltInRegistries.FLUID, ToiletTechnology.MOD_ID);

    /** 粪便液体类型：密度1100（比水重），粘度1500（较粘稠），不可游泳 */
    public static final DeferredHolder<FluidType, FluidType> FECES_LIQUID_TYPE = FLUID_TYPES.register("feces_liquid",
            () -> new BaseSewageFluidType(FluidType.Properties.create()
                    .density(1100)
                    .viscosity(1500)
                    .canSwim(false)
            ));

    /** 粪便液体源方块：静止状态的流体 */
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> FECES_LIQUID = FLUIDS.register("feces_liquid",
            () -> new BaseFlowingFluid.Source(ModFluids.FECES_LIQUID_PROPERTIES));

    /** 粪便液体流动方块：流动状态的流体 */
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FECES_LIQUID_FLOWING = FLUIDS.register("feces_liquid_flowing",
            () -> new BaseFlowingFluid.Flowing(ModFluids.FECES_LIQUID_PROPERTIES));

    /** 流体属性配置：关联流体类型、源方块、流动方块、方块实例和桶物品 */
    public static final BaseFlowingFluid.Properties FECES_LIQUID_PROPERTIES = new BaseFlowingFluid.Properties(
            FECES_LIQUID_TYPE, FECES_LIQUID, FECES_LIQUID_FLOWING)
            .block(ModBlocks.FECES_LIQUID_BLOCK)
            .bucket(ModItems.FECES_LIQUID_BUCKET);

    /**
     * 注册所有流体类型和流体实例到事件总线
     * 
     * @param bus NeoForge 事件总线
     */
    public static void register(IEventBus bus) {
        FLUID_TYPES.register(bus);
        FLUIDS.register(bus);
    }
}