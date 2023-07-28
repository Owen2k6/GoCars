//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.owen2k6.gocars;

import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
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
            if (p.isInsideVehicle()) {
                if (p.getVehicle() instanceof Boat) {
                    if (checkBoats((Boat)p.getVehicle())) {
                        BoatHandler boat = getBoatHandler((Boat)p.getVehicle());
                        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
                            if (boat.delay == 0L) {
                                boat.doArmSwing();
                            }
                        } else {
                            boat.doRightClick();
                        }

                    }
                }
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
