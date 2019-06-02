package com.rocketmail.vaishnavanil.towns.Economy;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import com.sun.org.apache.xpath.internal.operations.Bool;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public enum EconomyHandler {
    INSTANCE;

    public Boolean changePlayerBalance(Player player, Integer amount) {
        if (amount > 0) {
            return TownS.getEconomy().depositPlayer(player, amount).transactionSuccess();
        } else {
            return TownS.getEconomy().withdrawPlayer(player, amount).transactionSuccess();
        }
    }

    public Boolean createTownBank(Town town) {
        return TownS.getEconomy().createBank(town.getTownUUID().toString(), town.getMayor()).transactionSuccess();
    }

    public Boolean deleteTownBank(Town town){
        return TownS.getEconomy().deleteBank(town.getTownUUID().toString()).transactionSuccess();
    }

    public Boolean changeTownBalance(Town town, Integer amount) {
        if (amount > 0) {
            return TownS.getEconomy().bankDeposit(town.getTownUUID().toString(), amount).transactionSuccess();
        } else {
            return TownS.getEconomy().bankWithdraw(town.getTownUUID().toString(), amount).transactionSuccess();
        }
    }

    public Double getTownBalance(Town town) {
        return TownS.getEconomy().bankBalance(town.getTownUUID().toString()).amount;
    }

    public Double getPlayerBalance(Player player) {
        if (player.hasPlayedBefore()) {
            return TownS.getEconomy().getBalance(player);
        } else {
            return 0.0;
        }
    }

}
