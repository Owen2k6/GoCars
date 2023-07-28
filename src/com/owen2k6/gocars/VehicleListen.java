//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.owen2k6.gocars;

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
            if (p.isInsideVehicle()) {
                if (PlayerListen.checkBoats((Boat)event.getVehicle())) {
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
            }
        } else {
            super.onVehicleMove(event);
        }
    }

    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player) {
            Player player = (Player)event.getEntered();
            if (event.getVehicle() instanceof Boat) {
                if (Permission.genericCheck(player, "GoCars.player.enable")) {
                    if (GoCars.playerModes.get(player) == null) {
                        GoCars.playerModes.put(player, 0);
                    }

                    BoatHandler boat;
                    if (!PlayerListen.checkBoats((Boat)event.getVehicle())) {
                        boat = new BoatHandler((Boat)event.getVehicle(), (Integer)GoCars.playerModes.get(player), event.getVehicle().getEntityId());
                        GoCars.boats.put(boat.getEntityId(), boat);
                        player.sendMessage(ChatColor.AQUA + "You can enable Car Mode with /gocars and disable it with /goboat.");
                    } else {
                        boat = (BoatHandler)GoCars.boats.get(event.getVehicle().getEntityId());
                        player.sendMessage(ChatColor.AQUA + "You can enable Car Mode with /gocars and disable it with /goboat.");
                    }

                    boat.setMode((Integer)GoCars.playerModes.get(player));
                    super.onVehicleEnter(event);
                }
            }
        }
    }

    public void onVehicleExit(VehicleExitEvent event) {
        if (event.getExited() instanceof Player) {
            if (event.getVehicle() instanceof Boat) {
                if (PlayerListen.checkBoats((Boat)event.getVehicle())) {
                    BoatHandler boat = (BoatHandler)GoCars.boats.get(event.getVehicle().getEntityId());
                    Player p = (Player)event.getExited();
                    if (boat.getMode() == 3) {
                        p.sendMessage(ChatColor.LIGHT_PURPLE + "Your car has been parked. If you re-enter it, it will be a car again.");
                    }
                    boat.setMode(0);
                    super.onVehicleExit(event);
                }
            }
        }
    }

    public void onVehicleDamage(VehicleDamageEvent event) {
        if (event.getVehicle() instanceof Boat && event.getVehicle().getPassenger() instanceof Player) {
            if (PlayerListen.checkBoats((Boat)event.getVehicle())) {
                Player p = (Player)event.getVehicle().getPassenger();
                BoatHandler boat = (BoatHandler)GoCars.boats.get(event.getVehicle().getEntityId());
                if (Permission.genericCheck(p, "GoCars.admin.invincible") && boat.getItemInHandID() == 49 && Permission.genericCheck(p, "GoCars.items.obsidian")) {
                    event.setDamage(0);
                    event.setCancelled(true);
                }
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
