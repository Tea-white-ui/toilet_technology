package cn.tea.toilet.technology.api.gas;

import net.minecraft.nbt.CompoundTag;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BasicGasTankApiTest {
    @Test
    void simulationDoesNotMutateOrNotifyAndExecutionRespectsCapacity() {
        AtomicInteger changes = new AtomicInteger();
        BasicGasTank tank = new BasicGasTank(100, stack -> stack.is(ToiletGasRegistry.BIOGAS), changes::incrementAndGet);
        GasStack offered = new GasStack(ToiletGasRegistry.BIOGAS, 150);

        assertEquals(new GasStack(ToiletGasRegistry.BIOGAS, 50), tank.insert(offered, GasAction.SIMULATE));
        assertTrue(tank.isEmpty());
        assertEquals(0, changes.get());

        assertEquals(new GasStack(ToiletGasRegistry.BIOGAS, 50), tank.insert(offered, GasAction.EXECUTE));
        assertEquals(new GasStack(ToiletGasRegistry.BIOGAS, 100), tank.getStack());
        assertEquals(1, changes.get());
    }

    @Test
    void restoresLegacyAndMekanismNbtFormatsWithValidationAndCapacityLimit() {
        BasicGasTank tank = new BasicGasTank(100, stack -> stack.is(ToiletGasRegistry.BIOGAS), () -> { });
        CompoundTag legacy = new CompoundTag();
        legacy.putString("Gas", ToiletGasRegistry.BIOGAS.id().toString());
        legacy.putLong("Amount", 150);

        tank.deserializeNBT(legacy);
        assertEquals(new GasStack(ToiletGasRegistry.BIOGAS, 100), tank.getStack());

        CompoundTag mekanism = new CompoundTag();
        CompoundTag stored = new CompoundTag();
        stored.putString("id", ToiletGasRegistry.BIOGAS.id().toString());
        stored.putLong("amount", 20);
        mekanism.put("stored", stored);

        tank.deserializeNBT(mekanism);
        assertEquals(new GasStack(ToiletGasRegistry.BIOGAS, 20), tank.getStack());
    }
}
