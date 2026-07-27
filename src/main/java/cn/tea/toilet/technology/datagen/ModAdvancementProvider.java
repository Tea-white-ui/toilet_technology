package cn.tea.toilet.technology.datagen;

import cn.tea.toilet.technology.ModConstants;
import cn.tea.toilet.technology.item.ModItems;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Generates the discovery-oriented progression tree for the mod's resource cycle.
 * The methane endpoint intentionally represents constructing the refinement capability;
 * it does not track the first successful methane output.
 */
public final class ModAdvancementProvider extends AdvancementProvider {
    private static final ResourceLocation ROOT_ID = id("root");
    private static final ResourceLocation STONE_BACKGROUND = ResourceLocation.withDefaultNamespace(
            "textures/gui/advancements/backgrounds/stone.png");

    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
            ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, existingFileHelper, List.of(ModAdvancementProvider::generate));
    }

    static List<String> advancementIds() {
        return List.of(
                "root",
                "obtain_feces",
                "dry_feces",
                "build_septic_tank",
                "build_biogas_pond",
                "use_biogas_residue",
                "purify_sewage",
                "refine_methane"
        );
    }

    private static void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver,
            ExistingFileHelper existingFileHelper) {
        AdvancementHolder root = Advancement.Builder.advancement()
                .display(ModItems.FECES.get(), title("root"), description("root"), STONE_BACKGROUND,
                        AdvancementType.TASK, false, false, false)
                .addCriterion("impossible", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                .save(saver, ROOT_ID, existingFileHelper);

        AdvancementHolder obtainFeces = itemAdvancement(root, "obtain_feces", ModItems.FECES.get(), saver, existingFileHelper);
        itemAdvancement(obtainFeces, "dry_feces", ModItems.DRIED_FECES.get(), saver, existingFileHelper);
        AdvancementHolder septicTank = itemAdvancement(obtainFeces, "build_septic_tank",
                ModItems.SEPTIC_TANK_CONTROLLER_ITEM.get(), saver, existingFileHelper);
        AdvancementHolder biogasPond = itemAdvancement(septicTank, "build_biogas_pond",
                ModItems.BIOGAS_POND_CONTROLLER_ITEM.get(), saver, existingFileHelper);
        itemAdvancement(biogasPond, "use_biogas_residue", ModItems.BIOGAS_RESIDUE.get(), saver, existingFileHelper);
        AdvancementHolder purifier = itemAdvancement(biogasPond, "purify_sewage",
                ModItems.SEWAGE_PURIFIER_ITEM.get(), saver, existingFileHelper);
        itemAdvancement(purifier, "refine_methane", ModItems.ABSORPTION_TOWER_BOTTOM_ITEM.get(), saver, existingFileHelper);
    }

    private static AdvancementHolder itemAdvancement(AdvancementHolder parent, String name,
            net.minecraft.world.level.ItemLike item, Consumer<AdvancementHolder> saver,
            ExistingFileHelper existingFileHelper) {
        return Advancement.Builder.advancement()
                .parent(parent)
                .display(item, title(name), description(name), null, AdvancementType.TASK, true, true, false)
                .addCriterion("has_" + name, InventoryChangeTrigger.TriggerInstance.hasItems(item))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(saver, id(name), existingFileHelper);
    }

    private static Component title(String name) {
        return Component.translatable("advancements." + ModConstants.MOD_ID + "." + name + ".title");
    }

    private static Component description(String name) {
        return Component.translatable("advancements." + ModConstants.MOD_ID + "." + name + ".description");
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, path);
    }
}
