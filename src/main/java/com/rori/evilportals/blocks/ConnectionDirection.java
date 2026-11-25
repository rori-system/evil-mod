package com.rori.evilportals.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

public enum ConnectionDirection {
    NORTH(0, 0, -1),
    SOUTH(0, 0, 1),
    EAST(1, 0, 0),
    WEST(-1, 0, 0),

    UP(0, 1, 0),
    DOWN(0, -1, 0),

    NORTH_EAST(1, 0, -1),
    NORTH_WEST(-1, 0, -1),
    SOUTH_EAST(1, 0, 1),
    SOUTH_WEST(-1, 0, 1),

    UP_NORTH(0, 1, -1),
    UP_SOUTH(0, 1, 1),
    UP_EAST(1, 1, 0),
    UP_WEST(-1, 1, 0),

    DOWN_NORTH(0, -1, -1),
    DOWN_SOUTH(0, -1, 1),
    DOWN_EAST(1, -1, 0),
    DOWN_WEST(-1, -1, 0),

    UP_NORTH_EAST(1, 1, -1),
    UP_NORTH_WEST(-1, 1, -1),
    UP_SOUTH_EAST(1, 1, 1),
    UP_SOUTH_WEST(-1, 1, 1),

    DOWN_NORTH_EAST(1, -1, -1),
    DOWN_NORTH_WEST(-1, -1, -1),
    DOWN_SOUTH_EAST(1, -1, 1),
    DOWN_SOUTH_WEST(-1, -1, 1);

    public final int x;
    public final int y;
    public final int z;

    ConnectionDirection(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos offset(@NotNull BlockPos pos) {
        return pos.offset(x, y, z);
    }

    /**
     * Returns the Minecraft Direction for cardinal directions, or throws for diagonals.
     */
    public @NotNull Direction toDirection() {
        return switch (this) {
            case NORTH -> Direction.NORTH;
            case SOUTH -> Direction.SOUTH;
            case EAST -> Direction.EAST;
            case WEST -> Direction.WEST;
            case UP -> Direction.UP;
            case DOWN -> Direction.DOWN;
            default -> throw new IllegalArgumentException("Diagonal directions have no Minecraft Direction: " + this);
        };
    }

    /**
     * The 6 cardinal (face-adjacent) directions.
     */
    public static final ConnectionDirection[] CARDINAL_DIRECTIONS = {
            NORTH, SOUTH, EAST, WEST, UP, DOWN
    };
}