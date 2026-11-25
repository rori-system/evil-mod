
package com.rori.unboundedcorruption.blocks.blood_slime;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConnectedBlock extends Block {

    // Core shape
    private static final VoxelShape CORE_SHAPE =
            Block.box(5.0, 5.0, 5.0, 11.0, 11.0, 11.0);

    // Connection hitboxes
    private static final VoxelShape NORTH_SHAPE =
            Block.box(1.0, 1.0, 0.0, 15.0, 15.0, 1.0);

    private static final VoxelShape SOUTH_SHAPE =
            Block.box(1.0, 1.0, 15.0, 15.0, 15.0, 16.0);

    private static final VoxelShape EAST_SHAPE =
            Block.box(15.0, 1.0, 1.0, 16.0, 15.0, 15.0);

    private static final VoxelShape WEST_SHAPE =
            Block.box(0.0, 1.0, 1.0, 1.0, 15.0, 15.0);

    private static final VoxelShape UP_SHAPE =
            Block.box(1.0, 15.0, 1.0, 15.0, 16.0, 15.0);

    private static final VoxelShape DOWN_SHAPE =
            Block.box(1.0, 0.0, 1.0, 15.0, 1.0, 15.0);

    public ConnectedBlock(Properties properties) {
        super(properties);
    }

    // Placement and survival

    @Override
    protected boolean canSurvive(
            @NotNull BlockState state,
            @NotNull LevelReader level,
            @NotNull BlockPos pos
    ) {
        return hasSolidSurface(level, pos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(
            @NotNull net.minecraft.world.item.context.BlockPlaceContext context
    ) {
        BlockState state = defaultBlockState();

        return canSurvive(state, context.getLevel(), context.getClickedPos())
                ? state
                : null;
    }

    public static boolean hasSolidSurface(
            @NotNull LevelReader level,
            @NotNull BlockPos pos
    ) {
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);

            if (neighborState.isAir()) {
                continue;
            }

            // Connected slime blocks cannot support this block.
            if (neighborState.getBlock() instanceof ConnectedBlock) {
                continue;
            }

            if (neighborState.isFaceSturdy(
                    level,
                    neighborPos,
                    direction.getOpposite()
            )) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void neighborChanged(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Block neighborBlock,
            @NotNull BlockPos neighborPos,
            boolean isMoving
    ) {
        super.neighborChanged(
                state, level, pos, neighborBlock, neighborPos, isMoving
        );

        if (!level.isClientSide && !canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    // Hitboxes

    @Override
    protected @NotNull VoxelShape getShape(
            @NotNull BlockState state,
            @NotNull net.minecraft.world.level.BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context
    ) {
        return buildShapeFromConnections(level, pos);
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(
            @NotNull BlockState state,
            @NotNull net.minecraft.world.level.BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context
    ) {
        return Shapes.empty();
    }

    private @NotNull VoxelShape buildShapeFromConnections(
            @NotNull net.minecraft.world.level.BlockGetter level,
            @NotNull BlockPos pos
    ) {
        VoxelShape shape = Shapes.empty();

        boolean hasNorth = isFaceConnected(level, pos, Direction.NORTH);
        boolean hasSouth = isFaceConnected(level, pos, Direction.SOUTH);
        boolean hasEast = isFaceConnected(level, pos, Direction.EAST);
        boolean hasWest = isFaceConnected(level, pos, Direction.WEST);
        boolean hasUp = isFaceConnected(level, pos, Direction.UP);
        boolean hasDown = isFaceConnected(level, pos, Direction.DOWN);

        boolean hasAnyConnection =
                hasNorth || hasSouth || hasEast ||
                        hasWest || hasUp || hasDown;

        if (!hasAnyConnection) {
            return CORE_SHAPE;
        }

        if (hasNorth) shape = Shapes.or(shape, NORTH_SHAPE);
        if (hasSouth) shape = Shapes.or(shape, SOUTH_SHAPE);
        if (hasEast) shape = Shapes.or(shape, EAST_SHAPE);
        if (hasWest) shape = Shapes.or(shape, WEST_SHAPE);
        if (hasUp) shape = Shapes.or(shape, UP_SHAPE);
        if (hasDown) shape = Shapes.or(shape, DOWN_SHAPE);

        return shape;
    }

    private boolean isFaceConnected(
            @NotNull net.minecraft.world.level.BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull Direction direction
    ) {
        BlockPos neighborPos = pos.relative(direction);
        BlockState neighborState = level.getBlockState(neighborPos);

        if (neighborState.isAir()) {
            return false;
        }

        return neighborState.isFaceSturdy(
                level,
                neighborPos,
                direction.getOpposite()
        );
    }
}
