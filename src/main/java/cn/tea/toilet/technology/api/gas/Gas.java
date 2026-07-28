package cn.tea.toilet.technology.api.gas;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Immutable identity and client presentation data for a gas supported by Toilet Technology.
 *
 * <p>Use {@link #id()} as the stable cross-mod identity. Do not use object identity or menu
 * synchronization indexes to identify a gas.</p>
 */
public record Gas(ResourceLocation id, ResourceLocation texture, int tint) {
    public Component displayName() {
        return Component.translatable("gas." + id.getNamespace() + "." + id.getPath());
    }
}
