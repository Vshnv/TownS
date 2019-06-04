package com.rocketmail.vaishnavanil.towns.Utilities;

import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public enum ParticleManager {
    INSTANCE;

    public void sendRedstoneParticle(Player player, Location location, Color color){
        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1);
        player.spawnParticle(Particle.REDSTONE, location.getX(), location.getY(), location.getZ(), 0, 0, 0, 0.1, 0.1 , dustOptions);
    }

    public void drawLine(Player player, Location point1, Location point2, double space, Color color) {
        World world = point1.getWorld();
        Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        double length = 0;
        for (; length < distance; p1.add(vector)) {
            Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1);
            ParticleManager.INSTANCE.sendRedstoneParticle(player, new Location(player.getWorld(),p1.getX(), p1.getY(), p1.getZ()), color);
            //world.spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 1, dustOptions);
            length += space;
        }
    }

    public void drawChunkBorder(Player player, Chunk chunk, Color color){
        Float height = 0.6F;
        Location point1 = new Location(player.getWorld(), chunk.getX()*16, player.getLocation().add(0,height,0).getY(),chunk.getZ()*16);
        Location point2 = new Location(player.getWorld(), chunk.getX()*16+16, player.getLocation().add(0,height,0).getY(),chunk.getZ()*16);
        Location point3 = new Location(player.getWorld(), chunk.getX()*16, player.getLocation().add(0,height,0).getY(),chunk.getZ()*16+16);
        Location point4 = new Location(player.getWorld(), chunk.getX()*16+16, player.getLocation().add(0,height,0).getY(),chunk.getZ()*16+16);
        Float space = 0.2F;
        drawLine(player, point1,point2, space, color);
        drawLine(player, point1, point3, space, color);
        drawLine(player, point2, point4, space, color);
        drawLine(player, point3, point4, space, color);
    }

}
