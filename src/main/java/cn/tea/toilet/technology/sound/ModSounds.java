package cn.tea.toilet.technology.sound;
import cn.tea.toilet.technology.ModConstants;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 声音事件注册类
 * 负责注册模组中的所有自定义声音事件
 * 包括放屁声音等趣味音效
 */
public class ModSounds {
    /** 声音事件延迟注册表 */
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(
            BuiltInRegistries.SOUND_EVENT, ModConstants.MOD_ID
    );

    /** 放屁声音1：随机播放的放屁音效之一 */
    public static final DeferredHolder<SoundEvent, SoundEvent> FART_1 = SOUNDS.register("fart_1",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "fart_1")));

    /** 放屁声音2：随机播放的放屁音效之二 */
    public static final DeferredHolder<SoundEvent, SoundEvent> FART_2 = SOUNDS.register("fart_2",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "fart_2")));

    /**
     * 注册所有声音事件到事件总线
     * 
     * @param bus NeoForge 事件总线
     */
    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }
}