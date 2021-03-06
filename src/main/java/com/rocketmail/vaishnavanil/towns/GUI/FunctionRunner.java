package com.rocketmail.vaishnavanil.towns.GUI;

import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FunctionRunner implements Listener {
    static FunctionRunner runner;
    public static FunctionRunner get(){
        if(runner == null)runner = new FunctionRunner();
        return runner;
    }
    private FunctionRunner(){}
    HashMap<String,HashMap<Object,Function>> registered = new HashMap<>();
    List<String> keys = new ArrayList<>();
    public void register(ItemStack i,Function f,String name,Boolean ResetUse){
        HashMap<Object,Function> t = new HashMap<>();
        if(registered.containsKey(name)){
            t = registered.get(name);
        }
         t.put(i,f);
        registered.put(name,t);
        if(ResetUse)if(!keys.contains(name))keys.add(name);
    }

    @EventHandler
    public void onCLick(InventoryClickEvent e){
        if(e.getView().getTopInventory().getType() == InventoryType.CREATIVE)return;
        if(e instanceof InventoryCreativeEvent)return;
        if(e.getCurrentItem() == null)return;
         String inv_name =e.getView().getTitle();
         if(!registered.keySet().contains(inv_name))return;
               e.setCancelled(true);
        if(registered.get(inv_name).keySet().contains(e.getCurrentItem())){
            Function func = registered.get(inv_name).get(e.getCurrentItem());
            HashMap<String,String> inp = new HashMap<>();
            inp.put("Player",e.getWhoClicked().getUniqueId().toString());
            inp.put("Slot", Integer.valueOf(e.getSlot()).toString() );
            func.run(inp);
        }
    }
    @EventHandler
    public void ICE(InventoryCloseEvent e){
        if(registered.keySet().contains(e.getView().getTitle())){
            if(keys.contains(e.getView().getTitle())){
                registered.remove(e.getView().getTitle());
            }
        }
    }
}
