package cn.tea.toilet.technology.gas;

import cn.tea.toilet.technology.api.gas.Gas;
import cn.tea.toilet.technology.api.gas.ToiletGasRegistry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Internal bridge for legacy menu wire IDs. Addon code must use {@link ToiletGasRegistry} instead.
 */
public final class GasRegistry {
    public static final Gas BIOGAS = ToiletGasRegistry.BIOGAS;
    public static final Gas METHANE = ToiletGasRegistry.METHANE;
    private static final List<Gas> GASES = ToiletGasRegistry.gases();

    private GasRegistry() { }

    public static List<Gas> gases() { return GASES; }
    public static int id(Gas gas) { return GASES.indexOf(gas); }
    public static @Nullable Gas byId(int id) { return id >= 0 && id < GASES.size() ? GASES.get(id) : null; }
    public static @Nullable Gas byId(@Nullable ResourceLocation id) {
        return id == null ? null : ToiletGasRegistry.get(id).orElse(null);
    }
}