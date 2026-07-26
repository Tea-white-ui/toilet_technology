package cn.tea.toilet.technology.gas;

import cn.tea.toilet.technology.item.GasTankItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/** Server-side player transfer operations between a gas tank item and a block gas handler. */
public final class GasTankTransferInteraction {
    public static final long TRANSFER_LIMIT = GasTankItem.CAPACITY;

    private GasTankTransferInteraction() {
    }

    /**
     * Transfers one tankful at most. Normal use extracts from the block; sneaking inserts into it.
     * Returns the transferred gas, or {@link GasStack#EMPTY} when no transfer is possible.
     */
    public static GasStack transfer(IGasHandler blockHandler, ItemStack heldTank, boolean insertIntoBlock) {
        @Nullable IGasHandler itemHandler = heldTank.getCapability(GasCapabilities.ITEM);
        if (itemHandler == null) return GasStack.EMPTY;
        return insertIntoBlock
                ? GasTransfer.transfer(itemHandler, 0, blockHandler, 0, TRANSFER_LIMIT)
                : GasTransfer.transfer(blockHandler, 0, itemHandler, 0, TRANSFER_LIMIT);
    }
}
