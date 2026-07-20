package cn.tea.toilet.technology.event;

import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.block.toilet.SquatToiletBlock;
import cn.tea.toilet.technology.item.ModItems;
import cn.tea.toilet.technology.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerToiletHandler {

    private static final int PRODUCE_INTERVAL = 20;
    private static final Map<UUID, Integer> TIMERS = new HashMap<>();

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        if (!player.isShiftKeyDown()) {
            TIMERS.remove(player.getUUID());
            return;
        }

        BlockPos feet = player.blockPosition();
        BlockPos below = feet.below();
        BlockState stateAtFeet = player.level().getBlockState(feet);
        BlockState stateBelow = player.level().getBlockState(below);

        boolean onToilet = stateAtFeet.getBlock() instanceof SquatToiletBlock
                || stateBelow.getBlock() instanceof SquatToiletBlock;

        if (onToilet) {
            int count = TIMERS.merge(player.getUUID(), 1, Integer::sum);
            if (count >= PRODUCE_INTERVAL) {
                TIMERS.remove(player.getUUID());
                ItemEntity item = new ItemEntity(
                        (ServerLevel) player.level(),
                        player.getX(), player.getY(), player.getZ(),
                        new ItemStack(ModItems.FECES.get())
                );
                item.setPickUpDelay(8);
                player.level().addFreshEntity(item);

                SoundEvent fart = player.getRandom().nextBoolean() ? ModSounds.FART_1.get() : ModSounds.FART_2.get();
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), fart, SoundSource.PLAYERS, 1.0f, 1.0f);

                if (player.getFoodData().getSaturationLevel() > 0) {
                    player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel() - 1f);
                } else if (player.getFoodData().getFoodLevel() > 0) {
                    player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 1);
                }
            }
        } else {
            TIMERS.remove(player.getUUID());
        }
    }


}
