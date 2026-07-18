package tea.toilet.technology.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import tea.toilet.technology.ModTags;
import tea.toilet.technology.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ToiletTechnologyItemTagProvider extends FabricTagProvider<Item> {
	public ToiletTechnologyItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, Registries.ITEM, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		// === 原材料 ===
		getOrCreateTagBuilder(ModTags.Items.MATERIAL_TAG)

				.add(ModItems.FECES);
		// === 方块物品 ===
		getOrCreateTagBuilder(ModTags.Items.BLOCK_TAG)
				.add(ModItems.FECES_BLOCK);

	}
}