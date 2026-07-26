package cn.tea.toilet.technology.event;

import cn.tea.toilet.technology.compat.PoopSkyCompat;
import cn.tea.toilet.technology.ModAttachments;
import cn.tea.toilet.technology.block.toilet.NetheriteToiletBlock;
import cn.tea.toilet.technology.block.toilet.NetheriteToiletBlockEntity;
import cn.tea.toilet.technology.block.toilet.PremiumToiletBlock;
import cn.tea.toilet.technology.block.toilet.PremiumToiletBlockEntity;
import cn.tea.toilet.technology.block.toilet.SquatToiletBlock;
import cn.tea.toilet.technology.fluid.ModFluids;
import cn.tea.toilet.technology.item.ModItems;
import cn.tea.toilet.technology.particle.DefecationParticleMotion;
import cn.tea.toilet.technology.particle.ModParticles;
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

/**
 * 玩家马桶交互处理器
 * 监听玩家事件，处理玩家在马桶上蹲下时的交互逻辑：
 * - 蹲便器：直接掉落粪便物品
 * - 高级马桶：根据内部流体类型处理（水变粪便、粪便增加、熔岩燃烧）
 * - 消耗玩家饥饿值
 * - 播放放屁/飞溅/燃烧音效
 *
 * 计时器实现：
 * - 使用 NeoForge AttachmentType 把"蹲下 tick 计数"挂载到玩家实体上（见 ModAttachments#TOILET_SQUAT_TICKS）
 * - 玩家实体死亡 / 登出 / 跨维度时 attachment 由 NeoForge 自动清理，不会泄漏
 * - 替代旧实现的 static ConcurrentHashMap<UUID, Integer>，避免了：
 *   a) 玩家异常掉线时服务端等待 TCP 超时期间残留计时器，导致后续 count >= interval 永远成立
 *   b) Map 永远增长（玩家 UUID 不会被 GC）
 *   c) 每次自增的 Integer 装箱开销
 */
public class PlayerToiletHandler {

    /** 产生粪便的基础间隔时间（tick），20 tick = 1秒 */
    private static final int PRODUCE_INTERVAL = 20;

    /**
     * 玩家Tick事件处理
     * 检测玩家是否在马桶上蹲下，并处理相应的交互逻辑
     *
     * @param event 玩家Tick事件
     */
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        // 玩家未蹲下时重置计时器
        if (!player.isShiftKeyDown()) {
            player.setData(ModAttachments.TOILET_SQUAT_TICKS, 0);
            return;
        }

        BlockPos feet = player.blockPosition();
        BlockPos below = feet.below();
        BlockState stateAtFeet = player.level().getBlockState(feet);
        BlockState stateBelow = player.level().getBlockState(below);

        // 检测玩家是否站在马桶上
        boolean onPremiumToilet = stateAtFeet.getBlock() instanceof PremiumToiletBlock
                || stateBelow.getBlock() instanceof PremiumToiletBlock;
        boolean onNetheriteToilet = stateAtFeet.getBlock() instanceof NetheriteToiletBlock
                || stateBelow.getBlock() instanceof NetheriteToiletBlock;
        boolean onToilet = !onPremiumToilet && !onNetheriteToilet && (stateAtFeet.getBlock() instanceof SquatToiletBlock
                || stateBelow.getBlock() instanceof SquatToiletBlock);

