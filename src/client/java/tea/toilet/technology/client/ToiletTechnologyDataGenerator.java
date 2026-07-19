package tea.toilet.technology.client;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import tea.toilet.technology.client.datagen.*;
import tea.toilet.technology.client.datagen.lang.ModChineseLangProvider;
import tea.toilet.technology.client.datagen.lang.ModEnglishLangProvider;

public class ToiletTechnologyDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		ModBlockTagsProvider blockTagProvider = pack.addProvider(ModBlockTagsProvider::new);
		pack.addProvider((output, registriesFuture) -> new ModItemTagsProvider(output, registriesFuture, blockTagProvider));
		pack.addProvider(ModEnglishLangProvider::new);
		pack.addProvider(ModChineseLangProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModBlockLootTableProvider::new);
	}
}
