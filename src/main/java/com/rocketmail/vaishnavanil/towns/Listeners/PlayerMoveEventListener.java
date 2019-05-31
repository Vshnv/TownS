package com.rocketmail.vaishnavanil.towns.Listeners;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import com.rocketmail.vaishnavanil.towns.Utilities.TitleSender;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveEventListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if( event.getFrom().getChunk() != event.getTo().getChunk() ){
            Player player = event.getPlayer();
            Chunk fromChunk = event.getFrom().getChunk();
            Chunk toChunk = event.getTo().getChunk();
            Claim fromClaim = TownS.g().getClaim(fromChunk);
            Claim toClaim = TownS.g().getClaim(toChunk);
            if( (fromClaim != null) && (toClaim == null) ){
                //Player Entered Wilderness from Claimed Region
                TitleSender.INSTANCE.sendTitle(player, null, "&2Entering Wilderness");
            }else if( (fromClaim == null) && (toClaim != null) ){
                //Player Entered Claimed Area from Wilderness
                String plot_owner = toClaim.getOwner().getName();
                String plot_name = toClaim.getName();
                TitleSender.INSTANCE.sendTitle(player, "&a"+plot_name, "&eOwner: &6"+plot_owner);
            }

        }
    }
}
