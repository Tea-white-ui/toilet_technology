package tea.toilet.technology.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import tea.toilet.technology.ToiletTechnology;

import static tea.toilet.technology.ToiletTechnology.LOGGER;

public class ModBlocks {

    /**
     * 注册一个方块示例
     */
    public static final Block FECES_BLOCK = registerBlock(
            ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, "feces_block")),
            new Block(Block.Properties.of()
                    //.mapColor(MapColor.NONE) // 地图颜色：无
                    .destroyTime(1.0f) // 破坏时间：1秒
                    .explosionResistance(0.5f) // 爆炸抗性
                    .sound(SoundType.MUD) // 音效类型：泥巴
                    //.lightLevel((state) -> 0) // 光照等级：0（不发光）
                    .friction(0.7f) // 摩擦系数：(0.6默认值）
                    .speedFactor(0.9f) // 速度因子：(1.0默认速度）
                    //.jumpFactor(1.0f) // 跳跃因子：1.0（默认跳跃高度）
                    // .noOcclusion() // 无遮挡：不阻挡光线和视线，修复复杂方块的渲染问题
                    .pushReaction(PushReaction.NORMAL) // 活塞推动反应：正常
                    // .ignitedByLava() // 可被岩浆点燃
                    //.replaceable() // 可替换：玩家可以在其位置直接放置其他方块
                    //.noLootTable() // 无掉落物表：破坏后不掉落任何物品

                    // === 常用组合 ===
                    // .strength(0.0f, 0.0f) // 快捷设置：破坏时间+爆炸抗性（等同于同时设置上面两项）
                    // .strength(1.5f, 6.0f) // 快捷设置：如圆石（破坏1.5秒，爆炸抗性6.0）

                    // === 碰撞与实体交互 ===
                    // .noCollission() // 无碰撞：实体可以穿过该方块（如水、花）
                    // .hasPostProcess((state, world, pos) -> true) // 后处理：启用方块的后处理渲染效果

                    // === 方块状态与渲染 ===
                    // .dynamicShape() // 动态形状：方块形状可以变化（用于不同状态有不同模型的方块）
                    // .isValidSpawn((state, world, pos, type) -> false) // 生物生成：是否允许生物在此方块上生成（默认true）
                    // .isRedstoneConductor((state, world, pos) -> false) // 红石导体：是否传导红石信号（默认true）
                    // .isSuffocating((state, world, pos) -> false) // 窒息：是否会对玩家造成窒息伤害（默认true）
                    // .isViewBlocking((state, world, pos) -> false) // 视线阻挡：是否阻挡玩家视线（默认与noOcclusion相关）

                    // === 特殊行为 ===
                    // .requiresCorrectToolForDrops() // 需要正确工具：只有用正确工具挖掘才会掉落物品
                    // .instabreak() // 瞬间破坏：创造模式下瞬间破坏（等同于destroyTime(0)）
                    // .air() // 空气属性：无碰撞、不阻挡、不可交互（类似空气方块）
                    // .pushReaction(PushReaction.BLOCK) // 活塞反应：BLOCK=活塞无法推动; DESTROY=活塞破坏方块; IGNORE=活塞忽略
                    // .offsetType(Block.OffsetType.XZ) // 偏移类型：方块可以在XZ方向上微调偏移（如花草的随机偏移）
            )
    );

    public static final Block SQUAT_TOILET = registerBlock(
            ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, "squat_toilet")),
            new Block(Block.Properties.of()
                    .destroyTime(1.0f)
                    .explosionResistance(1.0f)
                    .sound(SoundType.STONE)
                    .pushReaction(PushReaction.NORMAL)
                    .isValidSpawn((state, world, pos, type) -> false)
                    .isRedstoneConductor((state, world, pos) -> false)
                    .requiresCorrectToolForDrops()
            ){
                @Override
                public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
                    return Shapes.or(
                            box(0, 0, 0, 16, 12, 16),
                            box(0, 12, 0, 5, 16, 16),
                            box(11, 12, 0, 16, 16, 16),
                            box(5, 12, 0, 11, 16, 3),
                            box(5, 12, 13, 11, 16, 16)
                    );
                }
            }
    );

    /**
     * 方块注册入口
     */
    public static Block registerBlock(ResourceKey<Block> resourceKey, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, resourceKey, block);
    }


    public static void registerModBlocks(){
        LOGGER.info("toilet_technology Blocks registered");
    }

}
