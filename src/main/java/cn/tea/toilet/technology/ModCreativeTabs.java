package cn.tea.toilet.technology;

import cn.tea.toilet.technology.item.ModItems;
import cn.tea.toilet.technology.gas.GasRegistry;
import cn.tea.toilet.technology.gas.GasStack;
import cn.tea.toilet.technology.item.GasTankItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * 创造模式标签页注册类
 * 负责注册模组的创造模式物品栏标签页
 * 所有模组物品按类别展示在统一的标签页中
 */
public class ModCreativeTabs {

    /** 创造模式标签页延迟注册表 */
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(
            Registries.CREATIVE_MODE_TAB,
            ModConstants.MOD_ID
    );

    /** ce标签页：包含所有模组物品，使用粪便作为图标 */
    public static final Supplier<CreativeModeTab> TOILET_TAB = CREATIVE_TABS.register("toilet_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.toilet_technology"))
                    .icon(() -> ModItems.FECES.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        // === 材料 ===
                        output.accept(ModItems.FECES.get());
                        output.accept(ModItems.DRIED_FECES.get());
                        output.accept(ModItems.FECES_BALL.get());
                        output.accept(ModItems.BIOGAS_RESIDUE.get());
                        // === 方块物品 ===
                        output.accept(ModItems.FECES_BLOCK_ITEM.get());
                        output.accept(ModItems.DRIED_FECES_BLOCK_ITEM.get());
                        output.accept(ModItems.ANTISEPTIC_BRICK_ITEM.get());
                        output.accept(ModItems.ANTISEPTIC_BRICK_STAIRS_ITEM.get());
                        output.accept(ModItems.ANTISEPTIC_BRICK_SLAB_ITEM.get());
                        output.accept(ModItems.ANTISEPTIC_BRICK_WALL_ITEM.get());
                        output.accept(ModItems.SQUAT_TOILET_ITEM.get());
                        output.accept(ModItems.OAK_TOILET_ITEM.get());
                        output.accept(ModItems.STONE_TOILET_ITEM.get());
                        output.accept(ModItems.IRON_TOILET_ITEM.get());
                        output.accept(ModItems.GOLD_TOILET_ITEM.get());
                        output.accept(ModItems.DIAMOND_TOILET_ITEM.get());
                        output.accept(ModItems.NETHERITE_TOILET_ITEM.get());
                        output.accept(ModItems.FECES_LIQUID_BUCKET.get());
                        output.accept(ModItems.WASTEWATER_BUCKET.get());
                        output.accept(ModItems.GAS_TANK.get());
                        output.accept(GasTankItem.createFilledStack(new GasStack(GasRegistry.BIOGAS, GasTankItem.CAPACITY)));
                        output.accept(GasTankItem.createFilledStack(new GasStack(GasRegistry.METHANE, GasTankItem.CAPACITY)));
                        output.accept(ModItems.DRYING_RACK_ITEM.get());
                        output.accept(ModItems.DRYING_BOX_ITEM.get());
                        output.accept(ModItems.SEPTIC_TANK_CONTROLLER_ITEM.get());
                        output.accept(ModItems.SEPTIC_TANK_WALL_ITEM.get());
                        output.accept(ModItems.SEPTIC_TANK_LIQUID_INPUT_PORT_ITEM.get());
                        output.accept(ModItems.SEPTIC_TANK_GAS_VALVE_ITEM.get());
                        output.accept(ModItems.BIOGAS_POND_CONTROLLER_ITEM.get());
                        output.accept(ModItems.BIOGAS_POND_WALL_ITEM.get());
                        output.accept(ModItems.BIOGAS_POND_GAS_VALVE_ITEM.get());
                        output.accept(ModItems.BIOGAS_POND_ITEM_INPUT_PORT_ITEM.get());
                        output.accept(ModItems.BIOGAS_POND_FLUID_INPUT_PORT_ITEM.get());
                        output.accept(ModItems.BIOGAS_POND_ITEM_OUTPUT_PORT_ITEM.get());
                        output.accept(ModItems.BIOGAS_GENERATOR_ITEM.get());
                        output.accept(ModItems.SEWAGE_PURIFIER_ITEM.get());
                        output.accept(ModItems.ABSORPTION_TOWER_BOTTOM_ITEM.get());
                        output.accept(ModItems.ABSORPTION_TOWER_BODY_ITEM.get());
                        output.accept(ModItems.SEALING_COMPONENT.get());
                        output.accept(ModItems.METAL_MESH.get());
                        output.accept(ModItems.MULTI_LAYER_SINTERED_METAL_MESH.get());

                    })
                    .build());

    /**
     * 注册所有创造模式标签页到事件总线
     * 
     * @param bus NeoForge 事件总线
     */
    public static void register(IEventBus bus) {
        CREATIVE_TABS.register(bus);
    }
}