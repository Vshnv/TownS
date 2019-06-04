package com.rocketmail.vaishnavanil.towns.Listeners.TitleManager;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Utilities.Protocol;
import com.rocketmail.vaishnavanil.towns.Utilities.TitleSender;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class MoveEventListener implements Listener {

    @EventHandler
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event) {
        if (event.getFrom().getChunk() != event.getTo().getChunk()) {

            Player player = event.getPlayer();
            Chunk fromChunk = event.getFrom().getChunk();
            Chunk toChunk = event.getTo().getChunk();

            Claim fromClaim = TownS.g().getClaim(fromChunk);
            Claim toClaim = TownS.g().getClaim(toChunk);

            Boolean fromChunkClaimed = TownS.g().isClaimed(fromChunk);
            Boolean toChunkClaimed = TownS.g().isClaimed(toChunk);

            if (!toChunkClaimed && fromChunkClaimed) {
                //Player Entered Wilderness from Claimed Region
                TitleSender.INSTANCE.sendTitle(player, "", "&2Entering Wilderness");
                drawChunkBorder(player, fromChunk);
            } else if (toChunkClaimed && !fromChunkClaimed) {
                //Player Entered Claimed Area from Wilderness
                toClaim.setName("l33t Plot Name");
                // ^ Setting Name is just for testing.
                String plot_owner = toClaim.getOwner().getName();
                String plot_name = toClaim.getName();
                String plot_town = toClaim.getTown().getName();
                TitleSender.INSTANCE.sendTitle(player, "&a" + plot_name, "&cTown: &6" + plot_town + " &8&l| &eOwner: &6" + plot_owner);
                drawChunkBorder(player, toChunk);
            } else if (toChunkClaimed && fromChunkClaimed) {
                String plot_owner = toClaim.getOwner().getName();
                String plot_name = toClaim.getName();
                String plot_town = toClaim.getTown().getName();

                if(fromClaim.getOwner().equals(toClaim.getOwner()) && toClaim.getOwner().equals(player)){
                    if (!plot_name.equals("")) {
                        TitleSender.INSTANCE.sendTitle(player, "", "&2Area: &a" + plot_name);
                    }
                }else{
                    if (plot_name.equals("")) {
                        TitleSender.INSTANCE.sendTitle(player, "", "&2Owner: &a" + plot_owner);
                    } else {
                        TitleSender.INSTANCE.sendTitle(player, "", "&2Area: &a" + plot_name + " &8&l| &eOwner: &6" + plot_owner);
                    }
                }
                drawChunkBorder(player, toChunk);

            }

        }
    }

    public void drawLine(Player player, Location point1, Location point2, double space) {
        World world = point1.getWorld();
        Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        double length = 0;
        for (; length < distance; p1.add(vector)) {
            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 1);
            Protocol.INSTANCE.sendRedstoneParticle(player, new Location(player.getWorld(),p1.getX(), p1.getY(), p1.getZ()), Color.WHITE);
            //world.spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 1, dustOptions);
            length += space;
        }
    }

    public void drawChunkBorder(Player player, Chunk chunk){
        Float height = 0.6F;
        Location point1 = new Location(player.getWorld(), chunk.getX()*16, player.getLocation().add(0,height,0).getY(),chunk.getZ()*16);
        Location point2 = new Location(player.getWorld(), chunk.getX()*16+16, player.getLocation().add(0,height,0).getY(),chunk.getZ()*16);
        Location point3 = new Location(player.getWorld(), chunk.getX()*16, player.getLocation().add(0,height,0).getY(),chunk.getZ()*16+16);
        Location point4 = new Location(player.getWorld(), chunk.getX()*16+16, player.getLocation().add(0,height,0).getY(),chunk.getZ()*16+16);
        Float space = 0.2F;
        drawLine(player, point1,point2, space);
        drawLine(player, point1, point3, space);
        drawLine(player, point2, point4, space);
        drawLine(player, point3, point4, space);
    }

}
