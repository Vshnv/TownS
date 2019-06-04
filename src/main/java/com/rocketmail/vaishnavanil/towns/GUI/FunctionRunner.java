package com.rocketmail.vaishnavanil.towns.GUI;

import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class FunctionRunner implements Listener {
    static FunctionRunner runner;
    public static FunctionRunner get(){
        if(runner == null)runner = new FunctionRunner();
        return runner;
    }
    private FunctionRunner(){}
    HashMap<String,HashMap<ItemStack,Function>> registered = new HashMap<>();
    public void register(ItemStack i,Function f,String name){
        HashMap<ItemStack,Function> t = new HashMap<>();
        if(registered.containsKey(name)){
            t = registered.get(name);
        }
         t.put(i,f);
        registered.put(name,t);
    }

    @EventHandler
    public void onCLick(InventoryClickEvent e){
        if(e.getView().getTopInventory().getType() == InventoryType.CREATIVE)return;
        if(e instanceof InventoryCreativeEvent)return;
        if(e.getCurrentItem() == null)return;
         String inv_name =e.getView().getTitle();
         e.setCancelled(true);
         if(!registered.keySet().contains(inv_name))return;
        if(registered.get(inv_name).keySet().contains(e.getCurrentItem())){
            Function func = registered.get(inv_name).get(e.getCurrentItem());
            HashMap<String,String> inp = new HashMap<>();
            inp.put("Player",e.getWhoClicked().getUniqueId().toString());
            func.run(inp);
        }
    }
}
