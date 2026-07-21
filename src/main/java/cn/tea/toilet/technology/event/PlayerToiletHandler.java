package cn.tea.toilet.technology.event;

import cn.tea.toilet.technology.block.toilet.PremiumToiletBlock;
import cn.tea.toilet.technology.block.toilet.PremiumToiletBlockEntity;
import cn.tea.toilet.technology.block.toilet.SquatToiletBlock;
import cn.tea.toilet.technology.fluid.ModFluids;
import cn.tea.toilet.technology.item.ModItems;
import cn.tea.toilet.technology.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.fluids.FluidStack;

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

        boolean onPremiumToilet = stateAtFeet.getBlock() instanceof PremiumToiletBlock
                || stateBelow.getBlock() instanceof PremiumToiletBlock;

        if (onToilet || onPremiumToilet) {
            int interval = PRODUCE_INTERVAL;
            if (onPremiumToilet) {
                BlockPos toiletPos = stateAtFeet.getBlock() instanceof PremiumToiletBlock ? feet : below;
                if (player.level().getBlockState(toiletPos).getBlock() instanceof PremiumToiletBlock premiumBlock) {
                    interval = (int) (PRODUCE_INTERVAL * premiumBlock.getMultiplier());
                }
            }

            int count = TIMERS.merge(player.getUUID(), 1, Integer::sum);
            if (count >= interval) {
                TIMERS.remove(player.getUUID());

                if (onPremiumToilet) {
                    handlePremiumToilet(player, stateAtFeet, stateBelow, feet, below);
                } else {
                    produceFeces(player);
                }

                playFartSound(player);
                consumeHunger(player);
            }
        } else {
            TIMERS.remove(player.getUUID());
        }
    }

    private void handlePremiumToilet(Player player, BlockState stateAtFeet, BlockState stateBelow, BlockPos feet, BlockPos below) {
        BlockPos toiletPos = stateAtFeet.getBlock() instanceof PremiumToiletBlock ? feet : below;
        BlockEntity be = player.level().getBlockEntity(toiletPos);
        if (!(be instanceof PremiumToiletBlockEntity toiletBE)) {
            produceFeces(player);
            return;
        }

        FluidStack fluid = toiletBE.fluidTank.getFluid();
        if (fluid.isEmpty()) {
            produceFeces(player);
        } else if (fluid.getFluid() == Fluids.WATER) {
            int amount = fluid.getAmount();
            toiletBE.fluidTank.drain(amount, net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE);
            toiletBE.fluidTank.fill(new FluidStack(ModFluids.FECES_LIQUID.get(), amount), net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE);
            playSplashSound(player);
        } else if (fluid.getFluid() == ModFluids.FECES_LIQUID.get()) {
            if (toiletBE.fluidTank.getFluidAmount() < toiletBE.fluidTank.getCapacity()) {
                toiletBE.fluidTank.fill(new FluidStack(ModFluids.FECES_LIQUID.get(), 1000), net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE);
            }
            playSplashSound(player);
        } else if (fluid.getFluid() == Fluids.LAVA) {
            playBurnSound(player);
        }
    }

    private void playSplashSound(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 0.3f, 1.0f);
    }

    private void playBurnSound(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.GENERIC_BURN, SoundSource.PLAYERS, 0.3f, 1.0f);
    }

    private void produceFeces(Player player) {
        ItemEntity item = new ItemEntity(
                (ServerLevel) player.level(),
                player.getX(), player.getY(), player.getZ(),
                new ItemStack(ModItems.FECES.get())
        );
        item.setPickUpDelay(8);
        player.level().addFreshEntity(item);
    }

    private void playFartSound(Player player) {
        SoundEvent fart = player.getRandom().nextBoolean() ? ModSounds.FART_1.get() : ModSounds.FART_2.get();
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), fart, SoundSource.PLAYERS, 1.0f, 1.0f);
    }

    private void consumeHunger(Player player) {
        if (player.getFoodData().getSaturationLevel() > 0) {
            player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel() - 1f);
        } else if (player.getFoodData().getFoodLevel() > 0) {
            player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 1);
        }
    }
}