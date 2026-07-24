package cn.tea.toilet.technology.gas;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record Gas(ResourceLocation id, ResourceLocation texture, int tint) {
    public Component displayName() {
        return Component.translatable("gas." + id.getNamespace() + "." + id.getPath());
    }
}