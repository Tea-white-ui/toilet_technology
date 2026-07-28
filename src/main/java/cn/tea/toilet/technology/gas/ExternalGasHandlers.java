package cn.tea.toilet.technology.gas;

import cn.tea.toilet.technology.api.gas.GasCapabilities;
import cn.tea.toilet.technology.api.gas.IGasHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Internal optional-compatibility bridge for exposing foreign handlers to pipe code.
 *
 * <p>This is not a supported addon extension point. Addons must expose
 * {@link GasCapabilities#BLOCK} directly.</p>
 */
public final class ExternalGasHandlers {
    private static final List<Provider> PROVIDERS = new CopyOnWriteArrayList<>();

    private ExternalGasHandlers() {
    }

    public static void register(Provider provider) {
        if (!PROVIDERS.contains(provider)) PROVIDERS.add(provider);
    }

    @Nullable
    public static IGasHandler find(Level level, BlockPos pos, @Nullable Direction side) {
        IGasHandler nativeHandler = level.getCapability(GasCapabilities.BLOCK, pos, side);
        if (nativeHandler != null) return nativeHandler;
        for (Provider provider : PROVIDERS) {
            IGasHandler handler = provider.find(level, pos, side);
            if (handler != null) return handler;
        }
        return null;
    }

    @FunctionalInterface
    public interface Provider {
        @Nullable IGasHandler find(Level level, BlockPos pos, @Nullable Direction side);
    }
}
