//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.owen2k6.gocars;

import java.io.File;
import org.bukkit.util.config.Configuration;

public class config extends Configuration {
    private static config singleton;

    private config(GoCars plugin) {
        super(new File(plugin.getDataFolder(), "godsaveus.yml"));
        this.reload();
    }

    private void write() {
        this.generateConfigOption("config-version", 0);
        this.generateConfigOption("Insurance.Rate", 0.02);
        this.generateConfigOption("National.Speed.Limit", 5.0);
    }

    public void generateConfigOption(String key, Object defaultValue) {
        if (this.getProperty(key) == null) {
            this.setProperty(key, defaultValue);
        }

        Object value = this.getProperty(key);
        this.removeProperty(key);
        this.setProperty(key, value);
    }

    public Object getConfigOption(String key) {
        return this.getProperty(key);
    }

    public String getConfigString(String key) {
        return String.valueOf(this.getConfigOption(key));
    }

    public Integer getConfigInteger(String key) {
        return Integer.valueOf(this.getConfigString(key));
    }

    public Long getConfigLong(String key) {
        return Long.valueOf(this.getConfigString(key));
    }

    public Double getConfigDouble(String key) {
        return Double.valueOf(this.getConfigString(key));
    }

    public Boolean getConfigBoolean(String key) {
        return Boolean.valueOf(this.getConfigString(key));
    }

    public Long getConfigLongOption(String key) {
        return this.getConfigOption(key) == null ? null : Long.valueOf(String.valueOf(this.getProperty(key)));
    }

    private boolean convertToNewAddress(String newKey, String oldKey) {
        if (this.getString(newKey) != null) {
            return false;
        } else if (this.getString(oldKey) == null) {
            return false;
        } else {
            System.out.println("Converting Config: " + oldKey + " to " + newKey);
            Object value = this.getProperty(oldKey);
            this.setProperty(newKey, value);
            this.removeProperty(oldKey);
            return true;
        }
    }

    private void reload() {
        this.load();
        this.write();
        this.save();
    }

    public static config getInstance() {
        if (singleton == null) {
            throw new RuntimeException("A instance of Fundamentals hasn't been passed into FundamentalsConfig yet.");
        } else {
            return singleton;
        }
    }

    public static config getInstance(GoCars plugin) {
        if (singleton == null) {
            singleton = new config(plugin);
        }

        return singleton;
    }
}
