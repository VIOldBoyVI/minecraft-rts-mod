package com.rtsmod.rts.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.rtsmod.rts.core.RTSPlayer;
import com.rtsmod.rts.units.UnitManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * /rts — root command for all RTS actions.
 *
 * Usage:
 *   /rts mode          — toggle RTS mode on/off
 *   /rts select <id>   — add entity to selection
 *   /rts deselect      — clear selection
 *   /rts list          — list selected entity IDs
 *   /rts move <x y z>  — move selected units to block position
 *   /rts attack <x y z>— attack-move to position
 *   /rts stop          — stop all selected units
 */
public class RTSCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("rts")
            .requires(src -> src.hasPermission(0))

            // /rts mode
            .then(Commands.literal("mode").executes(ctx -> {
                ServerPlayer player = ctx.getSource().getPlayerOrException();
                RTSPlayer rts = RTSPlayer.get(player);
                rts.toggleRTSMode();
                String state = rts.isRTSMode() ? "§aON" : "§cOFF";
                player.sendSystemMessage(Component.literal("§6RTS Mode: " + state));
                return 1;
            }))

            // /rts select <entityId>
            .then(Commands.literal("select")
                .then(Commands.argument("entityId", IntegerArgumentType.integer(1))
                    .executes(ctx -> {
                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                        int id = IntegerArgumentType.getInteger(ctx, "entityId");
                        RTSPlayer.get(player).selectUnit(id);
                        player.sendSystemMessage(Component.literal("§7Selected entity #" + id));
                        return 1;
                    })))

            // /rts deselect
            .then(Commands.literal("deselect").executes(ctx -> {
                ServerPlayer player = ctx.getSource().getPlayerOrException();
                RTSPlayer.get(player).deselectAll();
                player.sendSystemMessage(Component.literal("§7Selection cleared."));
                return 1;
            }))

            // /rts list
            .then(Commands.literal("list").executes(ctx -> {
                ServerPlayer player = ctx.getSource().getPlayerOrException();
                var ids = RTSPlayer.get(player).getSelected();
                if (ids.isEmpty()) {
                    player.sendSystemMessage(Component.literal("§7No units selected."));
                } else {
                    player.sendSystemMessage(Component.literal("§6Selected units: §e" + ids));
                }
                return 1;
            }))

            // /rts move <x y z>
            .then(Commands.literal("move")
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                    .executes(ctx -> {
                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                        BlockPos pos = BlockPosArgument.getLoadedBlockPos(ctx, "pos");
                        UnitManager.issueMoveOrder(player, pos);
                        return 1;
                    })))

            // /rts attack <x y z>
            .then(Commands.literal("attack")
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                    .executes(ctx -> {
                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                        BlockPos pos = BlockPosArgument.getLoadedBlockPos(ctx, "pos");
                        UnitManager.issueAttackMove(player, pos);
                        return 1;
                    })))

            // /rts stop
            .then(Commands.literal("stop").executes(ctx -> {
                ServerPlayer player = ctx.getSource().getPlayerOrException();
                UnitManager.issueStop(player);
                return 1;
            }))
        );
    }
}
