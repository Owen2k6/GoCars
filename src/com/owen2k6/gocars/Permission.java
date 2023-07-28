//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.owen2k6.gocars;

//import com.nijikokun.bukkit.Permissions.Permissions;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Permission {
    //private static Permissions permissionsPlugin;
    private static boolean permissionsEnabled = false;

    public Permission() {
    }

    public static void initialize(Server server) {
        Plugin test = server.getPluginManager().getPlugin("Permissions");
        Logger log;
        if (test != null) {
            log = Logger.getLogger("Minecraft");
            //permissionsPlugin = (Permissions)test;
            permissionsEnabled = true;
            log.log(Level.INFO, "[SkyPirates]: Permissions enabled.");
        } else {
            permissionsEnabled = false;
            log = Logger.getLogger("Minecraft");
            log.log(Level.SEVERE, "[SkyPirates]: Nijikokuns' Permissions plugin isn't loaded, everyone can use all features.");
        }

    }

    public static boolean isAdmin(Player player) {
        return permissionsEnabled ? permission(player, "skypirates.admin") : player.isOp();
    }

    private static boolean permission(Player player, String string) {
        return true;//Permissions.Security.permission(player, string);
    }

    public static boolean hasEnable(Player player) {
        return permissionsEnabled ? permission(player, "skypirates.enable") : true;
    }

    public static boolean genericCheck(Player player, String str) {
        return permissionsEnabled ? permission(player, str) : true;
    }
}
