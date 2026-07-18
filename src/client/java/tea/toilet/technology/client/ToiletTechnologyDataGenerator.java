package tea.toilet.technology.client;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import tea.toilet.technology.client.datagen.ModBlockTagsProvider;
import tea.toilet.technology.client.datagen.ModModelProvider;
import tea.toilet.technology.client.datagen.lang.ModChineseLangProvider;
import tea.toilet.technology.client.datagen.lang.ModEnglishLangProvider;
import tea.toilet.technology.client.datagen.ModItemTagsProvider;

public class ToiletTechnologyDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		ModBlockTagsProvider blockTagProvider = pack.addProvider(ModBlockTagsProvider::new);
		pack.addProvider((output, registriesFuture) -> new ModItemTagsProvider(output, registriesFuture, blockTagProvider));
		pack.addProvider(ModEnglishLangProvider::new);
		pack.addProvider(ModChineseLangProvider::new);
		pack.addProvider(ModModelProvider::new);
	}
}
