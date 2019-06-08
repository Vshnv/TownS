package com.rocketmail.vaishnavanil.towns.GUI.SI;

import com.rocketmail.vaishnavanil.towns.GUI.Function;
import com.rocketmail.vaishnavanil.towns.GUI.StackFunc;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemCreation.ItemBuilder;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemLore.LoreStyle;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemName.NameStyle;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Flag;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginAwareness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FlagGUI extends SimpleInterface {
    public FlagGUI(String n,Claim c) {
        super("Flag GUI");
        ItemStack DetailsItem = new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setNameStyle(NameStyle.DESIGNED).setLoreStyle(LoreStyle.INFO).setDisplayName("Showing Flags").setLore(getDetails(c.getChunk())).pack();
        super.inv[0] = new StackFunc(DetailsItem, new Function() {
            @Override
            public void run(HashMap<String, String> INPUT) {
                //NULL FUNCTION
            }
        });
        int cur_slot =1;
            for(Flag f: Flag.values()){
                ItemStack flagStack = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).pack();
            }

        super.init();
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
        lore.add("&e&lClick To View Flags");
        return lore;
    }
}
