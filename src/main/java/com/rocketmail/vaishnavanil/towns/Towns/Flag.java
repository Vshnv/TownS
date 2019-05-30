package com.rocketmail.vaishnavanil.towns.Towns;

public enum Flag {

    PVP("Disable PvP"),
    MOBS("Disable Mobs"),
    FIRE("Disable Fire"),
    EDIT("Allow Permission to Build for Town Members"),
    CONTAINER("Allow Town Members access to Containers"),
    EXPLOSION("Allows Explosions"),
    USE("Allow Block Use");

    String name;

    Flag(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
