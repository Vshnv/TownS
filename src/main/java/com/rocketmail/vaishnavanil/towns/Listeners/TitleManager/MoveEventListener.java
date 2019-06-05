package com.rocketmail.vaishnavanil.towns.Listeners.TitleManager;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Utilities.ParticleManager;
import com.rocketmail.vaishnavanil.towns.Utilities.TitleSender;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
                ParticleManager.INSTANCE.drawChunkBorder(player, fromChunk, Color.WHITE);
            } else if (toChunkClaimed && !fromChunkClaimed) {
                //Player Entered Claimed Area from Wilderness
                String plot_owner = toClaim.getOwner().getName();
                String plot_name = toClaim.getName();
                String plot_town = toClaim.getTown().getName();
                Boolean isToPlotFS =  toClaim.isFS();
                if(isToPlotFS){
                    Double cost = toClaim.getPlotCost();
                    if(plot_name.equals("")){
                        TitleSender.INSTANCE.sendTitle(player, "&aFor Sale! &2$" + cost, "&cTown: &6" + plot_town);
                        ParticleManager.INSTANCE.drawChunkBorder(player, toChunk, Color.YELLOW);
                    }else{
                        TitleSender.INSTANCE.sendTitle(player, "&aFor Sale! &2$" + cost, "&cTown: &6" + plot_town + " &8&l| &eName: &6" + plot_name);
                        ParticleManager.INSTANCE.drawChunkBorder(player, toChunk, Color.YELLOW);
                    }
                }else{
                    TitleSender.INSTANCE.sendTitle(player, "&a" + plot_name, "&cTown: &6" + plot_town + " &8&l| &eOwner: &6" + plot_owner);
                    ParticleManager.INSTANCE.drawChunkBorder(player, toChunk, Color.WHITE);
                }
            } else if (toChunkClaimed && fromChunkClaimed) {
                String plot_owner = toClaim.getOwner().getName();
                String plot_name = toClaim.getName();
                String plot_town = toClaim.getTown().getName();
                boolean isToPlotFS =  toClaim.isFS();
                if(isToPlotFS){
                    Double cost = toClaim.getPlotCost();
                    if(plot_name.equals("")){
                        TitleSender.INSTANCE.sendTitle(player, "&aFor Sale! &2$" + cost, "&cTown: &6" + plot_town);
                        ParticleManager.INSTANCE.drawChunkBorder(player, toChunk, Color.YELLOW);
                    }else{
                        TitleSender.INSTANCE.sendTitle(player, "&aFor Sale! &2$" + cost, "&cTown: &6" + plot_town + " &8&l| &eName: &6" + plot_name);
                        ParticleManager.INSTANCE.drawChunkBorder(player, toChunk, Color.YELLOW);
                    }
                }else{
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
                    ParticleManager.INSTANCE.drawChunkBorder(player, toChunk, Color.WHITE);
                }
            }

        }
    }

}
