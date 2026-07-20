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

public class ModFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, ToiletTechnology.MOD_ID);

    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(BuiltInRegistries.FLUID, ToiletTechnology.MOD_ID);

    // 设定液体类型
    public static final DeferredHolder<FluidType, FluidType> FECES_LIQUID_TYPE = FLUID_TYPES.register("feces_liquid",
            () -> new BaseSewageFluidType(FluidType.Properties.create()
                    .density(1100)
                    .viscosity(1500)
                    .canSwim(false)
            ));

    // 设定液体静态状态
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> FECES_LIQUID = FLUIDS.register("feces_liquid",
            () -> new BaseFlowingFluid.Source(ModFluids.FECES_LIQUID_PROPERTIES));

    // 设定液体流动状态
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FECES_LIQUID_FLOWING = FLUIDS.register("feces_liquid_flowing",
            () -> new BaseFlowingFluid.Flowing(ModFluids.FECES_LIQUID_PROPERTIES));

    // 注册液体
    public static final BaseFlowingFluid.Properties FECES_LIQUID_PROPERTIES = new BaseFlowingFluid.Properties(
            FECES_LIQUID_TYPE, FECES_LIQUID, FECES_LIQUID_FLOWING)
            .block(ModBlocks.FECES_LIQUID_BLOCK)
            .bucket(ModItems.FECES_LIQUID_BUCKET);

    public static void register(IEventBus bus) {
        FLUID_TYPES.register(bus);
        FLUIDS.register(bus);
    }
}
