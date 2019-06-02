package com.rocketmail.vaishnavanil.towns.Economy;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public enum EconomyHandler {
    INSTANCE;

    public EconomyResponse changePlayerBalance(Player player, Integer amount) {
        if (amount > 0) {
            return TownS.getEconomy().depositPlayer(player, amount);
        } else {
            return TownS.getEconomy().withdrawPlayer(player, amount);
        }
    }

    public EconomyResponse createTownBank(Player town_owner, Town town) {
        return TownS.getEconomy().createBank(town.getTownUUID().toString(), town_owner);
    }

    public EconomyResponse deleteTownBank(Town town){
        return TownS.getEconomy().deleteBank(town.getTownUUID().toString());
    }

    public EconomyResponse changeTownBalance(Town town, Integer amount) {
        if (amount > 0) {
            return TownS.getEconomy().bankDeposit(town.getTownUUID().toString(), amount);
        } else {
            return TownS.getEconomy().bankDeposit(town.getTownUUID().toString(), amount);
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
