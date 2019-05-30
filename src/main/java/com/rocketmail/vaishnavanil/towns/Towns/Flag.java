package com.rocketmail.vaishnavanil.towns.Towns;

public enum Flag {
    PVP("Pvp Disable"),
    MOBS("Mobs Disable"),
    FIRE("FireSpread Disable"),
    EDIT("Build permission for town members");

    String name;
    Flag(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
}
