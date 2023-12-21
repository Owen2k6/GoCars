//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.owen2k6.gocars;

import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerListen extends PlayerListener {
    public GoCars plugin;

    public PlayerListen(GoCars plugin) {
        this.plugin = plugin;
    }

    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasBlock() || event.getClickedBlock().getType() != Material.BOAT) {
            Player p = event.getPlayer();
            if (p.isInsideVehicle() && p.getVehicle() instanceof Boat && checkBoats((Boat)p.getVehicle())) {
                BoatHandler var3 = getBoatHandler((Boat)p.getVehicle());
            }
        }

    }

    public static boolean checkBoats(Boat boat) {
        return GoCars.boats != null && GoCars.boats.get(boat.getEntityId()) != null;
    }

    public static BoatHandler getBoatHandler(Boat boat) {
        return (BoatHandler)GoCars.boats.get(boat.getEntityId());
    }
}
