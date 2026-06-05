package com.rtsmod.rts.units;

import com.rtsmod.rts.core.MoveOrder;
import com.rtsmod.rts.core.RTSPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles issuing movement and attack orders to selected mobs.
 */
public class UnitManager {

    /**
     * Issue a move order to all selected entities owned by this player.
     */
    public static void issueMoveOrder(ServerPlayer player, BlockPos target) {
        RTSPlayer rts = RTSPlayer.get(player);
        List<Mob> units = getSelectedMobs(player, rts);

        if (units.isEmpty()) {
            sendFeedback(player, "§cNo units selected. Use /rts select <entityId> first.");
            return;
        }

        // Spread units around target in a simple grid
        int i = 0;
        int spread = (int) Math.ceil(Math.sqrt(units.size()));
        for (Mob mob : units) {
            int dx = (i % spread) - spread / 2;
            int dz = (i / spread) - spread / 2;
            BlockPos dest = target.offset(dx * 2, 0, dz * 2);
            navigateTo(mob, dest);
            i++;
        }

        rts.setMoveOrder(new MoveOrder(target, MoveOrder.OrderType.MOVE));
        sendFeedback(player, "§aMoved §e" + units.size() + "§a unit(s) to " + fmtPos(target));
    }

    /**
     * Issue an attack-move order: units move to target and attack enemies on the way.
     */
    public static void issueAttackMove(ServerPlayer player, BlockPos target) {
        RTSPlayer rts = RTSPlayer.get(player);
        List<Mob> units = getSelectedMobs(player, rts);

        if (units.isEmpty()) {
            sendFeedback(player, "§cNo units selected.");
            return;
        }

        for (Mob mob : units) {
            navigateTo(mob, target);
            // Aggressive goal is already wired in vanilla mobs; just move them there
        }

        rts.setMoveOrder(new MoveOrder(target, MoveOrder.OrderType.ATTACK_MOVE));
        sendFeedback(player, "§cAttack-move §e" + units.size() + "§c unit(s) to " + fmtPos(target));
    }

    /**
     * Stop all selected units in place.
     */
    public static void issueStop(ServerPlayer player) {
        RTSPlayer rts = RTSPlayer.get(player);
        List<Mob> units = getSelectedMobs(player, rts);

        for (Mob mob : units) {
            mob.getNavigation().stop();
        }

        rts.clearOrder();
        sendFeedback(player, "§7Stopped §e" + units.size() + "§7 unit(s).");
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    private static List<Mob> getSelectedMobs(ServerPlayer player, RTSPlayer rts) {
        ServerLevel level = player.serverLevel();
        Set<Integer> ids = rts.getSelected();
        List<Mob> result = new ArrayList<>();
        for (int id : ids) {
            Entity e = level.getEntity(id);
            if (e instanceof Mob mob && mob.isAlive()) result.add(mob);
        }
        return result;
    }

    private static void navigateTo(Mob mob, BlockPos pos) {
        PathNavigation nav = mob.getNavigation();
        nav.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 1.0);
    }

    private static void sendFeedback(ServerPlayer player, String msg) {
        player.sendSystemMessage(net.minecraft.network.chat.Component.literal(msg));
    }

    private static String fmtPos(BlockPos p) {
        return "(" + p.getX() + ", " + p.getY() + ", " + p.getZ() + ")";
    }
}
