package com.rocketmail.vaishnavanil.towns.Economy;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


public enum EconomyHandler {
    INSTANCE;

    /* Method to add and remove player balances */
    public Boolean changePlayerBalance(OfflinePlayer player, Double amount) {
        if (amount >= 0) {
            return TownS.getEconomy().depositPlayer(player, amount).transactionSuccess();
        } else {
            return TownS.getEconomy().withdrawPlayer(player, Math.abs(amount)).transactionSuccess();
        }
    }

    /* Method to get player's bank balance */
    public Double getPlayerBalance(OfflinePlayer player) {
        if (player.hasPlayedBefore()) {
            return TownS.getEconomy().getBalance(player);
        } else {
            return 0.0;
        }
    }

    /* Method to let player deposit money into town bank */
    public Boolean depositIntoTown(OfflinePlayer player, Town  town, Double amount){
        if( EconomyHandler.INSTANCE.getPlayerBalance(player) >= amount ){
            if(EconomyHandler.INSTANCE.changePlayerBalance(player, -amount)){
                town.changeTownBalanceBy(amount);
                return true;
            }else { return false; }
        }else{ return false; }
    }

    /*  Method to let player deposit percentage of their own money into town bank */
    public Boolean depositIntoTownByPercent(Player player, Town  town, Double amount, Double percent){
        if( EconomyHandler.INSTANCE.getPlayerBalance(player) >= amount*percent ){
            if(EconomyHandler.INSTANCE.changePlayerBalance(player, -amount*percent)){
                town.changeTownBalanceBy(amount*percent);
                return true;
            }else { return false; }
        }else{ return false; }
    }

    /* Method to let player withdraw money from town bank */
    public Boolean withdrawFromTown(Player player, Town town, Double amount){
        if(  town.getTownBalance() >= amount ){
            if(EconomyHandler.INSTANCE.changePlayerBalance(player, amount)){
                town.changeTownBalanceBy(-amount);
                return true;
            }else{ return false; }
        }else { return false; }
    }


}
