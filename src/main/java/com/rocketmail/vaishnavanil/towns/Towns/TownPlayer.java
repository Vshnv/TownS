package com.rocketmail.vaishnavanil.towns.Towns;

import com.rocketmail.vaishnavanil.towns.Messages.Format;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TownPlayer {
    private List<String> Invites = new ArrayList<>();
    private boolean showBorder;
    private boolean townChatActive;
    private Player player;

    public TownPlayer(Player player){
        this.player =  player;
        /* Defaults */
        this.showBorder = false;
        this.townChatActive = false;
    }
    public void silentUnvite(Town s){
        if(Invites.contains(s.getName()))Invites.remove(s.getName());
    }
    public boolean invite(Town s){
        if(Invites.contains(s.getName()))return false;
        Format.CmdInfoFrmt.use().a(player,"You were invited to the Town -> " + s.getName());
        Invites.add(s.getName());
        return true;
    }
    public boolean uninvite(Town s){
        if(!Invites.contains(s.getName()))return false;
        Format.CmdInfoFrmt.use().a(player,"You were uninvited from the Town -> " + s.getName());

        Invites.remove(s.getName());
        return true;
    }

    public boolean isInvited(Town s){
        return Invites.contains(s.getName());
    }

    public Player getPlayer(){ return player; }
    public boolean showBorder(){ return showBorder; }
    public void toggleBorder() { showBorder = !showBorder; }
    public boolean townnChatActive(){ return townChatActive; }
    public void toggleTownChat() { townChatActive = !townChatActive; }

}
