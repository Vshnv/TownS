package com.rocketmail.vaishnavanil.towns.Messages;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public enum Format {
    CmdErrFrmt( new CmdError()),
    CmdInfoFrmt( new CmdInfo()),
    AlrtFrmt( new Alert());




    private Message frmt;
    Format(Message type){
        this.frmt = type;
    }

    public  Message use(){
        return frmt;
    }

}
