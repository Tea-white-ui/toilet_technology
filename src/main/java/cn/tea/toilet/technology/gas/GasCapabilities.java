package cn.tea.toilet.technology.gas;
import cn.tea.toilet.technology.ModConstants;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;

public final class GasCapabilities {
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(
            ModConstants.MOD_ID, "gas_handler");
    public static final BlockCapability<IGasHandler, Direction> BLOCK = BlockCapability.createSided(ID, IGasHandler.class);
    public static final ItemCapability<IGasHandler, Void> ITEM = ItemCapability.createVoid(ID, IGasHandler.class);

    private GasCapabilities() { }
}