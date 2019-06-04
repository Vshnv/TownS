package com.rocketmail.vaishnavanil.towns.Towns;

import org.bukkit.entity.Player;

public class TownPlayer {
    boolean showBorder;
    Player player;

    public TownPlayer(Player player){
        this.player =  player;
        /* Defaults */
        this.showBorder = false;
    }

    public Player getPlayer(){ return player; }
    public boolean showBorder(){ return showBorder; }
    public void toggleBorder() { showBorder = !showBorder; }

}
