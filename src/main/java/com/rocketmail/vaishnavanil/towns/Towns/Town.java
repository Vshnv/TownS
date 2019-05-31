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
    private HashMap<Rank,List<UUID>> rankMap = new HashMap<>();
    private List<UUID> Members = new ArrayList<>();
    private List<Town> Allies = new ArrayList<>();
    private List<Town> Enemies = new ArrayList<>();
    public String getName(){
        return town_name;
    }
    public Player getMayor(){
        return Bukkit.getPlayer(Mayor_ID);
    }

    public void setRank(Player p,Rank rank){
        List<UUID> idList = null;
        try {
            idList = rankMap.get(rank) == null ? new ArrayList<>() : rankMap.get(rank);
        }catch(Exception e){
            idList = new ArrayList<>();

        }
        idList.add(p.getUniqueId());
        for(Rank r:rankMap.keySet()){
            if(rankMap.get(r).contains(p.getUniqueId())){
                List<UUID> rEmList = null;
                try {
                    rEmList = rankMap.get(r) == null ? new ArrayList<>() : rankMap.get(r);
                }catch(Exception e){
                    rEmList = new ArrayList<>();

                }
                rEmList.remove(p.getUniqueId());
                rankMap.put(r,rEmList);
            }
        }
        rankMap.put(rank, idList);
    }

    public void claim(Chunk chunk,Player owner){
        new Claim(chunk,this,owner);
    }

    public void unclaim(Chunk chunk){
        TownS.g().rCfT(TownS.g().getClaim(chunk));
    }
    public void Ally(Town t){
        unEnemy(t);
        Allies.add(t);
    }
    public void Enemy(Town t){
        unAlly(t);
        Enemies.add(t);
    }
    public void unAlly(Town t){
        Allies.remove(t);
    }
    public void unEnemy(Town t){
        Enemies.remove(t);
    }
    public void deleteTown(){
        TownS.g().rPfT(Mayor_ID);

        for(UUID a:rankMap.get(new Rank("",new ArrayList<>()))){
            TownS.g().rPfT(a);
        }
        for(UUID m:Members){
            TownS.g().rPfT(m);
        }
        TownS.g().rTfM(this);
    }

    public boolean belongs(Player player){
        if(Members.contains(player.getUniqueId()) /*|| Assistants.contains(player.getUniqueId()) */|| Mayor_ID == player.getUniqueId())return true;
        return false;
    }
    public Town(String name,Player mayor){
        town_name = name;
        Mayor_ID = mayor.getUniqueId();
        TownS.g().aTtM(this);
    }
    public Town(String name,Player mayor,List<UUID> MemberL,List<UUID> AssistantL){
        town_name = name;
        Mayor_ID = mayor.getUniqueId();
        Members = MemberL;
       //Assistants = AssistantL;
        TownS.g().aTtM(this);
    }
    public Town(String name,Player mayor,List<UUID> MemberL,List<UUID> AssistantL,List<Claim> claims){
        town_name = name;
        Mayor_ID = mayor.getUniqueId();
        Members = MemberL;
       // Assistants = AssistantL;
        for(Claim c:claims){
            TownS.g().aCtT(c);
        }
        TownS.g().aTtM(this);
    }

}
