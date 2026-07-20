package cn.tea.toilet.technology.datagen;

import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.datagen.lang.ModLanguageEnUsProvider;
import cn.tea.toilet.technology.datagen.lang.ModLanguageZhCnProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ToiletTechnology.MOD_ID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        ModBlockTagProvider blockTagProvider = new ModBlockTagProvider(output, lookupProvider, ToiletTechnology.MOD_ID, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagProvider);

        ModItemTagProvider itemTagProvider = new ModItemTagProvider(output, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper);
        generator.addProvider(event.includeServer(), itemTagProvider);

        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, existingFileHelper));

        generator.addProvider(event.includeServer(), ModLootTableProvider.create(output, lookupProvider));

        generator.addProvider(event.includeClient(), new ModLanguageEnUsProvider(output, "en_us"));
        generator.addProvider(event.includeClient(), new ModLanguageZhCnProvider(output, "zh_cn"));
    }
}
