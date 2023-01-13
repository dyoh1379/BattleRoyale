package kr.dyoh1379.battleroyale;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static kr.dyoh1379.battleroyale.Function.*;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        if(team == null) {
            scoreboard.registerNewTeam("player");
        }

        FileConfiguration config = this.getConfig();
        config.addDefault("GameProcess", false);
        config.addDefault("MaxBorderSize", 1000.0);
        config.addDefault("BorderTime", 2400);
        config.options().copyDefaults(true);
        saveConfig();

        getServer().getPluginCommand("battle").setExecutor(new Command());
        getServer().getPluginCommand("battle").setTabCompleter(new Completer());

        getServer().getPluginManager().registerEvents(new EventListener(), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                if (config.getBoolean("GameProcess")) {
                    BorderScheduler();
                }
            }
        },0 ,1);


        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    showScoreboard(p);
                }
            }
        },0 ,5);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
