package cn.tea.toilet.technology.block.toilet;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

/**
 * 马桶方块抽象基类
 * 定义所有马桶方块的通用属性和行为：
 * - 支持4个水平方向（NORTH, SOUTH, EAST, WEST）
 * - 定义马桶的碰撞箱形状（根据方向变化）
 * - 继承自 BaseEntityBlock，支持方块实体
 */
public abstract class ToiletBlock extends BaseEntityBlock {

    /** 构造函数：初始化方块属性 */
    protected ToiletBlock(Properties properties) {
        super(properties);
    }

    /** 方块朝向属性：水平方向 */
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    /** 马桶碰撞箱形状 - 朝北 */
    protected static final VoxelShape SHAPE_NORTH = Shapes.or(
            box(0, 0, 0, 16, 12, 16),
            box(0, 12, 0, 5, 16, 16),
            box(11, 12, 0, 16, 16, 16),
            box(5, 12, 0, 11, 16, 3),
            box(5, 12, 13, 11, 16, 16)
    );
    /** 马桶碰撞箱形状 - 朝南 */
    protected static final VoxelShape SHAPE_SOUTH = Shapes.or(
            box(0, 0, 0, 16, 12, 16),
            box(11, 12, 0, 16, 16, 16),
            box(0, 12, 0, 5, 16, 16),
            box(5, 12, 13, 11, 16, 16),
            box(5, 12, 0, 11, 16, 3)
    );
    /** 马桶碰撞箱形状 - 朝西 */
    protected static final VoxelShape SHAPE_WEST = Shapes.or(
            box(0, 0, 0, 16, 12, 16),
            box(0, 12, 11, 16, 16, 16),
            box(0, 12, 0, 16, 16, 5),
            box(0, 12, 5, 3, 16, 11),
            box(13, 12, 5, 16, 16, 11)
    );
    /** 马桶碰撞箱形状 - 朝东 */
    protected static final VoxelShape SHAPE_EAST = Shapes.or(
            box(0, 0, 0, 16, 12, 16),
            box(0, 12, 0, 16, 16, 5),
            box(0, 12, 11, 16, 16, 16),
            box(13, 12, 5, 16, 16, 11),
            box(0, 12, 5, 3, 16, 11)
    );

    /**
     * 创建方块状态定义：添加 FACING 属性
     * 
     * @param builder 方块状态构建器
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    /**
     * 获取放置时的方块状态：根据玩家朝向设置方向
     * 
     * @param context 放置上下文
     * @return 放置后的方块状态
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    /**
     * 获取方块碰撞箱形状：根据方向返回对应形状
     * 
     * @param state 方块状态
     * @param level 世界
     * @param pos 方块位置
     * @param context 碰撞上下文
     * @return 碰撞箱形状
     */
    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return getToiletShape(state);
    }

    /**
     * 根据方块状态获取对应的马桶形状
     * 
     * @param state 方块状态
     * @return 对应方向的碰撞箱形状
     */
    protected @NotNull VoxelShape getToiletShape(BlockState state) {
        return switch (state.getValue(FACING)) {
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            default -> SHAPE_NORTH;
        };
    }

    /**
     * 获取渲染形状：使用 MODEL 渲染（支持自定义模型）
     * 
     * @param state 方块状态
     * @return 渲染形状类型
     */
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

}