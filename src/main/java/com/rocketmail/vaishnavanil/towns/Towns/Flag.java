package com.rocketmail.vaishnavanil.towns.Towns;

public enum Flag {

    PVP("Allows PvP"), //DONE
    MOBS("Allows Mobs"),//DONE
    FIRE_PROTECTION("Disable Fire Damage and Spread"), //DONE
    EDIT("Allow Permission to Build for Town Members"), //DONE
    CONTAINER("Allow Town Members access to Containers"),
    EXPLOSION("Allows Explosions"), //DONE????
    USE("Allow Block Use"); //DONE

    String name;

    Flag(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static Flag getFlag(String name){
        for(Flag f:Flag.values()){
            if(f.getName().equals(name)){
                return f;
            }
        }
        return null;
    }
}
