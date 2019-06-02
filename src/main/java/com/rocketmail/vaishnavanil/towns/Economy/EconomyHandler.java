package com.rocketmail.vaishnavanil.towns.Economy;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public enum EconomyHandler {
    INSTANCE;

    /* Method to add and remove player balances */
    public Boolean changePlayerBalance(Player player, Integer amount) {
        if (amount > 0) {
            return TownS.getEconomy().depositPlayer(player, amount).transactionSuccess();
        } else {
            return TownS.getEconomy().withdrawPlayer(player, Math.abs(amount)).transactionSuccess();
        }
    }
    /* Method to add and remove town bank balances */
    public Boolean changeTownBalance(Town town, Integer amount) {
        if (amount > 0) {
            return TownS.getEconomy().bankDeposit(town.getTownUUID().toString(), amount).transactionSuccess();
        } else {
            return TownS.getEconomy().bankWithdraw(town.getTownUUID().toString(), Math.abs(amount)).transactionSuccess();
        }
    }

    /* Method to create a town's bank. Used on initial town creation. */
    public Boolean createTownBank(Town town) {

        return TownS.getEconomy().createBank(town.getTownUUID().toString(), (OfflinePlayer) town.getMayor()).transactionSuccess();
    }

    /* Method to remove town's bank */
    public Boolean deleteTownBank(Town town){
        return TownS.getEconomy().deleteBank(town.getTownUUID().toString()).transactionSuccess();
    }

    /* Method to get player's bank balance */
    public Double getPlayerBalance(Player player) {
        if (player.hasPlayedBefore()) {
            return TownS.getEconomy().getBalance(player);
        } else {
            return 0.0;
        }
    }

    /* Method to get town bank balance */
    public Double getTownBalance(Town town) {
        return TownS.getEconomy().bankBalance(town.getTownUUID().toString()).balance;
    }

}
