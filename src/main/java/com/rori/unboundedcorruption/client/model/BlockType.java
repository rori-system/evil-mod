package com.rori.unboundedcorruption.client.model;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum BlockType implements StringRepresentable {
    SOLID("solid"),
    SAME("same"),
    AIR("air");

    private final String name;

    BlockType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.getSerializedName();
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public boolean exists() {
        return this != AIR;
    }
    public boolean isSame() { return this == SAME; }
    public boolean isSolid() {
        return this == SOLID;
    }
}
