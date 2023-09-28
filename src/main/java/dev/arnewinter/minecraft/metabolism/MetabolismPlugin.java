package dev.arnewinter.minecraft.metabolism;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * The main plugin class for the Metabolism plugin.
 * This plugin manages the metabolic systems of players in the Minecraft server.
 */
public class MetabolismPlugin extends JavaPlugin {
    // Maps to store player-specific metabolic data and corresponding task for each player.
    private final Map<UUID, Guts> guts = new HashMap<>();
    private final Map<UUID, BukkitTask> gutsTasks = new HashMap<>();

    @Override
    public void onEnable() {
        var plugin = this;

        // Register event listeners for player join and quit events.
        this.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerJoin(PlayerJoinEvent ev) {
                // Registers a player's guts and schedules a task to tick it.
                // Also saves the task.

                Guts g;
                if (guts.containsKey(ev.getPlayer().getUniqueId())) {
                    g = guts.get(ev.getPlayer().getUniqueId());
                    g.setPlayer(ev.getPlayer());
                } else {
                    g = new Guts(ev.getPlayer());
                    guts.put(ev.getPlayer().getUniqueId(), g);
                }

                // Schedule a task to tick the player's guts every 60 seconds.
                var task = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> g.tick(), 0, 20 * 60);
                gutsTasks.put(ev.getPlayer().getUniqueId(), task);
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent ev) {
                // Clears the gut ticking task, while keeping the player's metabolic data intact.

                var task = gutsTasks.get(ev.getPlayer().getUniqueId());
                if (task != null) {
                    task.cancel();
                    gutsTasks.remove(ev.getPlayer().getUniqueId());
                }
            }
        }, this);
    }
}