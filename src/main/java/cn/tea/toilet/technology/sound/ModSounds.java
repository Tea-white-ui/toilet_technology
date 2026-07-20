package cn.tea.toilet.technology.sound;

import cn.tea.toilet.technology.ToiletTechnology;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(
            BuiltInRegistries.SOUND_EVENT, ToiletTechnology.MOD_ID
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> FART_1 = SOUNDS.register("fart_1",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, "fart_1")));

    public static final DeferredHolder<SoundEvent, SoundEvent> FART_2 = SOUNDS.register("fart_2",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, "fart_2")));

    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }
}
