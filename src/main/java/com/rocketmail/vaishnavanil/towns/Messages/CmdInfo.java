package com.rocketmail.vaishnavanil.towns.Messages;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdInfo implements Message {
    @Override
    public void a(Player p, List<String> list) {
        p.sendMessage(" ");
        p.sendMessage(c("&l&8------>> &n" + TownS.PREFIX + "&r&l&8 <<------"));
        for (String s : list) {
            p.sendMessage(c("&l&8-> &r&7" + s));
        }
        p.sendMessage(c("&l&8------>> &n" + TownS.PREFIX + "&r&l&8 <<------"));
        p.sendMessage(" ");
    }

    @Override
    public void a(Player p, String s) {
        p.sendMessage(" ");
        p.sendMessage(c("&l&8------>> &n" + TownS.PREFIX + "&r&l&8 <<------"));
        p.sendMessage(c("&l&8-> &r&7" + s));
        p.sendMessage(c("&l&8------>> &n" + TownS.PREFIX + "&r&l&8 <<------"));
        p.sendMessage(" ");
    }

    @Override
    public void b(Town town, String s) {

    }
}
