package com.rtsmod.rts.core;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.*;

/**
 * Per-player RTS state: selected units, mode toggle.
 */
public class RTSPlayer {
    private static final Map<UUID, RTSPlayer> PLAYERS = new HashMap<>();

    private final UUID playerId;
    private final Set<Integer> selectedEntityIds = new HashSet<>();
    private boolean rtsMode = false;
    private MoveOrder pendingOrder = null;

    private RTSPlayer(UUID id) { this.playerId = id; }

    public static RTSPlayer get(ServerPlayer player) {
        return PLAYERS.computeIfAbsent(player.getUUID(), RTSPlayer::new);
    }

    public static void remove(UUID id) { PLAYERS.remove(id); }

    // ── Selection ──────────────────────────────────────────────────────────

    public void selectUnit(int entityId) { selectedEntityIds.add(entityId); }

    public void deselectAll() { selectedEntityIds.clear(); }

    public Set<Integer> getSelected() { return Collections.unmodifiableSet(selectedEntityIds); }

    public boolean isSelected(Entity e) { return selectedEntityIds.contains(e.getId()); }

    // ── Mode ───────────────────────────────────────────────────────────────

    public boolean isRTSMode() { return rtsMode; }

    public void setRTSMode(boolean on) { rtsMode = on; }

    public void toggleRTSMode() { rtsMode = !rtsMode; }

    // ── Orders ─────────────────────────────────────────────────────────────

    public void setMoveOrder(MoveOrder order) { this.pendingOrder = order; }

    public MoveOrder getPendingOrder() { return pendingOrder; }

    public void clearOrder() { pendingOrder = null; }
}
