package cn.tea.toilet.technology.item;

import cn.tea.toilet.technology.api.gas.GasAction;
import cn.tea.toilet.technology.gas.GasRegistry;
import cn.tea.toilet.technology.api.gas.GasStack;
import cn.tea.toilet.technology.api.gas.IGasHandler;
import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

/** A reusable, sealed container for one type of registered gas. */
public final class GasTankItem extends Item {
    public static final long CAPACITY = 1_000;
    private static final String GAS_TAG = "Gas";
    private static final String AMOUNT_TAG = "Amount";

    public GasTankItem(Properties properties) {
        super(properties);
    }

    /** Creates a tank filled to capacity with a registered gas for creative-mode display. */
    public static ItemStack createFilledStack(GasStack contents) {
        ItemStack container = new ItemStack(ModItems.GAS_TANK.get());
        setContents(container, contents.copyWithAmount(CAPACITY));
        return container;
    }

    public IGasHandler createGasHandler(ItemStack container) {
        return new Handler(container);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
            @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        GasStack contents = getContents(stack);
        if (contents.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.toilet_technology.gas_tank.empty"));
        } else {
            tooltip.add(Component.translatable("tooltip.toilet_technology.gas_tank.amount",
                    contents.getTextComponent(), contents.getAmount(), CAPACITY));
        }
        tooltip.add(Component.translatable("tooltip.toilet_technology.gas_tank.sealed"));
    }

    /** Returns the gas currently stored by this tank without exposing its mutable capability. */
    public static GasStack getContents(ItemStack container) {
        CompoundTag tag = container.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        ResourceLocation gasId = ResourceLocation.tryParse(tag.getString(GAS_TAG));
        return new GasStack(GasRegistry.byId(gasId), Math.min(CAPACITY, tag.getLong(AMOUNT_TAG)));
    }

    private static void setContents(ItemStack container, GasStack contents) {
        CompoundTag tag = container.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (contents.isEmpty()) {
            tag.remove(GAS_TAG);
            tag.remove(AMOUNT_TAG);
        } else {
            tag.putString(GAS_TAG, contents.getGas().id().toString());
            tag.putLong(AMOUNT_TAG, contents.getAmount());
        }
        container.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    private static final class Handler implements IGasHandler {
        private final ItemStack container;

        private Handler(ItemStack container) {
            this.container = container;
        }

        @Override public int getGasTanks() { return 1; }
        @Override public GasStack getGasInTank(int tank) { return tank == 0 ? getContents(container) : GasStack.EMPTY; }
        @Override public void setGasInTank(int tank, GasStack stack) {
            if (tank == 0 && (stack.isEmpty() || GasRegistry.byId(stack.getGas().id()) != null)) {
                setContents(container, stack.isEmpty() ? GasStack.EMPTY : stack.copyWithAmount(Math.min(CAPACITY, stack.getAmount())));
            }
        }
        @Override public long getGasTankCapacity(int tank) { return tank == 0 ? CAPACITY : 0; }
        @Override public boolean isValid(int tank, GasStack stack) {
            GasStack stored = getGasInTank(tank);
            return tank == 0 && !stack.isEmpty() && GasRegistry.byId(stack.getGas().id()) != null
                    && (stored.isEmpty() || stored.is(stack.getGas()));
        }
        @Override public GasStack insertGas(int tank, GasStack stack, GasAction action) {
            if (!isValid(tank, stack)) return stack;
            GasStack stored = getGasInTank(tank);
            long accepted = Math.min(CAPACITY - stored.getAmount(), stack.getAmount());
            if (accepted <= 0) return stack;
            if (action.execute()) setContents(container, new GasStack(stack.getGas(), stored.getAmount() + accepted));
            return stack.copyWithAmount(stack.getAmount() - accepted);
        }
        @Override public GasStack extractGas(int tank, long amount, GasAction action) {
            GasStack stored = getGasInTank(tank);
            if (tank != 0 || amount <= 0 || stored.isEmpty()) return GasStack.EMPTY;
            long extracted = Math.min(amount, stored.getAmount());
            if (action.execute()) setContents(container, stored.copyWithAmount(stored.getAmount() - extracted));
            return stored.copyWithAmount(extracted);
        }
    }
}
