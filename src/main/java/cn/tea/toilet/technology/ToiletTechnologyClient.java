package cn.tea.toilet.technology;

import cn.tea.toilet.technology.fluid.BaseSewageFluidType;
import cn.tea.toilet.technology.fluid.ModFluids;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
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
    }

    @SubscribeEvent
    static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(new IClientFluidTypeExtensions() {
            private static final Vector3f FOG_COLOR = new Vector3f(0.35f, 0.25f, 0.1f);

            @Override
            public ResourceLocation getStillTexture() {
                return BaseSewageFluidType.STILL_TEXTURE;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return BaseSewageFluidType.FLOWING_TEXTURE;
            }

            @Override
            public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
                                           int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return FOG_COLOR;
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance,
                                        float partialTick, float nearDistance, float farDistance, FogShape shape) {
                RenderSystem.setShaderFogStart(1f);
                RenderSystem.setShaderFogEnd(6f);
            }
        }, ModFluids.FECES_LIQUID_TYPE.get());
    }
}
