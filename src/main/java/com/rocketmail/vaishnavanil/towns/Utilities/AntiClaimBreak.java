package com.rocketmail.vaishnavanil.towns.Utilities;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum AntiClaimBreak {
use;

    /**
     *
     * @param c <- claim to be unclaimed
     * @return true : if unclaiming Claim c will create a break and vice versa
     */
        public boolean willClaimBreak(Claim c){
            Town town = c.getTown();
            int adj = countAdjacentClaims(c);
            if(adj == 0)return false;
            return !isConnected(c);

           /* switch(adj){
                case 1:
                    return false;
                case 2:
                    if(checkParallel(getAdjClaims(c))){
                        return true;
                    }
                    if(isConnected(c)){
                        return false;
                    }
                    return true;
                case 3:
                    if(isConnected(c))return false;
                    return true;
                case 4:
                    if(isConnected(c))return false;
                    return true;
                    default: return false;
            }*/

        }
        private String getCombDir(Claim c){
            StringBuilder s = new StringBuilder();
            List<Chunk> l = getAdjClaims(c);
            if(l.contains(getNorth(c.getChunk())))s.append('N');
            if(l.contains(getEast(c.getChunk())))s.append('E');
            if(l.contains(getWest(c.getChunk())))s.append('W');
            if(l.contains(getSouth(c.getChunk())))s.append('S');
            return s.toString();
        }
        private boolean isConnected(Claim c){
            String Combo = getCombDir(c);
            boolean toR = true;
            int Connect_count = 0;
            if(containsCharIndividually(Combo,"NW")){
                Chunk NW = getDiffChunkFrom(c.getChunk(),-1,1);
                if(TownS.g().isClaimed(NW)){
                    if(TownS.g().getClaim(NW).getTown().getTownUUID() == c.getTown().getTownUUID()){
                        Connect_count++;
                    }
                }
            }
            if(containsCharIndividually(Combo,"NE")){
                Chunk NE = getDiffChunkFrom(c.getChunk(),1,1);
                if(TownS.g().isClaimed(NE)){
                    if(TownS.g().getClaim(NE).getTown().getTownUUID() == c.getTown().getTownUUID()){
                        Connect_count++;
                    }
                }
            }
            if(containsCharIndividually(Combo,"SW")){
                Chunk SW = getDiffChunkFrom(c.getChunk(),-1,-1);
                if(TownS.g().isClaimed(SW)){
                    if(TownS.g().getClaim(SW).getTown().getTownUUID() == c.getTown().getTownUUID()){
                        Connect_count++;
                    }
                }
            }
            if(containsCharIndividually(Combo,"SE")){
                Chunk SE = getDiffChunkFrom(c.getChunk(),1,-1);
                if(TownS.g().isClaimed(SE)){
                    if(TownS.g().getClaim(SE).getTown().getTownUUID() == c.getTown().getTownUUID()){
                        Connect_count++;
                    }
                }
            }
            if(Connect_count == countAdjacentClaims(c)-1){
                return true;
            }
            return false;
        }
        private boolean containsCharIndividually(String Text,String chars){
            List<Character> Textchars = new ArrayList<>();
            for(char c:Text.toCharArray()){
                Textchars.add(Character.valueOf(c));
            }
            boolean ret = true;
            for(char c:chars.toCharArray()){
                if(Textchars.contains(c)){
                    continue;
                }
                return false;
            }
            return true;
        }

        private List<Chunk> getAdjClaims(Claim c){
            List<Chunk> toReturn = new ArrayList<>();
           /*East*/ if(isDiffChunkClaimed(c.getChunk(),1,0))toReturn.add(getDiffChunkFrom(c.getChunk(),1,0));
            /*West*/if(isDiffChunkClaimed(c.getChunk(),-1,0))toReturn.add(getDiffChunkFrom(c.getChunk(),-1,0));
            /*South*/if(isDiffChunkClaimed(c.getChunk(),0,-1))toReturn.add(getDiffChunkFrom(c.getChunk(),0,-1));
            /*North*/if(isDiffChunkClaimed(c.getChunk(),0,1))toReturn.add(getDiffChunkFrom(c.getChunk(),0,1));
            return toReturn;
        }
        private boolean checkParallel(List<Chunk> values){
            int PrevX = 0;
            int PrevZ = 0;
            boolean fi = true;
            for(Chunk chunk:values){
                if(fi){
                    PrevX = chunk.getX();
                    PrevZ = chunk.getZ();
                    fi = false;
                    continue;
                }
                if(PrevX == chunk.getX() ^ PrevZ == chunk.getZ()){
                    PrevX = chunk.getX();
                    PrevZ = chunk.getZ();
                    continue;
                }else{
                    return false;
                }

            }
            return true;
        }


        private Chunk getNorth(Chunk c){
            return getDiffChunkFrom(c,0,1);
        }
        private Chunk getSouth(Chunk c){
            return getDiffChunkFrom(c,0,-1);
        }
        private Chunk getEast(Chunk c){
            return getDiffChunkFrom(c,1,0);
        }
        private Chunk getWest(Chunk c){
            return getDiffChunkFrom(c,-1,0);
        }

        private int countAdjacentClaims(Claim c){
            int count = 0;
            if(isDiffChunkClaimed(c.getChunk(),1,0))count++;
            if(isDiffChunkClaimed(c.getChunk(),-1,0))count++;
            if(isDiffChunkClaimed(c.getChunk(),0,-1))count++;
            if(isDiffChunkClaimed(c.getChunk(),0,1))count++;
            return count;
        }
        private Chunk getDiffChunkFrom(Chunk c,int xIncrement, int zIncrement){
            return c.getWorld().getChunkAt(c.getX()+xIncrement,c.getZ()+zIncrement);
        }
        private boolean isDiffChunkClaimed(Chunk c,int xIncrement, int zIncrement){
            return TownS.g().isClaimed(c.getX()+xIncrement,c.getZ()+zIncrement,c.getWorld().getName());
        }


}
