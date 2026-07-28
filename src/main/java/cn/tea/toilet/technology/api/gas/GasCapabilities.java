package cn.tea.toilet.technology.api.gas;

import cn.tea.toilet.technology.ModConstants;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;

/**
 * Stable NeoForge capabilities for querying and providing {@link IGasHandler} instances.
 *
 * <p>Consumers must query a block capability immediately before use and must not cache the
 * returned handler across capability invalidation. Providers must invalidate capability caches
 * when their exposed sides or availability change.</p>
 */
public final class GasCapabilities {
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(
            ModConstants.MOD_ID, "gas_handler");

    public static final BlockCapability<IGasHandler, Direction> BLOCK = BlockCapability.createSided(ID, IGasHandler.class);
    public static final ItemCapability<IGasHandler, Void> ITEM = ItemCapability.createVoid(ID, IGasHandler.class);

    private GasCapabilities() {
    }
}
