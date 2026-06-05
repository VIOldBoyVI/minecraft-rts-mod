package com.rtsmod.rts.core;

import net.minecraft.core.BlockPos;

public record MoveOrder(BlockPos target, OrderType type) {
    public enum OrderType { MOVE, ATTACK_MOVE, PATROL }
}
