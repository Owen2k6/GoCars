//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.owen2k6.gocars;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleListener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

public class VehicleListen extends VehicleListener {
    private final GoCars plugin;
    public double fromYaw;
    public double toYaw;
    public Location from;
    public Location to;

    public VehicleListen(GoCars plugin) {
        this.plugin = plugin;
    }

    public void onVehicleMove(VehicleMoveEvent event) {
        if (event.getVehicle() instanceof Boat && event.getVehicle().getPassenger() instanceof Player) {
            Player p = (Player)event.getVehicle().getPassenger();
            if (p.isInsideVehicle() && PlayerListen.checkBoats((Boat)event.getVehicle())) {
                this.from = event.getFrom();
                this.to = event.getTo();
                Boat tempBoat = (Boat)event.getVehicle();
                Vector vel = tempBoat.getVelocity();
                BoatHandler boat = PlayerListen.getBoatHandler(tempBoat);
                boat.doYaw(this.from, this.to);
                boat.updateCalendar();
                if (boat.isMoving() && boat.getMovingLastTick()) {
                    boat.movementHandler(vel);
                }
            }
        } else {
            super.onVehicleMove(event);
        }

    }

    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player) {
            Player player = (Player)event.getEntered();
            if (event.getVehicle() instanceof Boat) {
                GoCars.playerModes.putIfAbsent(player, 0);
                BoatHandler boat;
                if (!PlayerListen.checkBoats((Boat)event.getVehicle())) {
                    boat = new BoatHandler((Boat)event.getVehicle(), (Integer)GoCars.playerModes.get(player), event.getVehicle().getEntityId(), player.getName());
                    GoCars.boats.put(boat.getEntityId(), boat);
                    player.sendMessage(ChatColor.AQUA + "The DVLA is checking your license...");
                    String filePath = "plugins/GoCars/" + player.getName() + ".txt";
                    File file = new File(filePath);
                    if (!file.exists()) {
                        player.sendMessage(ChatColor.RED + "You do not have a licence, you cannot drive this vehicle on roads.");
                        event.getVehicle().getPassenger().eject();
                        event.getVehicle().eject();
                        event.setCancelled(true);
                        return;
                    }
                } else {
                    boat = (BoatHandler)GoCars.boats.get(event.getVehicle().getEntityId());
                    if (!boat.isOwner(player)) {
                        player.sendMessage(ChatColor.RED + "You are not the owner of this vehicle.");
                        player.sendMessage(ChatColor.RED + "This vehicle is registered to " + boat.getOwnerUsername() + ".");
                        event.getVehicle().getPassenger().eject();
                        event.getVehicle().eject();
                        event.setCancelled(true);
                        return;
                    }

                    player.sendMessage(ChatColor.AQUA + "Welcome Back to your Vehicle.");
                }

                boat.setMode((Integer)GoCars.playerModes.get(player));
                super.onVehicleEnter(event);
            }
        }

    }

    public void onVehicleExit(VehicleExitEvent event) {
        if (event.getExited() instanceof Player && event.getVehicle() instanceof Boat && PlayerListen.checkBoats((Boat)event.getVehicle())) {
            BoatHandler boat = (BoatHandler)GoCars.boats.get(event.getVehicle().getEntityId());
            Player p = (Player)event.getExited();
            if (boat.getMode() == 3) {
                p.sendMessage(ChatColor.LIGHT_PURPLE + "Vehicle Parked.");
            }

            boat.setMode(0);
            super.onVehicleExit(event);
        }

    }

    public void onVehicleDamage(VehicleDamageEvent event) {
        if (event.getVehicle() instanceof Boat && event.getVehicle().getPassenger() instanceof Player) {
            if (PlayerListen.checkBoats((Boat)event.getVehicle())) {
                Player p = (Player)event.getVehicle().getPassenger();
                BoatHandler var10000 = (BoatHandler)GoCars.boats.get(event.getVehicle().getEntityId());
                event.setDamage(0);
                event.setCancelled(true);
            }
        } else {
            super.onVehicleDamage(event);
        }

    }

    public void onVehicleBlockCollision(VehicleBlockCollisionEvent event) {
        if (event.getVehicle() instanceof Boat && event.getVehicle().getPassenger() instanceof Player) {
            event.getVehicle().teleport(event.getVehicle().getPassenger());
        } else {
            super.onVehicleBlockCollision(event);
        }

    }
}
