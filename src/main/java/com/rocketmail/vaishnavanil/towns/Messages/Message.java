package com.rocketmail.vaishnavanil.towns.Messages;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface Message {
    void a(Player p, List<String> list);

    void a(Player p, String s);

    default void a(UUID id, String s) {
        a(Bukkit.getPlayer(id), s);
    }

    void b(Town town, String s);
    void b(Town town,String s,Boolean DsM);
    default String c(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    default void broadcast(String msg){
        for(Player OP:Bukkit.getOnlinePlayers()){
            a(OP,msg);
        }
    }
}
