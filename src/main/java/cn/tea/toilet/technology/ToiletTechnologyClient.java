package cn.tea.toilet.technology;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.block.drying.DryingRackBlockEntityBER;
import cn.tea.toilet.technology.block.toilet.PremiumToiletBlockEntityBER;
import cn.tea.toilet.technology.fluid.BaseSewageFluidType;
import cn.tea.toilet.technology.fluid.ModFluids;
import cn.tea.toilet.technology.gui.DryingBoxScreen;
import cn.tea.toilet.technology.gui.ModMenuTypes;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

@Mod(value = ToiletTechnology.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = ToiletTechnology.MOD_ID, value = Dist.CLIENT)
public class ToiletTechnologyClient {
    public ToiletTechnologyClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        ToiletTechnology.LOGGER.info("HELLO FROM CLIENT SETUP");
        ToiletTechnology.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

        // enqueueWork 确保在客户端主线程中执行渲染注册
        event.enqueueWork(() -> {
            BlockEntityRenderers.register(
                    ModBlockEntities.PREMIUM_TOILET.get(),
                    PremiumToiletBlockEntityBER::new
            );
            BlockEntityRenderers.register(
                    ModBlockEntities.DRYING_RACK.get(),
                    DryingRackBlockEntityBER::new
            );
        });
    }

    @SubscribeEvent
    static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.DRYING_BOX_MENU.get(), DryingBoxScreen::new);
    }

    @SubscribeEvent
    static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(new IClientFluidTypeExtensions() {
            private static final Vector3f FOG_COLOR = new Vector3f(0.35f, 0.25f, 0.1f);

            @Override
            public @NotNull ResourceLocation getStillTexture() {
                return BaseSewageFluidType.STILL_TEXTURE;
            }

            @Override
            public @NotNull ResourceLocation getFlowingTexture() {
                return BaseSewageFluidType.FLOWING_TEXTURE;
            }

            @Override
            public @NotNull Vector3f modifyFogColor(@NotNull Camera camera, float partialTick, @NotNull ClientLevel level,
                                                    int renderDistance, float darkenWorldAmount, @NotNull Vector3f fluidFogColor) {
                return FOG_COLOR;
            }

            @Override
            public void modifyFogRender(@NotNull Camera camera, FogRenderer.@NotNull FogMode mode, float renderDistance,
                                        float partialTick, float nearDistance, float farDistance, @NotNull FogShape shape) {
                RenderSystem.setShaderFogStart(1f);
                RenderSystem.setShaderFogEnd(6f);
            }
        }, ModFluids.FECES_LIQUID_TYPE.get());
    }
}