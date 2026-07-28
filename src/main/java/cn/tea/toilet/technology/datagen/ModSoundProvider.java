package cn.tea.toilet.technology.datagen;
import cn.tea.toilet.technology.ModConstants;

import cn.tea.toilet.technology.sound.ModSounds;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ModSoundProvider extends SoundDefinitionsProvider {
    public ModSoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, ModConstants.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        add(ModSounds.FART_1.get(), definition()
                .with(sound("toilet_technology:fart_1")));
        add(ModSounds.FART_2.get(), definition()
                .with(sound("toilet_technology:fart_2")));
        add(ModSounds.GAS_MELTING_FURNACE_WORKING.get(), definition()
                .with(sound("toilet_technology:gas_melting_furnace_working").preload().attenuationDistance(18)));
    }
}
