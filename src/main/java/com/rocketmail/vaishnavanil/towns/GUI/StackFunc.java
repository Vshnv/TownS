package com.rocketmail.vaishnavanil.towns.GUI;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class StackFunc {
    private ItemStack stack;
    private Function function;
    public StackFunc(ItemStack i, Function f){
        stack = i;
        function = f;
    }
    public ItemStack getStack(){
        return stack;
    }
    public StackFunc setStackType(Material m){
        stack.setType(m);
        return this;
    }
    public Function getFunction() {
        return function;
    }
}
