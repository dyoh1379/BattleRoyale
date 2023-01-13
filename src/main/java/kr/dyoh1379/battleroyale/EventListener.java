package kr.dyoh1379.battleroyale;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;

import static kr.dyoh1379.battleroyale.Function.*;

public class EventListener implements Listener {

    private String blockedMessage = "게임 시작 전에는 활동할 수 없습니다";

    @EventHandler
    public void move(EntityMoveEvent e) {
        Entity entity = e.getEntity();
        if (!config.getBoolean("GameProcess")) {
            if (!(entity instanceof Player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void movePlayer(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (!config.getBoolean("GameProcess")) {
            if (!p.isOp()) {
                e.setCancelled(true);
                e.getPlayer().sendActionBar(this.blockedMessage);
            }
        }
    }

    @EventHandler
    public void damage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (!config.getBoolean("GameProcess")) {
            if (!entity.isOp()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void hunger(FoodLevelChangeEvent e) {
        Entity entity = e.getEntity();
        if (!config.getBoolean("GameProcess")) {
            if (!entity.isOp()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (!config.getBoolean("GameProcess")) {
            if (!p.isOp()) {e.setCancelled(true);
                e.getPlayer().sendActionBar(this.blockedMessage);
            }
        }
    }

    @EventHandler
    public void placeBlock(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (!config.getBoolean("GameProcess")) {
            if (!p.isOp()) {
                e.setCancelled(true);
                e.getPlayer().sendActionBar(this.blockedMessage);
            }
        }
    }

    @EventHandler
    public void motd(ServerListPingEvent e) {
        showMotd(e);
    }

    @EventHandler
    public void death(PlayerDeathEvent e) {
        Player p = e.getPlayer();
        outPlayer(p);
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (p.getFireTicks() > 1) {
            outPlayer(p);
        }

    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (config.getBoolean("GameProcess")) {
            if (!team.getPlayers().contains(p)) {
                p.setGameMode(GameMode.SPECTATOR);
                p.sendMessage("게임이 이미 시작되어 관전자가 되었습니다");
            }
        }

        showScoreboard(p);
    }

    @EventHandler
    public void chat(PlayerChatEvent e) {
        Player p = e.getPlayer();
        if (config.getBoolean("GameProcess")) {
            if (!team.getPlayers().contains(p.getName())) {
                if (!p.isOp()) {
                    e.setCancelled(true);
                    p.sendMessage("관전자는 채팅을 사용할 수 없습니다");
                }
            }
        }
    }

    @EventHandler
    public void command(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (config.getBoolean("GameProcess")) {
            if (!team.getPlayers().contains(p.getName())) {
                if (!p.isOp()) {
                    e.setCancelled(true);
                    p.sendMessage("관전자는 명령어를 사용할 수 없습니다");
                }
            }
        }
    }
}
