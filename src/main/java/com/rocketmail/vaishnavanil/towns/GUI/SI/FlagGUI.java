package com.rocketmail.vaishnavanil.towns.GUI.SI;

import com.rocketmail.vaishnavanil.towns.GUI.Function;
import com.rocketmail.vaishnavanil.towns.GUI.FunctionRunner;
import com.rocketmail.vaishnavanil.towns.GUI.StackFunc;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemCreation.ItemBuilder;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemLore.LoreStyle;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemName.NameStyle;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Flag;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginAwareness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FlagGUI extends SimpleInterface {
    public FlagGUI(String n,Claim c) {

        super(n);
        removeESett = true;
        int Pre_Needed_slot_count = Flag.values().length+2;
        int finalSlotCount = 0;
        if(Pre_Needed_slot_count>9){
            if(Pre_Needed_slot_count%9 == 0){
                finalSlotCount = Pre_Needed_slot_count;
            }else {
                finalSlotCount = Pre_Needed_slot_count + (9 - Pre_Needed_slot_count % 9);
            }
        }else{
            finalSlotCount =9;
        }

        inv = new StackFunc[Pre_Needed_slot_count];
        ItemStack DetailsItem = new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setGlow(true).setNameStyle(NameStyle.DESIGNED).setLoreStyle(LoreStyle.INFO).setDisplayName("Showing Flags").setLore(getDetails(c.getChunk())).pack();
        super.inv[0] = new StackFunc(DetailsItem, INPUT -> {
            //DO NOTHING
        });
        ItemStack Blank = new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setNameStyle(NameStyle.NO_STYLE).setDisplayName("").pack();
        StackFunc NullFunc = new StackFunc(Blank, INPUT -> {
            //DO NOTHING
        });
        ItemStack BackItem = new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setNameStyle(NameStyle.DESIGNED).setLoreStyle(LoreStyle.INFO).setDisplayName("Open Map").addLoreLine("Takes you to the Town Map").pack();
        StackFunc BackFunc = new StackFunc(BackItem, INPUT -> {
            Player p = Bukkit.getPlayer(UUID.fromString(INPUT.get("Player")));
            p.closeInventory();
            p.performCommand("towns map");
        });
        int cur_slot =1;
            for(Flag f: Flag.values()){
                ItemStack flagStack = new ItemBuilder(c.hasFlag(f)).setNameStyle(NameStyle.HIGHLIGHT).setDisplayName(f.getName()).setLoreStyle(LoreStyle.DETECTION).addLoreLine("This flag is currently "+(c.hasFlag(f) ? "Enabled":"Disabled")).pack();

                Function flagFunc = new Function() {
                    Flag thisFlag = f;
                    Claim cl = c;
                    @Override
                    public void run(HashMap<String, String> INPUT) {
                        Player clicker = Bukkit.getPlayer(UUID.fromString(INPUT.get("Player")));
                        if(clicker.getUniqueId() != c.getOwnerID())return;
                        inv[Integer.valueOf(INPUT.get("Slot"))] = inv[Integer.valueOf(INPUT.get("Slot"))].setStackType(toggleFlag(c,thisFlag));
                        FunctionRunner.get().register(inv[Integer.valueOf(INPUT.get("Slot"))].getStack(),inv[Integer.valueOf(INPUT.get("Slot"))].getFunction(),name,removeESett);

                        update(clicker);
                    }
                };
                StackFunc sF = new StackFunc(flagStack,flagFunc);
                super.inv[cur_slot++] = sF;
            }
            int cur_to_be_size;
        if(inv.length>9){
            if(inv.length%9 == 0){
                cur_to_be_size = inv.length;
            }else {
                cur_to_be_size = inv.length + (9 - inv.length % 9);
            }
        }else{
            cur_to_be_size =9;
        }
        if(cur_to_be_size > cur_slot){
            for(int start = cur_slot;start<=cur_to_be_size-1;start++){
                if(start == cur_to_be_size-1){
                    inv[start] = BackFunc;
                }else{
                    inv[start] = NullFunc;
                }
            }
        }


        super.init();
    }
    public Material toggleFlag(Claim c,Flag f){
        if(c.hasFlag(f)){c.removeFlag(f); return Material.RED_STAINED_GLASS_PANE;}
        else{ c.addFlag(f);return Material.LIME_STAINED_GLASS_PANE;}
    }
    private List<String> getDetails(Chunk chunk) {
        List<String> lore = new ArrayList<>();
        if (!TownS.g().isClaimed(chunk)) {
            lore.add("&a&lWilderness");
            return lore;
        }
        Claim claim = TownS.g().getClaim(chunk);
        lore.add("&f&lPlot Claimed");
        lore.add(" ");
        lore.add("Town: &c" + claim.getTown().getName());
        if(claim.getName().equals("")){
            lore.add("Area Name: &aUnnamed" );
        }else{
            lore.add("Area Name: &a" +claim.getName());
        }
        lore.add("Claim Owner: &6" + claim.getOwner().getName()+"  ");
        lore.add(" ");
        lore.add("&e&lDisplaying Flags");
        return lore;
    }
}
