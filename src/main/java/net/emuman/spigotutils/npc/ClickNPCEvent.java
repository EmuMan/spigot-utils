package net.emuman.spigotutils.npc;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * An event that represents a click (either attack or interact) on a created NPC.
 */
public class ClickNPCEvent extends Event implements Cancellable {

    private final Player player;
    private final NPC npc;
    private final boolean isAttack;
    private boolean isCancelled;
    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Creates a new ClickNPCEvent. Should generally not be created in this manner by user.
     *
     * @param player   the player who performed the click
     * @param npc      the NPC that was clicked.
     * @param isAttack true if the click was an attack (left click), false otherwise.
     */
    public ClickNPCEvent(Player player, NPC npc, boolean isAttack) {
        this.player = player;
        this.npc = npc;
        this.isAttack = isAttack;
    }

    /**
     * @return the player who performed the click.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the NPC that was clicked.
     */
    public NPC getNPC() {
        return npc;
    }

    /**
     * @return true if the click was an attack (left click), false otherwise.
     */
    public boolean isAttack() {
        return isAttack;
    }

    /**
     * Used by Bukkit. Should not be called.
     *
     * @return the handler list.
     */
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Used by Bukkit. Should not be called.
     *
     * @return the handler list.
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * @return true if the event has been cancelled, false otherwise.
     */
    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * @param b true if the event should be cancelled, false otherwise.
     */
    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }

}
