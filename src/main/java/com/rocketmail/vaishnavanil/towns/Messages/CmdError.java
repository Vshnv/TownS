package com.rocketmail.vaishnavanil.towns.Messages;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import com.rocketmail.vaishnavanil.towns.Utilities.ActionBar;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdError implements Message {
    @Override
    public void a(Player p, List<String> list) {
/*        p.sendMessage(" ");
        p.sendMessage(c("&l&4------>> &n" + TownS.PREFIX + "&r&l&4 <<------"));*/
        for (String s : list) {
            ActionBar.use.send(p, s);

/*
            p.sendMessage(c("&l&4-> &r&c" + s));
*/
        }
        /*p.sendMessage(c("&l&4------>> &n" + TownS.PREFIX + "&r&l&4 <<------"));
        p.sendMessage(" ");
        p.sendMessage(" ");*/
    }

    @Override
    public void a(Player p, String s) {
        ActionBar.use.send(p, s);

      /*  p.sendMessage(" ");
        p.sendMessage(c("&l&4------>> &n" + TownS.PREFIX + "&r&l&4 <<------"));
        p.sendMessage(c("&l&4-> &r&c" + s));
        p.sendMessage(c("&l&4------>> &n" + TownS.PREFIX + "&r&l&4 <<------"));
        p.sendMessage(" ");*/
    }

    @Override
    public void b(Town town, String s) {

    }

    @Override
    public void b(Town town, String s, Boolean DsM) {

    }
}
