package com.rocketmail.vaishnavanil.towns.Messages;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.util.List;

public class CmdInfo implements Message {
    static String FOOTER = null;

    public String footer() {
        if (FOOTER == null) {
            FOOTER = HEADER.replace("%PREFIX%", "========");
        }
        return FOOTER;
    }

    public String header() {
        return HEADER.replace("%PREFIX%", TownS.PREFIX);
    }
    @Override
    public void a(Player p, List<String> list) {
        p.sendMessage(" ");
        p.sendMessage(header());
        for (String s : list) {
            p.sendMessage(centralize(c(s)));
        }
        p.sendMessage(footer());
        p.sendMessage(" ");
    }

    public String centralize(String s) {
        StringBuilder b = new StringBuilder();
        b.append("-#");
        int HL = (HEADER.length() - 2) / 2;
        int leftLength = s.length() / 2;
        int spaces = HL - leftLength;
        for (int i = 1; i <= spaces; i++) {
            b.append(" ");
        }
        b.append(s);
        for (int i = 1; i <= spaces; i++) {
            b.append(" ");
        }
        b.append("#-");
        return b.toString();
    }

    @Override
    public void a(Player p, String s) {
        p.sendMessage(" ");

        p.sendMessage(header());

        p.sendMessage(centralize(c(s)));

        p.sendMessage(footer());
        p.sendMessage(" ");
    }

    @Override
    public void b(Town town, String s) {

    }

    @Override
    public void b(Town town, String s, Boolean DsM) {

    }
}
