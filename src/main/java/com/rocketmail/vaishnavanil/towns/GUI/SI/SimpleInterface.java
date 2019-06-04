package com.rocketmail.vaishnavanil.towns.GUI.SI;


import com.rocketmail.vaishnavanil.towns.GUI.FunctionRunner;
import com.rocketmail.vaishnavanil.towns.GUI.StackFunc;
import org.bukkit.Bukkit;

import org.bukkit.inventory.Inventory;


public abstract class SimpleInterface {
    StackFunc[] inv;
    int size;
    String name;
    public SimpleInterface(String n){
name = n;

    }
    public void init(){
        System.out.println(inv.length+(9-inv.length%9));
        System.out.println((int) (Math.ceil(inv.length/9)*9));

        size = inv.length+(9-inv.length%9);
        FunctionRunner runner = FunctionRunner.get();
        for(StackFunc func:inv){
            runner.register(func.getStack(),func.getFunction(),name);
        }
    }
    public Inventory get(){
        Inventory gui = Bukkit.createInventory(null,size,name);
        for(int i = 0;i<=inv.length-1;i++){
            gui.setItem(i,inv[i].getStack());
        }
        return gui;
    }

}
