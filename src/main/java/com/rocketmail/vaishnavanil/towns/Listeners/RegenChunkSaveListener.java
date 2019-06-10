package com.rocketmail.vaishnavanil.towns.Listeners;

import com.rocketmail.vaishnavanil.towns.Messages.Format;
import com.rocketmail.vaishnavanil.towns.Utilities.RegenSaveQueueManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class RegenChunkSaveListener implements Listener {
    private static RegenChunkSaveListener instance;
    RegenSaveQueueManager Queue = RegenSaveQueueManager.get;
    public static RegenChunkSaveListener get(){
        if(instance == null)instance = new RegenChunkSaveListener();
        return instance;
    }
    private RegenChunkSaveListener(){}




    @EventHandler
    public void onplc(BlockPlaceEvent e){
        if(Queue.isCached(e.getBlock().getLocation().getChunk()))return;
        if(Queue.isQueued(e.getBlock().getLocation().getChunk())){
            e.setCancelled(true);
            Format.AlrtFrmt.use().a(e.getPlayer(),"Please Wait... Preparing chunk for your use!"
            );
        }
        if(!Queue.addToQueue(e.getBlock().getLocation().getChunk())){
            e.setCancelled(true);
            Format.AlrtFrmt.use().a(e.getPlayer(),"Please Wait... Preparing chunk for your use!");

        }
    }

    @EventHandler
    public void onBrk(BlockBreakEvent e){
        if(Queue.isCached(e.getBlock().getLocation().getChunk()))return;
        if(Queue.isQueued(e.getBlock().getLocation().getChunk())){
            e.setCancelled(true);
            Format.AlrtFrmt.use().a(e.getPlayer(),"Please Wait... Preparing chunk for your use!"
            );
        }
        if(!Queue.addToQueue(e.getBlock().getLocation().getChunk())){
            e.setCancelled(true);
            Format.AlrtFrmt.use().a(e.getPlayer(),"Please Wait... Preparing chunk for your use!");

        }
    }
}
