package com.rocketmail.vaishnavanil.towns.Listeners.FlagManagers;

import com.rocketmail.vaishnavanil.towns.Listeners.Constraints;
import com.rocketmail.vaishnavanil.towns.Messages.Format;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Flag;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ContainerUseEventListener implements Listener {
    @EventHandler
    public void onContainerUse(PlayerInteractEvent e){

        if(e.getPlayer().hasPermission("towns.override")) return;

        if(!(e.getAction() == Action.PHYSICAL || e.getAction() == Action.RIGHT_CLICK_BLOCK))return;

        if(!TownS.g().isClaimed(e.getClickedBlock().getLocation().getChunk()))return;

        if(Constraints.Container.isRestricted(e.getClickedBlock().getType())){

            Player player = e.getPlayer();
            Claim claim = TownS.g().getClaim(e.getClickedBlock().getLocation().getChunk());

            /*Cross Town Container Trust Handler*/
            if(TownS.g().hasTown(player)){
                if(claim.canUseContainer(e.getPlayer())){
                    return;
                }
            }else{
                e.setCancelled(true);
                Format.CmdErrFrmt.use().a(player, "You do not belong to a town and lack permission to open containers here.");
                return;
            }

            /*Access Controls for Members within Town*/
            if(claim.getTown().belongs(player)){

                if(claim.hasFlag(Flag.CONTAINER)){
                    return;
                }else{

                    if(claim.getOwner().getUniqueId().equals(player.getUniqueId())){
                        return;
                    }else{
                        if(claim.getTown().hasPermission("Allow.ContainerALL",e.getPlayer())){
                            return;
                        }else{
                            if(claim.getTown().hasPermission("ContainerALL", player.getUniqueId())){
                                player.sendMessage(ChatColor.RED+"Opening container using override permission.");
                                return;
                            }
                            e.setCancelled(true);
                            Format.CmdErrFrmt.use().a(player, "You lack permission to open containers here!");
                        }
                    }
                }

            }else{
                e.setCancelled(true);
                Format.CmdErrFrmt.use().a(player, "You do not belong to this town!");
            }

        }

    }
}
