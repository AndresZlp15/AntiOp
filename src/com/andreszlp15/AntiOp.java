package com.andreszlp15;

import org.bukkit.plugin.java.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.command.*;
import java.util.*;
import org.bukkit.plugin.*;

public class AntiOp extends JavaPlugin
{
    public List<Player> playerlist;
    public List<String> list;
    public boolean allowedOp;
    
    public AntiOp() {
        this.playerlist = new ArrayList<Player>();
        this.list = new ArrayList<String>();
        this.allowedOp = false;
    }
    
    public void onEnable() {
        this.registerConfig();
        Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this, (Runnable)new Runnable() {
            @Override
            public void run() {
                for (final Player p : Bukkit.getOnlinePlayers()) {
                    AntiOp.this.playerlist.add(p);
                    if (AntiOp.this.getConfig().getBoolean("opListCheck")) {
                        for (final String s : AntiOp.this.getConfig().getStringList("opList")) {
                            if (s.equals(p.getName())) {
                                AntiOp.this.allowedOp = true;
                            }
                        }
                    }
                    if (AntiOp.this.getConfig().getBoolean("permissionCheck")) {
                        if (AntiOp.this.getConfig().getBoolean("opListOverridePermissionCheck")) {
                            if (!AntiOp.this.allowedOp && p.hasPermission(AntiOp.this.getConfig().getString("permission"))) {
                                AntiOp.this.getLogger().info(String.valueOf(p.getName()) + " has the Anti-Op permission and isn't on the list!");
                                for (final String s : AntiOp.this.getConfig().getStringList("commandsToRun")) {
                                    Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), s.replaceAll("%player%", p.getName()));
                                }
                                AntiOp.this.getLogger().info(String.valueOf(p.getName()) + " has been de-opped and banned!");
                            }
                        }
                        else if (p.hasPermission(AntiOp.this.getConfig().getString("permission"))) {
                            AntiOp.this.getLogger().info(String.valueOf(p.getName()) + " has the Anti-Op permission!");
                            for (final String s : AntiOp.this.getConfig().getStringList("commandsToRun")) {
                                Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), s.replaceAll("%player%", p.getName()));
                            }
                            AntiOp.this.getLogger().info(String.valueOf(p.getName()) + " has been de-opped and banned!");
                        }
                    }
                    if (AntiOp.this.getConfig().getBoolean("opListCheck") && !AntiOp.this.allowedOp && (p.isOp() || p.hasPermission("*"))) {
                        AntiOp.this.getLogger().info(String.valueOf(p.getName()) + " has unauthorized op!");
                        for (final String s : AntiOp.this.getConfig().getStringList("commandsToRun")) {
                            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), s.replaceAll("%player%", p.getName()));
                        }
                        AntiOp.this.getLogger().info(String.valueOf(p.getName()) + " has been de-opped and banned!");
                    }
                    AntiOp.this.allowedOp = false;
                }
            }
        }, 0L, (long)this.getConfig().getInt("intervalBetweenChecks"));
    }
    
    public void registerConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }
}