        if (onToilet || onPremiumToilet || onNetheriteToilet) {
            int interval = PRODUCE_INTERVAL;
            // 高级马桶根据材质效率调整产生间隔
            if (onPremiumToilet) {
                BlockPos toiletPos = stateAtFeet.getBlock() instanceof PremiumToiletBlock ? feet : below;
                if (player.level().getBlockState(toiletPos).getBlock() instanceof PremiumToiletBlock premiumBlock) {
                    interval = (int) (PRODUCE_INTERVAL * premiumBlock.getMultiplier());
                }
            } else if (onNetheriteToilet) {
                BlockPos toiletPos = stateAtFeet.getBlock() instanceof NetheriteToiletBlock ? feet : below;
                if (player.level().getBlockState(toiletPos).getBlock() instanceof NetheriteToiletBlock netheriteBlock) {
                    interval = (int) (PRODUCE_INTERVAL * netheriteBlock.getMultiplier());
                }
            }

            int count = player.getData(ModAttachments.TOILET_SQUAT_TICKS) + 1;
            if (count >= interval) {
                // 达到间隔：重置计数并执行交互
                player.setData(ModAttachments.TOILET_SQUAT_TICKS, 0);

                if (onPremiumToilet || onNetheriteToilet) {
                    handlePremiumToilet(player, stateAtFeet, stateBelow, feet, below);
                    spawnDefecationParticles((ServerLevel) player.level(), player);
                } else {
                    produceFeces(player);
                    spawnDefecationParticles((ServerLevel) player.level(), player);
                }

                playFartSound(player);
                consumeHunger(player);
            } else {
                player.setData(ModAttachments.TOILET_SQUAT_TICKS, count);
            }
        } else {
            // 不在马桶上时重置
            player.setData(ModAttachments.TOILET_SQUAT_TICKS, 0);
        }
    }

    /**
     * 处理高级马桶交互逻辑
     * 根据马桶内流体类型执行不同操作：
     * - 空：掉落粪便物品
     * - 水：水转化为粪便液体
     * - 粪便液体：增加粪便液体量
     * - 熔岩：播放燃烧音效
     *
     * @param player 玩家
     * @param stateAtFeet 脚下方块状态
     * @param stateBelow 下方方块状态
     * @param feet 脚下位置
     * @param below 下方位置
     */
    private void handlePremiumToilet(Player player, BlockState stateAtFeet, BlockState stateBelow, BlockPos feet, BlockPos below) {
        BlockPos toiletPos = stateAtFeet.getBlock() instanceof PremiumToiletBlock
                || stateAtFeet.getBlock() instanceof NetheriteToiletBlock ? feet : below;
        BlockEntity be = player.level().getBlockEntity(toiletPos);

        net.neoforged.neoforge.fluids.capability.templates.FluidTank fluidTank = null;

        if (be instanceof PremiumToiletBlockEntity premiumBE) {
            fluidTank = premiumBE.fluidTank;
        } else if (be instanceof NetheriteToiletBlockEntity netheriteBE) {
            fluidTank = netheriteBE.fluidTank;
        }

        if (fluidTank == null) {
            produceFeces(player);
            return;
        }

        FluidStack fluid = fluidTank.getFluid();
        if (fluid.isEmpty()) {
            produceFeces(player);
        } else if (fluid.getFluid() == Fluids.WATER) {
            int amount = fluid.getAmount();
            fluidTank.drain(amount, net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE);
            fluidTank.fill(new FluidStack(ModFluids.FECES_LIQUID.get(), amount), net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE);
            playSplashSound(player);
        } else if (PoopSkyCompat.isFecesLiquid(fluid)) {
            if (fluidTank.getFluidAmount() < fluidTank.getCapacity()) {
                fluidTank.fill(fluid.copyWithAmount(1000), net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE);
            }
            playSplashSound(player);
        } else if (fluid.getFluid() == Fluids.LAVA) {
            playBurnSound(player);
        }
    }

    /**
     * 播放液体飞溅音效
     *
     * @param player 玩家
     */
    private void playSplashSound(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 0.3f, 1.0f);
    }

    /**
     * 播放燃烧音效（马桶内有熔岩时）
     *
     * @param player 玩家
     */
    private void playBurnSound(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.GENERIC_BURN, SoundSource.PLAYERS, 0.3f, 1.0f);
    }

    /**
     * 产生粪便物品实体
     *
     * @param player 玩家
     */
    private void produceFeces(Player player) {
        ItemEntity item = new ItemEntity(
                (ServerLevel) player.level(),
                player.getX(), player.getY(), player.getZ(),
                new ItemStack(ModItems.FECES.get())
        );
        item.setPickUpDelay(8);
        player.level().addFreshEntity(item);
    }

    /**
     * Emits a crushed burst of small brown cloud fragments from the player's feet.
     */
    private void spawnDefecationParticles(ServerLevel level, Player player) {
        for (int burst = 0; burst < DefecationParticleMotion.BURST_COUNT; burst++) {
            double angle = player.getRandom().nextDouble() * Math.TAU;
            double speed = 0.035D + player.getRandom().nextDouble() * 0.065D;
            for (int fragment = 0; fragment < DefecationParticleMotion.FRAGMENT_COUNT; fragment++) {
                double fragmentSpeed = speed * (0.75D + player.getRandom().nextDouble() * 0.30D);
                double verticalSpeed = 0.15D + player.getRandom().nextDouble() * 0.10D;
                DefecationParticleMotion motion = DefecationParticleMotion.fromAngle(
                        angle + DefecationParticleMotion.fragmentAngleOffset(fragment), fragmentSpeed, verticalSpeed);
                level.sendParticles(ModParticles.DEFECATION_CLOUD.get(),
                        player.getX(), player.getY() + 0.28D, player.getZ(),
                        0, motion.xVelocity(), motion.yVelocity(), motion.zVelocity(), 1.0D);
            }
        }
    }

    /**
     * 播放放屁音效
     * 随机选择 fart_1 或 fart_2，并在 0.8 到 1.2 的范围内随机调整音调。
     *
     * @param player 玩家
     */
    private void playFartSound(Player player) {
        SoundEvent fart = player.getRandom().nextBoolean() ? ModSounds.FART_1.get() : ModSounds.FART_2.get();
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), fart, SoundSource.PLAYERS, 1.0f,
                PlayerToiletSound.getFartSoundPitch(player.getRandom().nextFloat()));
    }

    /**
     * 消耗玩家饥饿值
     * 优先消耗饱和度，再消耗饥饿值
     *
     * @param player 玩家
     */
    private void consumeHunger(Player player) {
        if (player.getFoodData().getSaturationLevel() > 0) {
            player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel() - 1f);
        } else if (player.getFoodData().getFoodLevel() > 0) {
            player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 1);
        }
    }

}
