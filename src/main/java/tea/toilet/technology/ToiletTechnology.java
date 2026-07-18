package tea.toilet.technology;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tea.toilet.technology.block.ModBlocks;
import tea.toilet.technology.item.ModItemGroups;
import tea.toilet.technology.item.ModItems;

public class ToiletTechnology implements ModInitializer {
	public static final String MOD_ID = "toilet_technology";

	// 此记录器用于将文本写入控制台和日志文件。将您的mod-id用作记录器的名称被认为是最佳做法。这样，就很清楚哪个模块写了信息、警告和错误。
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// 一旦Minecraft处于mod加载就绪状态，此代码就会运行。然而，有些东西（如资源）可能仍未初始化。谨慎行事。
		LOGGER.info("Hello Fabric world!");
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModItemGroups.registerModItemsGroups();
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
