package cn.tea.toilet.technology.chemical;

import cn.tea.toilet.technology.ToiletTechnology;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalBuilder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registers the chemicals produced and consumed by Toilet Technology machines. */
public final class ModChemicals {
    /** Muted yellow-green tint matching the appearance of raw, methane-rich biogas. */
    public static final int BIOGAS_TINT = 0x87965B;
    public static final ResourceLocation BIOGAS_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ToiletTechnology.MOD_ID, "block/biogas");

    public static final DeferredRegister<Chemical> CHEMICALS =
            DeferredRegister.create(MekanismAPI.CHEMICAL_REGISTRY_NAME, ToiletTechnology.MOD_ID);

    public static final DeferredHolder<Chemical, Chemical> BIOGAS = CHEMICALS.register("biogas",
            () -> new Chemical(ChemicalBuilder.builder(BIOGAS_TEXTURE).tint(BIOGAS_TINT)));

    private ModChemicals() {
    }

    public static void register(IEventBus eventBus) {
        CHEMICALS.register(eventBus);
    }
}
