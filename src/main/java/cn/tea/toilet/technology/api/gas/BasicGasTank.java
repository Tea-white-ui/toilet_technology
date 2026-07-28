package cn.tea.toilet.technology.api.gas;

import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

/**
 * Reusable single-gas tank implementation with validation, callbacks, and stable NBT persistence.
 *
 * <p>Accepted stacks are limited to capacity. Simulated operations never invoke the change callback.
 * The NBT format is {@code {Gas: "namespace:id", Amount: long}} and also accepts Mekanism 10.7's
 * {@code {stored: {id: "namespace:id", amount: long}}} format for migration.</p>
 */
public final class BasicGasTank {
    private final long capacity;
    private final Predicate<GasStack> validator;
    private final Runnable contentsChanged;
    private GasStack stack = GasStack.EMPTY;

    public BasicGasTank(long capacity, Predicate<GasStack> validator, Runnable contentsChanged) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Gas tank capacity must be positive");
        }
        this.capacity = capacity;
        this.validator = Objects.requireNonNull(validator, "validator");
        this.contentsChanged = Objects.requireNonNull(contentsChanged, "contentsChanged");
    }

    public long getCapacity() {
        return capacity;
    }

    public long getStored() {
        return stack.amount();
    }

    public GasStack getStack() {
        return stack;
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public boolean isValid(GasStack candidate) {
        return candidate != null && !candidate.isEmpty() && validator.test(candidate);
    }

    public void setStack(GasStack candidate) {
        GasStack normalized = candidate == null || candidate.isEmpty() || !isValid(candidate)
                ? GasStack.EMPTY : candidate.copyWithAmount(Math.min(candidate.amount(), capacity));
        if (!stack.equals(normalized)) {
            stack = normalized;
            contentsChanged.run();
        }
    }

    public void setEmpty() {
        setStack(GasStack.EMPTY);
    }

    public GasStack insert(GasStack candidate, GasAction action) {
        if (!isValid(candidate) || (!stack.isEmpty() && !stack.is(candidate.gas()))) {
            return candidate == null ? GasStack.EMPTY : candidate;
        }
        long accepted = Math.min(capacity - getStored(), candidate.amount());
        if (accepted <= 0) {
            return candidate;
        }
        if (action.executes()) {
            setStack(new GasStack(candidate.gas(), getStored() + accepted));
        }
        return candidate.copyWithAmount(candidate.amount() - accepted);
    }

    public GasStack extract(long amount, GasAction action) {
        if (amount <= 0 || stack.isEmpty()) {
            return GasStack.EMPTY;
        }
        long extracted = Math.min(amount, stack.amount());
        GasStack result = stack.copyWithAmount(extracted);
        if (action.executes()) {
            setStack(stack.copyWithAmount(stack.amount() - extracted));
        }
        return result;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (!stack.isEmpty()) {
            tag.putString("Gas", stack.gas().id().toString());
            tag.putLong("Amount", stack.amount());
        }
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        CompoundTag payload = tag.contains("stored") ? tag.getCompound("stored") : tag;
        String gasId = payload.getString("Gas");
        long amount = payload.getLong("Amount");
        if (gasId.isEmpty()) {
            gasId = payload.getString("id");
        }
        if (amount <= 0) {
            amount = payload.getLong("amount");
        }
        Gas gas = ToiletGasRegistry.get(ResourceLocation.tryParse(gasId)).orElse(null);
        setStack(gas == null ? GasStack.EMPTY : new GasStack(gas, amount));
    }
}
