package com.rocketmail.vaishnavanil.towns.Utilities;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public enum Protocol {
    INSTANCE;

    public void sendRedstoneParticle(Player player, Location location, Color color){
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 1);
        player.spawnParticle(Particle.REDSTONE, location.getX(), location.getY(), location.getZ(), 0, 0, 0, 0.1, 0.1 , dustOptions);
    }

}
