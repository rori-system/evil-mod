package com.rori.unboundedcorruption.client.model;

import net.neoforged.neoforge.client.model.data.ModelProperty;

public class ModModelData {

    // Directly above and below
    public static final ModelProperty<BlockType> ABOVE = new ModelProperty<>();
    public static final ModelProperty<BlockType> BELOW = new ModelProperty<>();

    // Horizontal
    public static final ModelProperty<BlockType> NORTH = new ModelProperty<>();
    public static final ModelProperty<BlockType> SOUTH = new ModelProperty<>();
    public static final ModelProperty<BlockType> EAST = new ModelProperty<>();
    public static final ModelProperty<BlockType> WEST = new ModelProperty<>();

    // Upper diagonals (edges)
    public static final ModelProperty<BlockType> NORTH_EAST = new ModelProperty<>();
    public static final ModelProperty<BlockType> NORTH_WEST = new ModelProperty<>();
    public static final ModelProperty<BlockType> SOUTH_EAST = new ModelProperty<>();
    public static final ModelProperty<BlockType> SOUTH_WEST = new ModelProperty<>();

    public static final ModelProperty<BlockType> ABOVE_NORTH = new ModelProperty<>();
    public static final ModelProperty<BlockType> ABOVE_SOUTH = new ModelProperty<>();
    public static final ModelProperty<BlockType> ABOVE_EAST = new ModelProperty<>();
    public static final ModelProperty<BlockType> ABOVE_WEST = new ModelProperty<>();

    // Lower diagonals (edges)
    public static final ModelProperty<BlockType> BELOW_NORTH = new ModelProperty<>();
    public static final ModelProperty<BlockType> BELOW_SOUTH = new ModelProperty<>();
    public static final ModelProperty<BlockType> BELOW_EAST = new ModelProperty<>();
    public static final ModelProperty<BlockType> BELOW_WEST = new ModelProperty<>();

    // Upper corners
    public static final ModelProperty<BlockType> ABOVE_NORTH_EAST = new ModelProperty<>();
    public static final ModelProperty<BlockType> ABOVE_NORTH_WEST = new ModelProperty<>();
    public static final ModelProperty<BlockType> ABOVE_SOUTH_EAST = new ModelProperty<>();
    public static final ModelProperty<BlockType> ABOVE_SOUTH_WEST = new ModelProperty<>();

    // Lower corners
    public static final ModelProperty<BlockType> BELOW_NORTH_EAST = new ModelProperty<>();
    public static final ModelProperty<BlockType> BELOW_NORTH_WEST = new ModelProperty<>();
    public static final ModelProperty<BlockType> BELOW_SOUTH_EAST = new ModelProperty<>();
    public static final ModelProperty<BlockType> BELOW_SOUTH_WEST = new ModelProperty<>();

    public static final ModelProperty<Integer> VARIANT = new ModelProperty<>();
}
