package com.rocketmail.vaishnavanil.towns.Utilities;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGaurdUtil implements WGUtil {
    @Override
    public boolean isPlotOverride(Location p) {
        ApplicableRegionSet set = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(p.getWorld())).getApplicableRegions(BukkitAdapter.asBlockVector(p));
        for (ProtectedRegion r : set.getRegions()) {
            if (r.getId().toLowerCase().startsWith("plot")) {
                return true;
            }
        }
        return false;
    }
}
