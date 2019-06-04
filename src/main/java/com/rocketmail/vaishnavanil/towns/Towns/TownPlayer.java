package com.rocketmail.vaishnavanil.towns.Towns;

import org.bukkit.entity.Player;

public class TownPlayer {
    private boolean showBorder;
    private Player player;

    public TownPlayer(Player player){
        this.player =  player;
        /* Defaults */
        this.showBorder = false;
    }

    public Player getPlayer(){ return player; }
    public boolean showBorder(){ return showBorder; }
    public void toggleBorder() { showBorder = !showBorder; }

}
