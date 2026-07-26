package cn.tea.toilet.technology.gas;
import cn.tea.toilet.technology.ModConstants;

import java.util.List;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public final class GasRegistry {
    public static final Gas BIOGAS = new Gas(
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "biogas"),
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "block/biogas"),
            0x87965B);
    public static final Gas METHANE = new Gas(
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "methane"),
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "block/methane"),
            0x8EE6F2);
    private static final List<Gas> GASES = List.of(BIOGAS, METHANE);

    private GasRegistry() { }

    public static List<Gas> gases() { return GASES; }
    public static int id(Gas gas) { return GASES.indexOf(gas); }
    public static @Nullable Gas byId(int id) { return id >= 0 && id < GASES.size() ? GASES.get(id) : null; }
    public static @Nullable Gas byId(@Nullable ResourceLocation id) {
        return id == null ? null : GASES.stream().filter(gas -> gas.id().equals(id)).findFirst().orElse(null);
    }
}