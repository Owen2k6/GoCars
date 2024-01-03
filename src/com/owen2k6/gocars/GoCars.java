//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.owen2k6.gocars;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
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
        log.info("[" + pdfFile.getName() + "]: version [" + pdfFile.getVersion() + "] loaded");
    }

    public void onDisable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info("[" + pdfFile.getName() + "]: version [" + pdfFile.getVersion() + "] disabled");
    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        String commandName = command.getName().toLowerCase();
        if (!(sender instanceof Player)) {
            sender.sendMessage("[GoCars]: Must be ingame to use this command.");
            return true;
        } else {
            Player player = (Player)sender;
            commandName = "/" + commandName;
            String parameters = "";
            String[] var11 = args;
            int var10 = args.length;

            String fullCommand;
            for(int var9 = 0; var9 < var10; ++var9) {
                fullCommand = var11[var9];
                parameters = parameters + " " + fullCommand;
            }

            fullCommand = commandName + parameters;
            String[] split = fullCommand.split(" ");
            BoatHandler boat;
            switch (split[0]) {
                case "/car":
                    boat = PlayerListen.getBoatHandler((Boat)player.getVehicle());
                    player.sendMessage(ChatColor.RED + "Boats must be in water to activate car mode correctly!");
                    player.sendMessage(ChatColor.GOLD + "Your boat is now in Car mode!");
                    player.sendMessage(ChatColor.AQUA + "Your boat should hover above the ground, use it like a normal boat to drive!");
                    playerModes.put(player, 3);
                    boat.setMode(3);
                    return true;
                case "/boat":
                    boat = PlayerListen.getBoatHandler((Boat)player.getVehicle());
                    player.sendMessage(ChatColor.GOLD + "You have disabled car mode.");
                    playerModes.put(player, 0);
                    boat.setMode(0);
                    return true;
                case "/dvla":
                    if (split.length == 1) {
                        player.sendMessage(ChatColor.AQUA + "The DVLA ensures that players are educated in the rules of the road. And have all nessaesary documents.");
                        return true;
                    } else {
                        switch (split[1]) {
                            case "rules":
                                player.sendMessage(ChatColor.GOLD + "Road Rules:");
                                player.sendMessage(ChatColor.AQUA + "1. You must drive on the left side of the road.");
                                player.sendMessage(ChatColor.AQUA + "2. Abide by all speed limits.");
                                player.sendMessage(ChatColor.AQUA + "3. Avoid crashing into other vehicles.");
                                player.sendMessage(ChatColor.AQUA + "4. Avoid crashing into buildings. (Your boat will most likely be destroyed.)");
                                player.sendMessage(ChatColor.AQUA + "5. You must have a valid driving licence and registration.");
                                player.sendMessage(ChatColor.AQUA + "6. Read all provided info panels on the DVLA command service.");
                                break;
                            case "signs":
                                player.sendMessage(ChatColor.GOLD + "Road Signs:");
                                player.sendMessage(ChatColor.AQUA + "Our signs on the road network are colour coded to help you understand the road.");
                                player.sendMessage(ChatColor.AQUA + "CLASS P ROADS are not affiliated with RoadsGMC. They are private roads.");
                                player.sendMessage(ChatColor.AQUA + "CLASS M ROADS: " + ChatColor.AQUA + "Blue(AQUA)");
                                player.sendMessage(ChatColor.AQUA + "CLASS A ROADS: " + ChatColor.GREEN + "Green");
                                player.sendMessage(ChatColor.AQUA + "CLASS B ROADS: " + ChatColor.WHITE + "White");
                                player.sendMessage(ChatColor.AQUA + "CLASS C ROADS: " + ChatColor.WHITE + "WHITE");
                                break;
                            case "speed":
                                player.sendMessage(ChatColor.GOLD + "Speed Limits:");
                                player.sendMessage(ChatColor.GOLD + "If the road states a different speed limit, you must follow it.");
                                player.sendMessage(ChatColor.RED + "Due to limitations on MC, We are lenient on speed limits as you can't easily find out your speeds.");
                                player.sendMessage(ChatColor.AQUA + "CLASS P ROADS: Recommended limit is 70mph.");
                                player.sendMessage(ChatColor.AQUA + "CLASS M ROADS: 70mph. (31 blocks per second)");
                                player.sendMessage(ChatColor.AQUA + "CLASS A ROADS: 70mph. (31 blocks per second)");
                                player.sendMessage(ChatColor.AQUA + "CLASS B ROADS: 60mph. (27 blocks per second)");
                                player.sendMessage(ChatColor.AQUA + "CLASS C ROADS: 20mph. (9 blocks per second)");
                                break;
                            case "classes":
                                player.sendMessage(ChatColor.GOLD + "Road Classes:");
                                player.sendMessage(ChatColor.AQUA + "CLASS P ROADS: Private Road not managed by RoadsGMC.");
                                player.sendMessage(ChatColor.AQUA + "CLASS M ROADS: Motorway");
                                player.sendMessage(ChatColor.AQUA + "CLASS A ROADS: Priority Road");
                                player.sendMessage(ChatColor.AQUA + "CLASS B ROADS: Back roads");
                                player.sendMessage(ChatColor.AQUA + "CLASS C ROADS: City Roads");
                                break;
                            case "licence":
                                player.sendMessage(ChatColor.GOLD + "Licence:");
                                player.sendMessage(ChatColor.AQUA + "To get a licence, you must register for one at the DVLA");
                                player.sendMessage(ChatColor.AQUA + "We have several locations you may visit to register.");
                                player.sendMessage(ChatColor.AQUA + "You may also register online on Discord in the #roadsgmc channel.");
                                player.sendMessage(ChatColor.AQUA + "You must have a valid licence to drive on the road network.");
                                break;
                            case "registration":
                                player.sendMessage(ChatColor.GOLD + "Registration:");
                                player.sendMessage(ChatColor.AQUA + "You are automatically registered when you get a licence.");
                                player.sendMessage(ChatColor.AQUA + "Your registration plate is always with you no matter what vehicle you drive.");
                                break;
                            case "locations":
                                player.sendMessage(ChatColor.GOLD + "Locations:");
                                player.sendMessage(ChatColor.AQUA + "DVLA Goplexia: " + ChatColor.AQUA + "X: 122 Z: -170");
                                player.sendMessage(ChatColor.AQUA + "DVLA Omegamall: " + ChatColor.AQUA + "Under Construction");
                                break;
                            case "lookup":
                                if (split.length == 2) {
                                    player.sendMessage(ChatColor.RED + "You must specify a player to lookup.");
                                    return true;
                                }

                                String target = split[2];
                                if (target == null) {
                                    player.sendMessage(ChatColor.RED + "You must specify a player to lookup.");
                                    return true;
                                }

                                String filePath = "plugins/GoCars/" + target + ".txt";
                                String registrationCode = "";
                                String Status = "";
                                String issuedate = "";
                                String expirydate = "";
                                long daysAgo = 0L;
                                int points = 0;

                                try {
                                    BufferedReader reader = new BufferedReader(new FileReader(filePath));
                                    registrationCode = reader.readLine();
                                    String pointsStr = reader.readLine();
                                    if (pointsStr != null) {
                                        points = Integer.parseInt(pointsStr);
                                    }

                                    Status = reader.readLine();
                                    issuedate = reader.readLine();
                                    String expiryunix = reader.readLine();
                                    long expiry = Long.parseLong(expiryunix);
                                    long currentTimestamp = Instant.now().getEpochSecond();
                                    long thirtyDaysAgo = currentTimestamp - 2592000L;
                                    daysAgo = (currentTimestamp - expiry) / 86400L;
                                    reader.close();
                                } catch (FileNotFoundException e) {
//                                    registrationCode = "Not on record.";
//                                    points = 0;
//                                    player.sendMessage(var36.toString());
                                    player.sendMessage(ChatColor.RED+"This player does not have a licence.");
                                    break;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    player.sendMessage(e.toString());
                                }

                                if (!player.hasPermission("dvla.scan") && !target.equals(player.getName())) {
                                    player.sendMessage(ChatColor.GOLD + "DVLA System Search for " + target + " has returned the following:");
                                    player.sendMessage(ChatColor.AQUA + "Vehicle Registration: " + registrationCode);
                                    player.sendMessage(ChatColor.RED + "Information has been redacted due to lack of permissions.");
                                } else {
                                    player.sendMessage(ChatColor.GOLD + "DVLA System Search for " + target + " has returned the following:");
                                    player.sendMessage(ChatColor.AQUA + "Vehicle Registration: " + registrationCode);
                                    player.sendMessage(ChatColor.AQUA + "Licence Points: " + points);
                                    player.sendMessage(ChatColor.AQUA + "Licence Status: " + Status);
                                    player.sendMessage(ChatColor.AQUA + "Licence Issue Date: " + issuedate);
                                    player.sendMessage(ChatColor.AQUA + "Tax was paid: " + (daysAgo < 30L ? ChatColor.GREEN + "" + daysAgo + " days ago." : ChatColor.RED + "" + daysAgo + " days ago."));
                                }
                                break;
                            case "help":
                                player.sendMessage(ChatColor.GOLD + "Help:");
                                player.sendMessage(ChatColor.AQUA + "Here is a list of commands you can do.");
                                player.sendMessage(ChatColor.WHITE + "/dvla rules: " + ChatColor.AQUA + "Shows you the rules of the road.");
                                player.sendMessage(ChatColor.WHITE + "/dvla signs: " + ChatColor.AQUA + "Shows you the road signs.");
                                player.sendMessage(ChatColor.WHITE + "/dvla speed: " + ChatColor.AQUA + "Shows you the speed limits.");
                                player.sendMessage(ChatColor.WHITE + "/dvla classes: " + ChatColor.AQUA + "Shows you the road classes.");
                                player.sendMessage(ChatColor.WHITE + "/dvla licence: " + ChatColor.AQUA + "Shows you how to get a licence.");
                                player.sendMessage(ChatColor.WHITE + "/dvla registration: " + ChatColor.AQUA + "Shows you how to get a registration plate.");
                                player.sendMessage(ChatColor.WHITE + "/dvla locations: " + ChatColor.AQUA + "Shows you the locations of the DVLA.");
                                player.sendMessage(ChatColor.WHITE + "/dvla help: " + ChatColor.AQUA + "Shows you this help page.");
                                player.sendMessage(ChatColor.WHITE + "/dvla lookup: " + ChatColor.AQUA + "Shows you the licence information of a player.");
                        }

                        return true;
                    }
                default:
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
