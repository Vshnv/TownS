package com.rocketmail.vaishnavanil.towns.Listeners.FlagManagers;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Flag;
import com.rocketmail.vaishnavanil.towns.Utilities.ActionBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PVPEventListener implements Listener {
    @EventHandler
    public void onPVP(EntityDamageByEntityEvent e){
        Entity A = e.getDamager();
        Entity B = e.getEntity();
        // A atks B
        if(!(A instanceof Player && B instanceof Player))return;

        //PVP
        Player Attacker = (Player)A;
        Player Defender = (Player)B;
        if(!TownS.g().isClaimed(Defender.getLocation().getChunk()))return;
        Claim claim = TownS.g().getClaim(Defender.getLocation().getChunk());

        if(!claim.hasFlag(Flag.PVP)){
            e.setCancelled(true);
            Attacker.setVelocity(B.getLocation().toVector().subtract(A.getLocation().toVector()).normalize());
            ActionBar.use.send(Attacker,"&4PVP disabled in this claim!");
        }

    }
}
