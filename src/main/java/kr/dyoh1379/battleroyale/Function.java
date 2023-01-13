package kr.dyoh1379.battleroyale;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Function {

    public static Plugin plugin = Bukkit.getPluginManager().getPlugin("BattleRoyale");
    public static FileConfiguration config = plugin.getConfig();

    public static Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    public static Team team = scoreboard.getTeam("player");

    private static WorldBorder world_Border = Bukkit.getWorld("world").getWorldBorder();
    private static WorldBorder end_Border = Bukkit.getWorld("world_the_end").getWorldBorder();
    private static WorldBorder nether_Border = Bukkit.getWorld("world_nether").getWorldBorder();

    public static void showMotd(ServerListPingEvent e) {
        e.setMotd(ChatColor.DARK_RED + "" + ChatColor.BOLD + "BATTLE ROYALE");
    }

    public static void outPlayer(Player p) {
        if (config.getBoolean("GameProcess")) {

            int players = team.getSize();
            String rankMessage = ChatColor.BOLD + "#" + players + " " + p.getName();

            team.removePlayer(p);
            p.setGameMode(GameMode.SPECTATOR);
            Bukkit.broadcastMessage(rankMessage);

            if(players == 2) {
                for (OfflinePlayer winner : team.getPlayers()) {
                    if(winner.isOnline()) {
                        String winnerMessage = ChatColor.GOLD + "" + ChatColor.BOLD + "#" + (players - 1) + " " + winner.getName();

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendTitle(ChatColor.RED + "게임 종료!", winnerMessage);
                        }

                        winner.getPlayer().sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "승리!", winnerMessage);
                        Bukkit.broadcastMessage(winnerMessage);
                        config.set("GameProcess", false);
                        plugin.saveConfig();
                    }
                }
            }
        }
    }

    public static void startGame() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            team.addPlayer(p);
        }

        setBorder();
    }



    public static void BorderScheduler() {
        double BorderSize = config.getDouble("MaxBorderSize");
        double BorderTime = config.getDouble("BorderTime");

        if (world_Border.getSize() > BorderSize / (BorderTime * 20) + 1) {
            double BorderSizeTo = world_Border.getSize() - BorderSize * 2 / (BorderTime * 20);

            world_Border.setSize(BorderSizeTo);
            nether_Border.setSize(BorderSizeTo / 8);
            end_Border.setSize(BorderSizeTo);

        }

        else if (world_Border.getSize() == 1) {
            for (OfflinePlayer p : team.getPlayers()) {
                if(p.isOnline()) {
                    p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20, 1, true, true, true));
                }
            }

        } else {
            for (World world : Bukkit.getWorlds()) {
                world.getWorldBorder().setSize(1);
            }
        }
    }

    private static void setBorder() {
        double BorderSize = config.getDouble("MaxBorderSize") * 2;

        for (World world : Bukkit.getWorlds()) {
            WorldBorder border = world.getWorldBorder();
            border.setCenter(0, 0);
            border.setDamageAmount(0.25);
            border.setDamageBuffer(0);
            border.setWarningDistance(50);
            border.setWarningTime(1);
        }

        world_Border.setSize(BorderSize);
        end_Border.setSize(BorderSize);
        nether_Border.setSize(BorderSize / 8);
    }

    public static void showScoreboard(Player p) {

        Scoreboard NewScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective info = NewScoreboard.registerNewObjective("Info", String.valueOf(Criteria.DUMMY));

        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date = DateFormat.format(new Date());

        int MaxPlayers = Bukkit.getMaxPlayers();
        String players = String.valueOf(team.getPlayers().size());

        String Border;

        if ("world_nether".equals(p.getWorld().getName())) {
            Border = String.valueOf((int) nether_Border.getSize() / 2);
        } else {
            Border = String.valueOf((int) world_Border.getSize() / 2);
        }

        double playerLocationX = p.getLocation().getX();
        double playerLocationZ = p.getLocation().getZ();
        ChatColor borderColor = null;

        if (playerLocationX < 0) playerLocationX = -playerLocationX;
        if (playerLocationZ < 0) playerLocationZ = -playerLocationZ;

        double borderDistanceX = playerLocationX - p.getWorld().getWorldBorder().getSize() / 2;
        double borderDistanceZ = playerLocationZ - p.getWorld().getWorldBorder().getSize() / 2;

        if (borderDistanceX < 0) borderDistanceX = -borderDistanceX;
        if (borderDistanceZ < 0) borderDistanceZ = -borderDistanceZ;

        double BorderDouble = Double.parseDouble(Border);
        double warningRate;

        if ("world_nether".equals(p.getWorld().getName())) {
            warningRate = 8;
        } else {
            warningRate = 1;
        }

        if (BorderDouble > playerLocationX && BorderDouble > playerLocationZ) {
            if (borderDistanceX > 100 / warningRate && borderDistanceZ > 100 / warningRate) {
                borderColor = ChatColor.GREEN;
            }

            else if (borderDistanceX <= 100 / warningRate || borderDistanceZ <= 100 / warningRate) {

                if (borderDistanceX > 50 && borderDistanceZ > 50) {
                    borderColor = ChatColor.YELLOW;
                } else {
                    borderColor = ChatColor.RED;
                }
            }
        } else {
            borderColor = ChatColor.RED;
        }

        String borderString = borderColor + Border + ", -" + Border;

        info.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "배틀로얄");
        info.getScore(ChatColor.GRAY + date).setScore(9);
        info.getScore("").setScore(8);
        info.getScore("플레이어").setScore(7);
        info.getScore(ChatColor.GREEN + players + ChatColor.GRAY + "/" + MaxPlayers).setScore(6);
        info.getScore(" ").setScore(5);
        info.getScore("월드보더").setScore(4);
        info.getScore(ChatColor.GREEN + borderString).setScore(3);
        info.getScore("  ").setScore(2);
        info.getScore(ChatColor.YELLOW + "dyoh1379.mcv.kr").setScore(1);
        info.setDisplaySlot(DisplaySlot.SIDEBAR);
        p.setScoreboard(NewScoreboard);

    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }

    public static boolean isBoolean(String s) {
        List<String> list = Arrays.asList("true", "false");
        return list.contains(s.toLowerCase());
    }
}
