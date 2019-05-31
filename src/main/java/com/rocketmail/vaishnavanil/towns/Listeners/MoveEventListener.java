package com.rocketmail.vaishnavanil.towns.Listeners;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Utilities.TitleSender;
import org.bukkit.Chunk;
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
            } else if (toChunkClaimed && !fromChunkClaimed) {
                //Player Entered Claimed Area from Wilderness
                toClaim.setName("l33t Plot Name");
                // ^ Setting Name is just for testing.
                String plot_owner = toClaim.getOwner().getName();
                String plot_name = toClaim.getName();
                String plot_town = toClaim.getTown().getName();
                TitleSender.INSTANCE.sendTitle(player, "&a" + plot_name, "&cTown: &6" + plot_town + " &8&l| &eOwner: &6" + plot_owner);
            } else if (toChunkClaimed && fromChunkClaimed) {
                String plot_owner = toClaim.getOwner().getName();
                String plot_name = toClaim.getName();
                String plot_town = toClaim.getTown().getName();
                if (plot_name.equals("")) {
                    TitleSender.INSTANCE.sendTitle(player, "", "&2Owner: &a" + plot_owner);
                } else {
                    TitleSender.INSTANCE.sendTitle(player, "", "&2Area: &a" + plot_name + " &8&l| &eOwner: &6" + plot_owner);
                }
            }

        }
    }
}
