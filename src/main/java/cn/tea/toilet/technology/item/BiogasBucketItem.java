package cn.tea.toilet.technology.item;

import cn.tea.toilet.technology.chemical.ModChemicals;
import java.util.List;
import mekanism.api.Action;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
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

    public IChemicalHandler createChemicalHandler(ItemStack container) {
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

    private static final class Handler implements IChemicalHandler {
        private final ItemStack container;

        private Handler(ItemStack container) {
            this.container = container;
        }

        private ChemicalStack contents() {
            long amount = storedAmount(container);
            return container.isEmpty() || amount == 0
                    ? ChemicalStack.EMPTY
                    : new ChemicalStack(ModChemicals.BIOGAS, amount);
        }

        @Override public int getChemicalTanks() { return 1; }
        @Override public ChemicalStack getChemicalInTank(int tank) {
            return tank == 0 ? contents() : ChemicalStack.EMPTY;
        }
        @Override public void setChemicalInTank(int tank, ChemicalStack stack) {
            // Sealed disposable container: arbitrary replacement and refilling are intentionally unsupported.
        }
        @Override public long getChemicalTankCapacity(int tank) { return tank == 0 ? CAPACITY : 0; }
        @Override public boolean isValid(int tank, ChemicalStack stack) {
            return tank == 0 && stack.is(ModChemicals.BIOGAS.get());
        }
        @Override public ChemicalStack insertChemical(int tank, ChemicalStack stack, Action action) {
            if (tank != 0 || stack.isEmpty() || !stack.is(ModChemicals.BIOGAS.get())) return stack;
            long accepted = Math.min(CAPACITY - storedAmount(container), stack.getAmount());
            if (accepted <= 0) return stack;
            if (action.execute()) container.setDamageValue((int) (CAPACITY - storedAmount(container) - accepted));
            return stack.copyWithAmount(stack.getAmount() - accepted);
        }
        @Override public ChemicalStack extractChemical(int tank, long amount, Action action) {
            ChemicalStack stored = contents();
            if (tank != 0 || amount <= 0 || stored.isEmpty()) return ChemicalStack.EMPTY;
            long extracted = Math.min(amount, stored.getAmount());
            if (action.execute()) container.setDamageValue((int) (CAPACITY - storedAmount(container) + extracted));
            return stored.copyWithAmount(extracted);
        }
    }
}