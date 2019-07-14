package com.rocketmail.vaishnavanil.towns.Economy;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.RegenBuilder;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import com.rocketmail.vaishnavanil.towns.Utilities.LoadManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public enum EcoUpkeep {
    EcoUK;
    public static final double UPKEEP_PER_CLAIM = 100D;
    public void doUpkeep(){
        rentClaims();
        upKeepTowns();
    }
    public void warnUpkeep(){
        for(Town t:TownS.g().getAllTowns()){
            if (t.getTownBalance() + t.getPayingClaims().size() * t.getRent() < UPKEEP_PER_CLAIM * t.getClaims().size()) {
                if(t.getMayor().isOnline()){
                    t.getMayor().getPlayer().sendActionBar('&',"&cYour town will fall into ruins soon if upkeep is not met");
                }
                for(OfflinePlayer p:t.getMembers()){
                    if(p.isOnline()){
                        p.getPlayer().sendActionBar('&',"&cBEWARE Your town will fall into ruins soon if upkeep is not met");
                    }
                }

            }
        }
    }
    private void rentClaims(){
        if(TownS.g().getAllClaims().isEmpty())return;
        for(Claim c:TownS.g().getAllClaims()){
            if(c == null)continue;
            if(c.getTown() == null){

                    Chunk chunk = c.getChunk();
                    TownS.g().removeFromClaimMap(chunk.getX(),chunk.getZ(),chunk.getWorld().getName());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            new RegenBuilder((Material[][][]) LoadManager.get.loadObject("ChunkSaves", chunk.getX() + "TT" + chunk.getZ() + "TT" + chunk.getWorld().getName() + ".dat"), chunk);

                        }
                    }.runTask(TownS.g());

                    continue;

            }
            if(c.getOwnerID() == c.getTown().getTownUUID())continue;
            if(EconomyHandler.INSTANCE.depositIntoTown(c.getOwner(),c.getTown(),c.getTown().getRent())){
                if(c.getOwner().isOnline()){
                    c.getOwner().getPlayer().sendActionBar('&',"&3You paid today's rent for a plot");
                }
            }else{
                if(c.getOwner().isOnline()){
                    c.getOwner().getPlayer().sendActionBar('&',"&cYou failed to pay rent for one of your plots");
                }
                c.setOwner(c.getTown().getMayor().getUniqueId());
            }
        }
        return;
    }
    private void upKeepTowns(){
        if(TownS.g().getAllTowns().isEmpty())return;
        for(Town t:TownS.g().getAllTowns()){
            if (t.getTownBalance() < UPKEEP_PER_CLAIM * t.getClaims().size()) {
                if(t.getMayor().isOnline()){
                    t.getMayor().getPlayer().sendActionBar('&',"&cYour town fell into ruins");
                }
                for(OfflinePlayer p:t.getMembers()){
                    if(p.isOnline()){
                        p.getPlayer().sendActionBar('&',"&cYour town fell into ruins");
                    }
                }
                Bukkit.broadcastMessage(ChatColor.RED+"The town " + t.getName() + " fell into ruins!");
                t.deleteTown();
            } else {
                t.changeTownBalanceBy(-1 * UPKEEP_PER_CLAIM * t.getClaims().size());
            }
        }
    }
}
