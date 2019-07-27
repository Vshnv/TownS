package com.rocketmail.vaishnavanil.towns.Messages;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum TownsMainMessage {
    get;

    public void sendBaseCommands(Player p) {
        String var = "&2=#===================#=[TownS]=#====================#=\n" +
                "&2=#          &l&a/towns-Shows basic towns commands.&r&2      #=\n" +
                "&2=#          &l&a/plot-Shows basic plot commands&r&2        #=\n" +
                "&2=#              &l&aDeveloped by TheWild Team.&r&2          #=\n" +
                "=#===================#========#=====================#=";
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', var));
    }

    public void sendSubCommandTOWNS(Player p) {
        String var = "&2=#===================#=[TownS]=#====================#=\n" +
                "&2=# &l&a/t invite <player>   | /t uninvite <player>&r&2      #=\n" +
                "&2=# &l&a/t join <town>       | /t leave&r&2                  #=\n" +
                "&2=# &l&a/t setspawn          | /t spawn&r&2                  #=\n" +
                "&2=# &l&a/t setmayor <player> | /t setrank <player> <rank>&r&2#=\n" +
                "&2=# &l&a/t deposit <amount>  | /t withdraw <amount>&r&2      #=\n" +
                "&2=# &l&a/t setrent <amount>  | /t create&r&2                 #=\n" +
                "&2=# &l&a/t setname <newName> | /t delete&r&2                 #=\n" +
                "&2=# &l&a/t warp              | /t setwarp <WarpName> &r&2    #=\n" +
                "&2=# &l&a/t delwarp           | /t kick <player>&r&2          #=\n" +
                "&2=# &l&a/t claim             | /t unclaim  &r&2              #=\n" +
                "&2=# &l&a/t setopen true/false| /t visit <town>    &r&2       #=\n" +
                "&2=#===================#========#=====================#=";
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', var));

    }

    public void sendSubCommandPLOTS(Player p) {
        String var = "=#===================#=[TownS]=#====================#=\n" +
                "=# /plot claim <player>   | /plot unclaim <player>  #=\n" +
                "=# /plot setname <Name>   | /plot border            #=\n" +
                "=# /plot forsale <Cost>   | /plot notforsale        #=\n" +
                "=# /plot fs <Cost>        | /plot nfs               #=\n" +
                "=# /plot access <amount>  | /plot flags             #=\n" +
                "=# /plot allow/disallow build/container rank/player #=\n" +
                "=#===================#========#=====================#=";
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', var));

    }

    public void townCreate(Player p) {
        String var = "=#===================#=[TownS]=#====================#=\n" +
                "=#             You just started a town              #=\n" +
                "=#          /town and /plots for more info          #=\n" +
                "=#===================#========#=====================#=";
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', var));

    }
}
