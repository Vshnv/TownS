package com.rocketmail.vaishnavanil.towns.MapGUI.ItemCreation;

import com.rocketmail.vaishnavanil.towns.MapGUI.ItemLore.LoreStyle;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemName.NameStyle;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    ItemStack stack;
    String display = null;
    NameStyle nStyle = NameStyle.NO_STYLE;
    List<String> RAWlore = new ArrayList<>();
    LoreStyle style = LoreStyle.NO_STYLE;
    List<ItemFlag> flags = new ArrayList<>();

    public ItemBuilder(Material Stack){
        this.stack = new ItemStack(Stack);
    }
    public ItemBuilder setDisplayName(String name){
        display = name;
        return this;
    }
    public ItemBuilder setLore(List<String> lore){
        this.RAWlore = lore;
        return this;
    }
    public ItemBuilder addLoreLine(String line){
        RAWlore.add(line);
        return this;
    }
    public ItemBuilder addIFlag(ItemFlag flag){
        this.flags.add(flag);
        return this;
    }
    public ItemBuilder setLoreStyle(LoreStyle s){
        style = s;
        return this;
    }
    public ItemBuilder setNameStyle(NameStyle stle){
        this.nStyle = stle;
        return this;
    }
    public ItemStack pack(){
        ItemMeta meta = stack.getItemMeta();
        if(display !=null)meta.setDisplayName(nStyle.use(display));
        if(!RAWlore.isEmpty()){meta.setLore(style.use(RAWlore));}
        stack.setItemMeta(meta);
        if(!flags.isEmpty())for(ItemFlag f:flags) {
            stack.addItemFlags(f);
        }

        return stack;
    }

}
