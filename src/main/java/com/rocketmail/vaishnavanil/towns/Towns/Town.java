package com.rocketmail.vaishnavanil.towns.Towns;

import com.rocketmail.vaishnavanil.towns.TownS;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Town {
    private String town_name;
    private UUID Mayor_ID;
    private List<UUID> Assistants = new ArrayList<>();
    private List<UUID> Members = new ArrayList<>();

    public String getName() {
        return town_name;
    }

    public Player getMayor() {
        return Bukkit.getPlayer(Mayor_ID);
    }


    public void claim(Chunk chunk, Player owner) {
        new Claim(chunk, this, owner);
    }

    public void unclaim(Chunk chunk) {
        TownS.g().rCfT(TownS.g().getClaim(chunk));
    }

    public void deleteTown() {
        TownS.g().rPfT(Mayor_ID);

        for (UUID a : Assistants) {
            TownS.g().rPfT(a);
        }
        for (UUID m : Members) {
            TownS.g().rPfT(m);
        }
        TownS.g().rTfM(this);
    }

    public boolean belongs(Player player) {
        if (Members.contains(player.getUniqueId()) || Assistants.contains(player.getUniqueId()) || Mayor_ID == player.getUniqueId())
            return true;
        return false;
    }

    public Town(String name, Player mayor) {
        town_name = name;
        Mayor_ID = mayor.getUniqueId();
        TownS.g().aTtM(this);
    }

    public Town(String name, Player mayor, List<UUID> MemberL, List<UUID> AssistantL) {
        town_name = name;
        Mayor_ID = mayor.getUniqueId();
        Members = MemberL;
        Assistants = AssistantL;
        TownS.g().aTtM(this);
    }

    public Town(String name, Player mayor, List<UUID> MemberL, List<UUID> AssistantL, List<Claim> claims) {
        town_name = name;
        Mayor_ID = mayor.getUniqueId();
        Members = MemberL;
        Assistants = AssistantL;
        for (Claim c : claims) {
            TownS.g().aCtT(c);
        }
        TownS.g().aTtM(this);
    }

}
