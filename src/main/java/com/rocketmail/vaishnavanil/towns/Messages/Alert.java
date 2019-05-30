package com.rocketmail.vaishnavanil.towns.Messages;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.entity.Player;

import java.util.List;

public class Alert implements Message {
    @Override
    public void a(Player p, List<String> list) {
        p.sendMessage(" ");
        p.sendMessage(c("&l&3------>> &n" + TownS.PREFIX + "&r&l&3 <<------"));
        for (String s : list) {
            p.sendMessage(c("&l&3-> &r&b" + s));
        }
        p.sendMessage(c("&l&3------>> &n" + TownS.PREFIX + "&r&l&3 <<------"));
        p.sendMessage(" ");
    }

    @Override
    public void a(Player p, String s) {
        p.sendMessage(" ");
        p.sendMessage(c("&l&3------>> &n" + TownS.PREFIX + "&r&l&3 <<------"));
        p.sendMessage(c("&l&3-> &r&b" + s));
        p.sendMessage(c("&l&3------>> &n" + TownS.PREFIX + "&r&l&3 <<------"));
        p.sendMessage(" ");
    }

    @Override
    public void b(Town town, String s) {

    }
}
