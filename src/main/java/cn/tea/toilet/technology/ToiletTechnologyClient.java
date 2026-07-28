package cn.tea.toilet.technology;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.block.drying.DryingRackBlockEntityBER;
import cn.tea.toilet.technology.block.toilet.ToiletBlockEntityBER;
import cn.tea.toilet.technology.client.GasTankItemRenderer;
import cn.tea.toilet.technology.entity.ModEntityTypes;
import cn.tea.toilet.technology.fluid.BaseSewageFluidType;
import cn.tea.toilet.technology.fluid.ModFluids;
import cn.tea.toilet.technology.fluid.WastewaterFluidType;
import cn.tea.toilet.technology.gui.ModMenuTypes;
import cn.tea.toilet.technology.item.ModItems;
import cn.tea.toilet.technology.particle.DefecationCloudParticle;
import cn.tea.toilet.technology.particle.ModParticles;
import cn.tea.toilet.technology.gui.biogaspond.BiogasPondScreen;
import cn.tea.toilet.technology.gui.dryingbox.DryingBoxScreen;
import cn.tea.toilet.technology.gui.septictank.SepticTankScreen;
import cn.tea.toilet.technology.gui.absorptiontower.AbsorptionTowerScreen;
import cn.tea.toilet.technology.gui.gasmeltingfurnace.GasMeltingFurnaceScreen;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

@Mod(value = ModConstants.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = ModConstants.MOD_ID, value = Dist.CLIENT)
public class ToiletTechnologyClient {
    public ToiletTechnologyClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityRenderers.register(ModBlockEntities.PREMIUM_TOILET.get(), ToiletBlockEntityBER::new);
            BlockEntityRenderers.register(ModBlockEntities.NETHERITE_TOILET.get(), ToiletBlockEntityBER::new);
            BlockEntityRenderers.register(ModBlockEntities.DRYING_RACK.get(), DryingRackBlockEntityBER::new);
            EntityRenderers.register(ModEntityTypes.FECES_BALL.get(), ThrownItemRenderer::new);
        });
    }

    @SubscribeEvent
    static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.DEFECATION_CLOUD.get(), DefecationCloudParticle.Provider::new);
    }

    @SubscribeEvent
    static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.DRYING_BOX_MENU.get(), DryingBoxScreen::new);
        event.register(ModMenuTypes.SEPTIC_TANK_MENU.get(), SepticTankScreen::new);
        event.register(ModMenuTypes.BIOGAS_POND_MENU.get(), BiogasPondScreen::new);
        event.register(ModMenuTypes.ABSORPTION_TOWER_MENU.get(), AbsorptionTowerScreen::new);
        event.register(ModMenuTypes.GAS_MELTING_FURNACE_MENU.get(), GasMeltingFurnaceScreen::new);
    }

    @SubscribeEvent
    static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            private GasTankItemRenderer renderer;

            @Override
            public GasTankItemRenderer getCustomRenderer() {
                if (renderer == null) {
                    Minecraft minecraft = Minecraft.getInstance();
                    renderer = new GasTankItemRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
                }
                return renderer;
            }
        }, ModItems.GAS_TANK.get());
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
        event.registerFluidType(new IClientFluidTypeExtensions() {
            private static final Vector3f FOG_COLOR = new Vector3f(0.18f, 0.31f, 0.28f);

            @Override
            public @NotNull ResourceLocation getStillTexture() {
                return WastewaterFluidType.STILL_TEXTURE;
            }

            @Override
            public @NotNull ResourceLocation getFlowingTexture() {
                return WastewaterFluidType.FLOWING_TEXTURE;
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
                RenderSystem.setShaderFogEnd(8f);
            }
        }, ModFluids.WASTEWATER_TYPE.get());
    }
}
