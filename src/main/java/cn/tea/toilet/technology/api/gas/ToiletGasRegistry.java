package cn.tea.toilet.technology.api.gas;

import cn.tea.toilet.technology.ModConstants;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

/**
 * Read-only lookup for Toilet Technology's built-in gases.
 *
 * <p>Version 1.0 intentionally does not support third-party gas registration. The returned list
 * is immutable and gas identity is always the {@link ResourceLocation} returned by {@link Gas#id()}.</p>
 */
public final class ToiletGasRegistry {
    public static final Gas BIOGAS = new Gas(
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "biogas"),
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "block/biogas"),
            0x87965B);
    public static final Gas METHANE = new Gas(
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "methane"),
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "block/methane"),
            0x8EE6F2);

    private static final List<Gas> GASES = List.of(BIOGAS, METHANE);

    private ToiletGasRegistry() {
    }

    public static List<Gas> gases() {
        return GASES;
    }

    public static Optional<Gas> get(ResourceLocation id) {
        if (id == null) {
            return Optional.empty();
        }
        return GASES.stream().filter(gas -> gas.id().equals(id)).findFirst();
    }
}
