package tea.toilet.technology.client;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import tea.toilet.technology.client.datagen.ToiletTechnologyBlockTagProvider;
import tea.toilet.technology.client.datagen.ToiletTechnologyChineseLangProvider;
import tea.toilet.technology.client.datagen.ToiletTechnologyEnglishLangProvider;
import tea.toilet.technology.client.datagen.ToiletTechnologyItemTagProvider;

public class ToiletTechnologyDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ToiletTechnologyItemTagProvider::new);
		pack.addProvider(ToiletTechnologyBlockTagProvider::new);
		pack.addProvider(ToiletTechnologyEnglishLangProvider::new);
		pack.addProvider(ToiletTechnologyChineseLangProvider::new);
	}
}
