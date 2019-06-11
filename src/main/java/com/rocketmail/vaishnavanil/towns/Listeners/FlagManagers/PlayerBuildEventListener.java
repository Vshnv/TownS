package com.rocketmail.vaishnavanil.towns.Listeners.FlagManagers;

import com.rocketmail.vaishnavanil.towns.Messages.Format;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Flag;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlayerBuildEventListener implements Listener {

    @EventHandler
    public void onBuild(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Location blockloc = event.getBlock().getLocation();
        if (TownS.g().isClaimed(blockloc.getChunk())) {
            Claim claim = TownS.g().getClaim(blockloc.getChunk());


            if (claim.canBuild(player)) return;

            if (claim.getTown().belongs(player)) {

                if (claim.hasFlag(Flag.EDIT)) {
                    return;
                } else {

                    if (claim.getOwner().getUniqueId().equals(player.getUniqueId())) {
                        return;
                    } else {
                        if (claim.getTown().hasPermission("Allow.BuildALL", event.getPlayer())) {
                            return;
                        } else {
                            if (claim.getTown().hasPermission("BuildALL", player.getUniqueId())) {
                                return;
                            }
                            event.setCancelled(true);
                            Format.CmdErrFrmt.use().a(player, "You lack permission to build here!");
                        }
                    }

                }

            } else {
                event.setCancelled(true);
                Format.CmdErrFrmt.use().a(player, "You do not belong to this town!");
            }
        } else {
            if (player.getWorld().getName().equalsIgnoreCase("world") && !player.hasPermission("towns.buildwild")) {
                event.setCancelled(true);
                Format.CmdErrFrmt.use().a(player, "This Area does not belong to you yet!");
            }
        }

    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location blockloc = event.getBlock().getLocation();
        if (TownS.g().isClaimed(blockloc.getChunk())) {
            Claim claim = TownS.g().getClaim(blockloc.getChunk());

            /*Cross Town Container Trust Handler*/
            if(TownS.g().hasTown(player)){
                if(claim.canBuild(event.getPlayer())){
                    return;
                }
            }else{
                event.setCancelled(true);
                Format.CmdErrFrmt.use().a(player, "You do not belong to a town and lack permission to build here.");
                return;
            }

            /*Access Controls for Members within Town.*/
            if (claim.getTown().belongs(player)) {

                if (claim.hasFlag(Flag.EDIT)) {
                    return;
                } else {

                    if (claim.getOwner().getUniqueId().equals(player.getUniqueId())) {
                        return;
                    } else {
                        if (claim.getTown().hasPermission("Allow.BuildALL", event.getPlayer())) {
                            return;
                        } else {
                            if (claim.getTown().hasPermission("BuildALL", player.getUniqueId())) {
                                return;
                            }
                            event.setCancelled(true);
                            Format.CmdErrFrmt.use().a(player, "You lack permission to build here!");
                        }
                    }

                }

            } else {
                event.setCancelled(true);
                Format.CmdErrFrmt.use().a(player, "You do not belong to this town!");
            }
        } else {
            if (player.getWorld().getName().equalsIgnoreCase("world") && !player.hasPermission("towns.buildwild")) {
                event.setCancelled(true);
                Format.CmdErrFrmt.use().a(player, "This Area does not belong to you yet!");
            }
        }

    }


}
