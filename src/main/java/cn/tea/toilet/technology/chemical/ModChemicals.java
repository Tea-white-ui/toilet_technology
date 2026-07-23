package cn.tea.toilet.technology.chemical;

import cn.tea.toilet.technology.ToiletTechnology;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalBuilder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registers the chemicals produced and consumed by Toilet Technology machines. */
public final class ModChemicals {
    public static final DeferredRegister<Chemical> CHEMICALS =
            DeferredRegister.create(MekanismAPI.CHEMICAL_REGISTRY_NAME, ToiletTechnology.MOD_ID);

    public static final DeferredHolder<Chemical, Chemical> BIOGAS = CHEMICALS.register("biogas",
            () -> new Chemical(ChemicalBuilder.builder().tint(0x8CA66B)));

    private ModChemicals() {
    }

    public static void register(IEventBus eventBus) {
        CHEMICALS.register(eventBus);
    }
}
