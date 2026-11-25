package com.rori.unboundedcorruption.blocks.other;

import com.rori.unboundedcorruption.blocks.ConnectionDirection;
import com.rori.unboundedcorruption.blocks.ConnectionType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConnectedBlockOld extends Block {

    // Core shape — shown when block has no connections
    private static final VoxelShape CORE_SHAPE = Block.box(5.0, 5.0, 5.0, 11.0, 11.0, 11.0);

    public static final EnumProperty<ConnectionType> NORTH =
            EnumProperty.create("north", ConnectionType.class);

    public static final EnumProperty<ConnectionType> SOUTH =
            EnumProperty.create("south", ConnectionType.class);

    public static final EnumProperty<ConnectionType> EAST =
            EnumProperty.create("east", ConnectionType.class);

    public static final EnumProperty<ConnectionType> WEST =
            EnumProperty.create("west", ConnectionType.class);

    public static final EnumProperty<ConnectionType> UP =
            EnumProperty.create("up", ConnectionType.class);

    public static final EnumProperty<ConnectionType> DOWN =
            EnumProperty.create("down", ConnectionType.class);

    private static final VoxelShape NORTH_SHAPE = Block.box(3.0, 3.0, 0.0, 13.0, 13.0, 1.0);
    private static final VoxelShape SOUTH_SHAPE = Block.box(3.0, 3.0, 15.0, 13.0, 13.0, 16.0);
    private static final VoxelShape EAST_SHAPE = Block.box(15.0, 3.0, 3.0, 16.0, 13.0, 13.0);
    private static final VoxelShape WEST_SHAPE = Block.box(0.0, 3.0, 3.0, 1.0, 13.0, 13.0);
    private static final VoxelShape UP_SHAPE = Block.box(3.0, 15.0, 3.0, 13.0, 16.0, 13.0);
    private static final VoxelShape DOWN_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 13.0);

    public ConnectedBlockOld(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, ConnectionType.NONE)
                .setValue(SOUTH, ConnectionType.NONE)
                .setValue(EAST, ConnectionType.NONE)
                .setValue(WEST, ConnectionType.NONE)
                .setValue(UP, ConnectionType.NONE)
                .setValue(DOWN, ConnectionType.NONE)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        for (ConnectionDirection direction : ConnectionDirection.CARDINAL_DIRECTIONS) {
            BlockPos neighborPos = direction.offset(pos);
            BlockState neighborState = level.getBlockState(neighborPos);
            Direction face = direction.toDirection();
            if (neighborState.isFaceSturdy(level, neighborPos, face.getOpposite())) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        if (!this.canSurvive(this.defaultBlockState(), context.getLevel(), context.getClickedPos())) {
            return null;
        }

        BlockState defaultState = this.defaultBlockState();
        LevelAccessor level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        BlockState state = defaultState
                .setValue(NORTH, classifyConnection(level, pos, ConnectionDirection.NORTH))
                .setValue(SOUTH, classifyConnection(level, pos, ConnectionDirection.SOUTH))
                .setValue(EAST, classifyConnection(level, pos, ConnectionDirection.EAST))
                .setValue(WEST, classifyConnection(level, pos, ConnectionDirection.WEST))
                .setValue(UP, classifyConnection(level, pos, ConnectionDirection.UP))
                .setValue(DOWN, classifyConnection(level, pos, ConnectionDirection.DOWN));

        // Apply diagonal connection propagation
        state = applyDiagonalConnections(state, level, pos);
        return state;
    }

    @Override
    protected @NotNull BlockState updateShape(
            @NotNull BlockState state,
            @NotNull Direction direction,
            @NotNull BlockState neighborState,
            @NotNull LevelAccessor level,
            @NotNull BlockPos pos,
            @NotNull BlockPos neighborPos
    ) {
        return rebuildState(state, level, pos);
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
        if (isDiagonalNeighbor(pos, neighborPos)) {
            BlockState newState = rebuildState(state, level, pos);
            if (!newState.equals(state)) {
                level.setBlock(pos, newState, 3);
            }
        }
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, isMoving);
    }

    @Override
    public void onNeighborChange(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (isDiagonalNeighbor(pos, neighborPos) && level instanceof LevelAccessor levelAccessor) {
            BlockState newState = rebuildState(state, levelAccessor, pos);
            if (!newState.equals(state)) {
                levelAccessor.setBlock(pos, newState, 3);
            }
        }
    }

    /**
     * Notifies all 12 face-diagonal neighbors that contain the same block type to rebuild their state.
     */
    private void notifyDiagonalNeighbors(@NotNull LevelAccessor level, @NotNull BlockPos pos) {
        for (ConnectionDirection direction : DIAGONAL_DIRECTIONS) {
            BlockPos neighborPos = direction.offset(pos);
            BlockState neighborState = level.getBlockState(neighborPos);
            if (neighborState.getBlock() == this) {
                BlockState newState = rebuildState(neighborState, level, neighborPos);
                if (!newState.equals(neighborState)) {
                    level.setBlock(neighborPos, newState, 3);
                }
            }
        }
    }

    /**
     * Returns true if the neighbor position is a face-diagonal of this position.
     */
    private boolean isDiagonalNeighbor(@NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        int dx = Math.abs(neighborPos.getX() - pos.getX());
        int dy = Math.abs(neighborPos.getY() - pos.getY());
        int dz = Math.abs(neighborPos.getZ() - pos.getZ());
        // Face-diagonal: exactly 2 axes differ by 1, one axis is 0
        int nonZero = (dx == 1 ? 1 : 0) + (dy == 1 ? 1 : 0) + (dz == 1 ? 1 : 0);
        return nonZero == 2;
    }

    @Override
    public void setPlacedBy(
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull BlockState state,
            @Nullable LivingEntity placer,
            @NotNull ItemStack stack
    ) {
        super.setPlacedBy(level, pos, state, placer, stack);
        notifyDiagonalNeighbors(level, pos);
    }

    @Override
    public void onRemove(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull BlockState newState,
            boolean isMoving
    ) {
        super.onRemove(state, level, pos, newState, isMoving);
        notifyDiagonalNeighbors(level, pos);
    }

    /**
     * Recalculates all 6 direction connections and applies diagonal propagation,
     * returning the updated state. Used by updateShape, neighborChanged, and diagonal notifications.
     */
    private @NotNull BlockState rebuildState(@NotNull BlockState state, @NotNull LevelAccessor level, @NotNull BlockPos pos) {
        BlockState newState = state
                .setValue(NORTH, classifyConnection(level, pos, ConnectionDirection.NORTH))
                .setValue(SOUTH, classifyConnection(level, pos, ConnectionDirection.SOUTH))
                .setValue(EAST, classifyConnection(level, pos, ConnectionDirection.EAST))
                .setValue(WEST, classifyConnection(level, pos, ConnectionDirection.WEST))
                .setValue(UP, classifyConnection(level, pos, ConnectionDirection.UP))
                .setValue(DOWN, classifyConnection(level, pos, ConnectionDirection.DOWN));

        // Apply diagonal connection propagation
        newState = applyDiagonalConnections(newState, level, pos);

        if (!newState.equals(state)) {
            level.setBlock(pos, newState, 3);
        }
        return newState;
    }

    @Override
    protected @NotNull VoxelShape getShape(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context
    ) {
        return buildShapeFromState(state);
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context
    ) {
        return buildShapeFromState(state);
    }

    /**
     * Classifies the connection type for a neighbor in a given direction.
     */
    private ConnectionType classifyConnection(BlockGetter level, BlockPos pos, ConnectionDirection direction) {
        BlockPos neighborPos = direction.offset(pos);
        BlockState neighborState = level.getBlockState(neighborPos);

        if (neighborState.isAir()) {
            return ConnectionType.NONE;
        }

        Direction mcDirection = toMinecraftDirection(direction);
        if (neighborState.isFaceSturdy(level, neighborPos, mcDirection.getOpposite())) {
            return ConnectionType.SOLID;
        }

        if (!neighborState.getCollisionShape(level, neighborPos).isEmpty()) {
            return ConnectionType.SIDE;
        }

        return ConnectionType.NONE;
    }

    /**
     * Maps ConnectionDirection to Minecraft Direction enum.
     */
    private @NotNull Direction toMinecraftDirection(ConnectionDirection direction) {
        return switch (direction) {
            case NORTH -> Direction.NORTH;
            case SOUTH -> Direction.SOUTH;
            case EAST -> Direction.EAST;
            case WEST -> Direction.WEST;
            case UP -> Direction.UP;
            case DOWN -> Direction.DOWN;
            default -> throw new IllegalArgumentException("Diagonal directions not supported: " + direction);
        };
    }

    /**
     * The 12 face-diagonal directions (excluding the 6 cardinal and 8 corner diagonals).
     */
    private static final ConnectionDirection[] DIAGONAL_DIRECTIONS = {
            ConnectionDirection.NORTH_EAST,
            ConnectionDirection.NORTH_WEST,
            ConnectionDirection.SOUTH_EAST,
            ConnectionDirection.SOUTH_WEST,
            ConnectionDirection.UP_NORTH,
            ConnectionDirection.UP_SOUTH,
            ConnectionDirection.UP_EAST,
            ConnectionDirection.UP_WEST,
            ConnectionDirection.DOWN_NORTH,
            ConnectionDirection.DOWN_SOUTH,
            ConnectionDirection.DOWN_EAST,
            ConnectionDirection.DOWN_WEST
    };

    /**
     * Maps each diagonal direction to its two component cardinal directions.
     * For example, NORTH_EAST → (NORTH, EAST).
     */
    private static final DiagonalPair[] DIAGONAL_PAIRS = {
            new DiagonalPair(ConnectionDirection.NORTH_EAST, NORTH, EAST),
            new DiagonalPair(ConnectionDirection.NORTH_WEST, NORTH, WEST),
            new DiagonalPair(ConnectionDirection.SOUTH_EAST, SOUTH, EAST),
            new DiagonalPair(ConnectionDirection.SOUTH_WEST, SOUTH, WEST),
            new DiagonalPair(ConnectionDirection.UP_NORTH, UP, NORTH),
            new DiagonalPair(ConnectionDirection.UP_SOUTH, UP, SOUTH),
            new DiagonalPair(ConnectionDirection.UP_EAST, UP, EAST),
            new DiagonalPair(ConnectionDirection.UP_WEST, UP, WEST),
            new DiagonalPair(ConnectionDirection.DOWN_NORTH, DOWN, NORTH),
            new DiagonalPair(ConnectionDirection.DOWN_SOUTH, DOWN, SOUTH),
            new DiagonalPair(ConnectionDirection.DOWN_EAST, DOWN, EAST),
            new DiagonalPair(ConnectionDirection.DOWN_WEST, DOWN, WEST)
    };

    /**
     * Represents a diagonal direction and its two component cardinal EnumProperties.
     */
    private record DiagonalPair(
            ConnectionDirection diagonal,
            EnumProperty<ConnectionType> first,
            EnumProperty<ConnectionType> second
    ) {}

    /**
     * Applies diagonal connection propagation: if one cardinal direction is SOLID and the
     * diagonal between it and another cardinal has the same block, upgrade the second cardinal
     * to SIDE if it is currently NONE.
     *
     * For each diagonal, both orderings are checked:
     * - If first is SOLID and diagonal has same block → second becomes SIDE (if NONE)
     * - If second is SOLID and diagonal has same block → first becomes SIDE (if NONE)
     */
    private @NotNull BlockState applyDiagonalConnections(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        for (DiagonalPair pair : DIAGONAL_PAIRS) {
            boolean diagonalHasSameBlock = isSameBlock(level, pos, pair.diagonal);
            if (!diagonalHasSameBlock) continue;

            ConnectionType firstType = state.getValue(pair.first);
            ConnectionType secondType = state.getValue(pair.second);

            // If first is SOLID and second is NONE, upgrade second to SIDE
            if (firstType == ConnectionType.SOLID && secondType == ConnectionType.NONE) {
                state = state.setValue(pair.second, ConnectionType.SIDE);
            }
            // If second is SOLID and first is NONE, upgrade first to SIDE
            if (secondType == ConnectionType.SOLID && firstType == ConnectionType.NONE) {
                state = state.setValue(pair.first, ConnectionType.SIDE);
            }
        }
        return state;
    }

    /**
     * Holds the result of checking all 12 face-diagonal neighbors for same-block connections.
     */
    public record DiagonalConnections(
            boolean northEast, boolean northWest, boolean southEast, boolean southWest,
            boolean upNorth, boolean upSouth, boolean upEast, boolean upWest,
            boolean downNorth, boolean downSouth, boolean downEast, boolean downWest
    ) {
        /** Returns true if any diagonal neighbor is the same block. */
        public boolean any() {
            return northEast || northWest || southEast || southWest ||
                   upNorth || upSouth || upEast || upWest ||
                   downNorth || downSouth || downEast || downWest;
        }
    }

    /**
     * Checks all 12 face-diagonal neighbor positions and returns whether each is the same block type.
     */
    public @NotNull DiagonalConnections getDiagonalConnections(@NotNull BlockGetter level, @NotNull BlockPos pos) {
        return new DiagonalConnections(
                isSameBlock(level, pos, ConnectionDirection.NORTH_EAST),
                isSameBlock(level, pos, ConnectionDirection.NORTH_WEST),
                isSameBlock(level, pos, ConnectionDirection.SOUTH_EAST),
                isSameBlock(level, pos, ConnectionDirection.SOUTH_WEST),
                isSameBlock(level, pos, ConnectionDirection.UP_NORTH),
                isSameBlock(level, pos, ConnectionDirection.UP_SOUTH),
                isSameBlock(level, pos, ConnectionDirection.UP_EAST),
                isSameBlock(level, pos, ConnectionDirection.UP_WEST),
                isSameBlock(level, pos, ConnectionDirection.DOWN_NORTH),
                isSameBlock(level, pos, ConnectionDirection.DOWN_SOUTH),
                isSameBlock(level, pos, ConnectionDirection.DOWN_EAST),
                isSameBlock(level, pos, ConnectionDirection.DOWN_WEST)
        );
    }

    /**
     * Checks if the neighbor in the given direction is the same block type as this block.
     */
    private boolean isSameBlock(@NotNull BlockGetter level, @NotNull BlockPos pos, ConnectionDirection direction) {
        BlockPos neighborPos = direction.offset(pos);
        BlockState neighborState = level.getBlockState(neighborPos);
        return neighborState.getBlock() == this;
    }

    /**
     * Builds the voxel shape from the block state's connection properties using Shapes.or.
     */
    private @NotNull VoxelShape buildShapeFromState(@NotNull BlockState state) {
        VoxelShape shape = Shapes.empty();

        boolean hasNorth = state.getValue(NORTH).isSolid();
        boolean hasSouth = state.getValue(SOUTH).isSolid();
        boolean hasEast = state.getValue(EAST).isSolid();
        boolean hasWest = state.getValue(WEST).isSolid();
        boolean hasUp = state.getValue(UP).isSolid();
        boolean hasDown = state.getValue(DOWN).isSolid();

        boolean hasAnyConnection = hasNorth || hasSouth || hasEast || hasWest || hasUp || hasDown;

        // Show core shape only when isolated (no connections)
        if (!hasAnyConnection) {
            return CORE_SHAPE;
        }

        // Combine direction shapes using Shapes.or
        if (hasNorth) shape = Shapes.or(shape, NORTH_SHAPE);
        if (hasSouth) shape = Shapes.or(shape, SOUTH_SHAPE);
        if (hasEast) shape = Shapes.or(shape, EAST_SHAPE);
        if (hasWest) shape = Shapes.or(shape, WEST_SHAPE);
        if (hasUp) shape = Shapes.or(shape, UP_SHAPE);
        if (hasDown) shape = Shapes.or(shape, DOWN_SHAPE);

        return shape;
    }

}
