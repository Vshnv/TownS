package com.rocketmail.vaishnavanil.towns.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.Serializable;

public class WarpLocation implements Serializable {
    public static final long serialVerisionUID = 345910747105L;

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private String world_name;
    public WarpLocation(Location location){
        x = location.getX();
        y = location.getY();
        z = location.getZ();
        yaw = location.getYaw();
        pitch = location.getPitch();
        world_name = location.getWorld().getName();
    }
    public Location getLocation(){
        return new Location(Bukkit.getWorld(world_name),x,y,z, yaw , pitch);
    }
}
