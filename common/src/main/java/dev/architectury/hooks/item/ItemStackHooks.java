/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021, 2022 architectury
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package dev.architectury.hooks.item;

import dev.architectury.transfer.TransferAction;
import dev.architectury.transfer.item.ItemTransfer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

import static dev.architectury.utils.Amount.toInt;

public final class ItemStackHooks {
    private ItemStackHooks() {
    }
    
    public static ItemStack copyWithCount(ItemStack stack, int count) {
        var copy = stack.copy();
        copy.setCount(count);
        return copy;
    }
    
    public static ItemStack copyWithCount(ItemStack stack, long count) {
        return copyWithCount(stack, toInt(count));
    }
    
    public static void giveItem(ServerPlayer player, ItemStack stack) {
        var inserted = ItemTransfer.container(player.getInventory(), null).insert(stack, TransferAction.ACT);
        var remaining = stack.getCount() - inserted;
        if (remaining > 0) {
            var entity = player.drop(stack, false);
            if (entity != null) {
                entity.setNoPickUpDelay();
                entity.setOwner(player.getUUID());
            }
        } else {
            stack = stack.copy();
            stack.setCount(1);
            var entity = player.drop(stack, false);
            if (entity != null) {
                entity.makeFakeItem();
            }
            
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.inventoryMenu.broadcastChanges();
        }
    }
    
    public static boolean isStackable(ItemStack first, ItemStack second) {
        // TODO check caps?
        return ItemStack.matches(first, second);
    }
}