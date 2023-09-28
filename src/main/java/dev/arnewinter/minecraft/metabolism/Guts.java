/**
 * The Guts class represents a player's digestive system in a Minecraft plugin called Metabolism.
 * It keeps track of the player's "guts" level, simulating a digestive process, and triggers
 * an event when the guts reach a certain level.
 */
package dev.arnewinter.minecraft.metabolism;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Guts {
    private Player player;
    private int amount = -10;

    /**
     * Constructs a Guts object for a specific player.
     *
     * @param player The player for whom the Guts object is created.
     */
    public Guts(Player player){
        this.player = player;
    }

    /**
     * Sets the player associated with this Guts object.
     *
     * @param player The player to associate with this Guts object.
     */
    public void setPlayer(Player player){
        this.player = player;
    }

    /**
     * Simulates the passage of time and increases the player's guts level.
     * Notifies the player about their guts level and triggers an event if the guts are full.
     */
    public void tick() {
        amount += 10;
        player.sendMessage("Your guts are " + amount + "% full!");

        if (amount == 100) {
            poopPants();
        }
    }

    /**
     * Triggers an event when the player's guts are full. It broadcasts a message to the server,
     * changes the player's block to represent that they've pooped, and resets the guts level to zero.
     */
    public void poopPants() {
        player.getServer().broadcastMessage(player.getName() + " pooped their pants!");
        player.sendMessage("Your guts are empty.");

        Block b = player.getLocation().getBlock();
        if (b.getType().isAir()) {
            b.setType(Material.BROWN_CARPET);
        }
        
        amount = 0;
    }
}
