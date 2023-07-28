//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.owen2k6.gocars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GoCars extends JavaPlugin {
    public VehicleListen vl = new VehicleListen(this);
    public PlayerListen pl = new PlayerListen(this);
    public static HashMap<Player, Integer> playerModes = new HashMap();
    public static HashMap<Integer, BoatHandler> boats = new HashMap();
    public static ArrayList<String> helmets = new ArrayList();
    public static GoCars plugin;
    private static final String codename = "Caribbean";
    public static Logger log = Logger.getLogger("Minecraft");

    public GoCars() {
    }

    public void onLoad() {
    }

    public void onEnable() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvent(Type.VEHICLE_COLLISION_BLOCK, this.vl, Priority.Normal, this);
        pm.registerEvent(Type.VEHICLE_MOVE, this.vl, Priority.Normal, this);
        pm.registerEvent(Type.VEHICLE_ENTER, this.vl, Priority.Normal, this);
        pm.registerEvent(Type.VEHICLE_EXIT, this.vl, Priority.Normal, this);
        pm.registerEvent(Type.VEHICLE_DAMAGE, this.vl, Priority.Normal, this);
        pm.registerEvent(Type.PLAYER_INTERACT, this.pl, Priority.Normal, this);
        this.populateHelmets();
        PluginDescriptionFile pdfFile = this.getDescription();
        Permission.initialize(this.getServer());
        log.info("[" + pdfFile.getName() + "]: version [" + pdfFile.getVersion() + "] (" + "Caribbean" + ") loaded");
    }

    public void onDisable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info("[" + pdfFile.getName() + "]: version [" + pdfFile.getVersion() + "] (" + "Caribbean" + ") disabled");
    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        String commandName = command.getName().toLowerCase();
        if (!(sender instanceof Player)) {
            sender.sendMessage("[GoCars]: Must be ingame to use this command.");
            return true;
        } else {
            Player player = (Player) sender;
            commandName = "/" + commandName;
            String parameters = "";
            String[] var11 = args;
            int var10 = args.length;

            String fullCommand;
            for (int var9 = 0; var9 < var10; ++var9) {
                fullCommand = var11[var9];
                parameters = parameters + " " + fullCommand;
            }

            fullCommand = commandName + parameters;
            String[] split = fullCommand.split(" ");
            if ((split[0].equals("/gocars"))) {
                BoatHandler boat = PlayerListen.getBoatHandler((Boat) player.getVehicle());
                player.sendMessage(ChatColor.RED + "Boats must be in water to activate car mode correctly!");
                player.sendMessage(ChatColor.GOLD + "Your boat is now in Car mode!");
                player.sendMessage(ChatColor.AQUA + "You will need a Stick to control your speed!");
                player.sendMessage(ChatColor.AQUA + "Stick Controls: Left Click to accelerate, Right Click to decelerate!");
                player.sendMessage(ChatColor.AQUA + "Your boat should hover above the ground, use it like a normal boat to drive!");
                playerModes.put(player, 3);
                boat.setMode(3);
                return true;
            } else if ((split[0].equals("/goboat"))) {
                BoatHandler boat = PlayerListen.getBoatHandler((Boat) player.getVehicle());
                player.sendMessage(ChatColor.GOLD + "Your boats will no longer become a car.");
                playerModes.put(player, 0);
                boat.setMode(0);
                return true;
            } else if ((split[0].equals("/roadrules"))) {
                player.sendMessage(ChatColor.GOLD + "Road Rules:");
                player.sendMessage(ChatColor.AQUA + "1. You must drive on the left side of the road.");
                player.sendMessage(ChatColor.AQUA + "2. You must not drive into players.");
                player.sendMessage(ChatColor.AQUA + "3. You must not drive into buildings. (you will most likely lose your boat.)");
                player.sendMessage(ChatColor.AQUA + "4. You must drive on the road.");
                player.sendMessage(ChatColor.AQUA + "5. Stick to the speed limits. (/speedlimits)");
                player.sendMessage(ChatColor.AQUA + "6. Be aware of the road sign colour formats. (/roadsigns)");
                player.sendMessage(ChatColor.AQUA + "7. You must not drive into other vehicles.");
                return true;
            } else if ((split[0].equals("/speedlimits"))){
                player.sendMessage(ChatColor.GOLD + "Speed Limits:");
                player.sendMessage(ChatColor.GOLD + "If the road states a different speed limit, you must follow it.");
                player.sendMessage(ChatColor.GOLD + "Speeds are in N.Nx format so you dont need to worry about conversions");
                player.sendMessage(ChatColor.AQUA + "CLASS P ROADS (private roads): No limit");
                player.sendMessage(ChatColor.AQUA + "CLASS MX ROADS: 4.2x");
                player.sendMessage(ChatColor.AQUA + "CLASS M ROADS: 4.0x");
                player.sendMessage(ChatColor.AQUA + "CLASS A ROADS: 3.5x");
                player.sendMessage(ChatColor.AQUA + "CLASS B ROADS: 2.5x");
                player.sendMessage(ChatColor.AQUA + "CLASS C ROADS: 1.5x");
                player.sendMessage(ChatColor.AQUA + "CLASS D ROADS: 0.5x");
                return true;
            } else if ((split[0].equals("/roadsigns"))){
                player.sendMessage(ChatColor.GOLD + "Road Sign Colour Formats:");
                player.sendMessage(ChatColor.GOLD + "Road signs are colour coded to help you understand the road.");
                player.sendMessage(ChatColor.AQUA + "CLASS P ROADS are not affiliated with RoadsGMC. They are private roads.");
                player.sendMessage(ChatColor.AQUA + "CLASS MX ROADS: "+ ChatColor.BLUE +"Dark Blue");
                player.sendMessage(ChatColor.AQUA + "CLASS M ROADS: "+ ChatColor.AQUA +"Light Blue");
                player.sendMessage(ChatColor.AQUA + "CLASS A ROADS: "+ ChatColor.GREEN +"Green");
                player.sendMessage(ChatColor.AQUA + "CLASS B ROADS: "+ ChatColor.WHITE +"White");
                player.sendMessage(ChatColor.AQUA + "CLASS C ROADS: "+ ChatColor.WHITE +"Light Grey");
                player.sendMessage(ChatColor.AQUA + "CLASS D ROADS: "+ ChatColor.WHITE +"Black");
                player.sendMessage(ChatColor.AQUA + "Informational Signs:"+ ChatColor.YELLOW +"Yellow");
                player.sendMessage(ChatColor.AQUA + "Warning Signs:"+ ChatColor.RED +"Red");
                player.sendMessage(ChatColor.AQUA + "Speed Limit:"+ ChatColor.RED +"Red ring"+ChatColor.AQUA +" with a" + ChatColor.WHITE + " white centre.");
                return true;
            }

            else {
                return false;
            }
        }
    }

    public void populateHelmets() {
        helmets.add("298");
        helmets.add("306");
        helmets.add("310");
        helmets.add("314");
    }
}
