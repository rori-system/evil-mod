package com.rori.evilportals.blocks;

import net.minecraft.util.StringRepresentable;

public enum ConnectionType implements StringRepresentable {
    SOLID("solid"),
    SIDE("side"),
    NONE("none");

    private final String name;

    ConnectionType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.getSerializedName();
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public boolean isConnected() {
        return this != NONE;
    }
    public boolean isSolid() {
        return this == SOLID;
    }
}
