package cn.tea.toilet.technology.api.gas;

import cn.tea.toilet.technology.ModConstants;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PublicGasApiTest {
    @Test
    void normalizesInvalidStacksToEmpty() {
        assertTrue(new GasStack(null, 1).isEmpty());
        assertTrue(new GasStack(ToiletGasRegistry.BIOGAS, 0).isEmpty());
        assertTrue(new GasStack(ToiletGasRegistry.BIOGAS, -1).isEmpty());
        assertTrue(new GasStack(ToiletGasRegistry.BIOGAS, 1).copyWithAmount(0).isEmpty());
    }

    @Test
    void resolvesReadOnlyBuiltInGasesByStableResourceLocation() {
        ResourceLocation biogasId = ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "biogas");
        List<Gas> gases = ToiletGasRegistry.gases();

        assertSame(ToiletGasRegistry.BIOGAS, ToiletGasRegistry.get(biogasId).orElseThrow());
        assertFalse(ToiletGasRegistry.get(ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "unknown")).isPresent());
        assertThrows(UnsupportedOperationException.class, () -> gases.add(ToiletGasRegistry.BIOGAS));
        assertEquals(List.of(ToiletGasRegistry.BIOGAS, ToiletGasRegistry.METHANE), gases);
    }

    @Test
    void exposesTheStableGasHandlerCapabilities() {
        assertEquals(ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "gas_handler"), GasCapabilities.BLOCK.name());
        assertEquals(ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "gas_handler"), GasCapabilities.ITEM.name());
    }
}
