package cn.tea.toilet.technology;
import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.event.PlayerToiletHandler;
import cn.tea.toilet.technology.item.ModItems;
import cn.tea.toilet.technology.sound.ModSounds;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;


@Mod(ToiletTechnology.MOD_ID)
public class ToiletTechnology {

    public static final String MOD_ID = "toilet_technology";

    public static final Logger LOGGER = LogUtils.getLogger();


    public ToiletTechnology(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerCapabilities);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModSounds.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new PlayerToiletHandler());
        // 报名参加我们感兴趣的服务器及其他游戏活动。
        //注意，当且仅当我们希望*这个*类（ToiletTechnology）直接响应事件时，才需要这样做。
        //如果该类中没有带@SubscribeEvent注释的函数，比如下面的 onServerStarting（），请不要添加这行。
        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.PREMIUM_TOILET.get(),
                (blockEntity, side) -> blockEntity.fluidTank
        );
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // 日志初始化
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }


    // 服务器初始化
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

        LOGGER.info("HELLO from server starting");
    }
}
