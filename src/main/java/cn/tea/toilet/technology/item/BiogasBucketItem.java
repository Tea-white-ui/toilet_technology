package cn.tea.toilet.technology.item;

import cn.tea.toilet.technology.gas.GasAction;
import cn.tea.toilet.technology.gas.GasRegistry;
import cn.tea.toilet.technology.gas.GasStack;
import cn.tea.toilet.technology.gas.IGasHandler;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

/** A sealed, non-placeable bucket containing a fixed 1,000 mB of biogas. */
public final class BiogasBucketItem extends Item {
    public static final long CAPACITY = 1_000;

    public BiogasBucketItem(Properties properties) {
        super(properties);
    }

    public IGasHandler createGasHandler(ItemStack container) {
        return new Handler(container);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
            @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable("tooltip.toilet_technology.biogas_bucket.amount", storedAmount(stack), CAPACITY));
        tooltip.add(Component.translatable("tooltip.toilet_technology.biogas_bucket.sealed"));
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack stack, @NotNull ItemStack repairCandidate) {
        return false;
    }

    private static long storedAmount(ItemStack container) {
        return Math.max(0, CAPACITY - container.getDamageValue());
    }

    private static final class Handler implements IGasHandler {
        private final ItemStack container;

        private Handler(ItemStack container) {
            this.container = container;
        }

        private GasStack contents() {
            long amount = storedAmount(container);
            return container.isEmpty() || amount == 0
                    ? GasStack.EMPTY
                    : new GasStack(GasRegistry.BIOGAS, amount);
        }

        @Override public int getGasTanks() { return 1; }
        @Override public GasStack getGasInTank(int tank) {
            return tank == 0 ? contents() : GasStack.EMPTY;
        }
        @Override public void setGasInTank(int tank, GasStack stack) {
            // Sealed disposable container: arbitrary replacement and refilling are intentionally unsupported.
        }
        @Override public long getGasTankCapacity(int tank) { return tank == 0 ? CAPACITY : 0; }
        @Override public boolean isValid(int tank, GasStack stack) {
            return tank == 0 && stack.is(GasRegistry.BIOGAS);
        }
        @Override public GasStack insertGas(int tank, GasStack stack, GasAction action) {
            if (tank != 0 || stack.isEmpty() || !stack.is(GasRegistry.BIOGAS)) return stack;
            long accepted = Math.min(CAPACITY - storedAmount(container), stack.getAmount());
            if (accepted <= 0) return stack;
            if (action.execute()) container.setDamageValue((int) (CAPACITY - storedAmount(container) - accepted));
            return stack.copyWithAmount(stack.getAmount() - accepted);
        }
        @Override public GasStack extractGas(int tank, long amount, GasAction action) {
            GasStack stored = contents();
            if (tank != 0 || amount <= 0 || stored.isEmpty()) return GasStack.EMPTY;
            long extracted = Math.min(amount, stored.getAmount());
            if (action.execute()) container.setDamageValue((int) (CAPACITY - storedAmount(container) + extracted));
            return stored.copyWithAmount(extracted);
        }
    }
}