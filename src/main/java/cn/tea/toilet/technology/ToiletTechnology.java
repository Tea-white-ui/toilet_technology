package cn.tea.toilet.technology;
import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.gas.GasCapabilities;
import cn.tea.toilet.technology.event.PlayerToiletHandler;
import cn.tea.toilet.technology.event.TankBatSpawnHandler;
import cn.tea.toilet.technology.fluid.ModFluids;
import cn.tea.toilet.technology.gui.ModMenuTypes;
import cn.tea.toilet.technology.item.ModItems;
import cn.tea.toilet.technology.network.ModNetworkRegistry;
import cn.tea.toilet.technology.recipe.ModRecipeSerializers;
import cn.tea.toilet.technology.recipe.ModRecipeTypes;
import cn.tea.toilet.technology.sound.ModSounds;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;


@Mod(ToiletTechnology.MOD_ID)
public class ToiletTechnology {

    public static final String MOD_ID = "toilet_technology";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static Logger getLOGGER(){
        return LOGGER;
    }


    public ToiletTechnology(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerCapabilities);
        // 注册网络包处理器
        modEventBus.addListener(this::registerPayloadHandlers);
        ModFluids.register(modEventBus);

        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModSounds.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
        ModAttachments.register(modEventBus);
        registerOptionalMekanismCompat(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new PlayerToiletHandler());
        NeoForge.EVENT_BUS.register(new TankBatSpawnHandler());

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private static void registerOptionalMekanismCompat(IEventBus modEventBus) {
        if (!ModList.get().isLoaded("mekanism")) return;
        try {
            Class<?> compat = Class.forName("cn.tea.toilet.technology.compat.mekanism.MekanismCompat");
            compat.getMethod("register", IEventBus.class).invoke(null, modEventBus);
        } catch (ReflectiveOperationException exception) {
            LOGGER.error("Failed to initialize optional Mekanism compatibility", exception);
        }
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.PREMIUM_TOILET.get(),
                (blockEntity, side) -> blockEntity.fluidTank
        );
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.NETHERITE_TOILET.get(),
                (blockEntity, side) -> blockEntity.fluidTank
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.DRYING_RACK.get(),
                (blockEntity, side) -> blockEntity.itemHandler
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.DRYING_BOX.get(),
                (blockEntity, side) -> blockEntity.getHopperHandler()
        );
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.SEPTIC_TANK_CONTROLLER.get(),
                (blockEntity, side) -> blockEntity.isStructureValid() ? blockEntity.getAutomationItems() : null);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntities.SEPTIC_TANK_CONTROLLER.get(),
                (blockEntity, side) -> blockEntity.isStructureValid() ? blockEntity.getAutomationFluids() : null);
        event.registerBlockEntity(GasCapabilities.BLOCK,
                ModBlockEntities.SEPTIC_TANK_CONTROLLER.get(),
                (blockEntity, side) -> blockEntity.isStructureValid() ? blockEntity.getAutomationGases() : null);
        event.registerBlockEntity(GasCapabilities.BLOCK, ModBlockEntities.BIOGAS_POND_PORT.get(),
                (blockEntity, side) -> blockEntity.getGasHandler(side));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntities.BIOGAS_POND_PORT.get(),
                (blockEntity, side) -> blockEntity.getFluidHandler(side));
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.BIOGAS_GENERATOR.get(),
                (blockEntity, side) -> blockEntity.energyStorage);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntities.SEWAGE_PURIFIER.get(),
                (blockEntity, side) -> blockEntity.getFluidHandler(side));
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.SEWAGE_PURIFIER.get(),
                (blockEntity, side) -> blockEntity.getEnergyStorage(side));
        event.registerItem(GasCapabilities.ITEM,
                (stack, ignored) -> ((cn.tea.toilet.technology.item.BiogasTankItem) stack.getItem())
                        .createGasHandler(stack),
                ModItems.BIOGAS_TANK.get());
    }

    /**
     * 注册网络包处理器
     * 使用 NeoForge 的新版网络系统，基于 CustomPacketPayload
     */
    private void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        // 使用版本号进行协议协商，版本不匹配的客户端/服务端将被拒绝连接
        var registrar = event.registrar("1.0.0");
        ModNetworkRegistry.register(registrar);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}