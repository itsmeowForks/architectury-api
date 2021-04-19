/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
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

package me.shedaniel.architectury.core.access.specific;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.function.Predicate;

@FunctionalInterface
public interface ChunkAccess<T> {
    default T getByChunk(Level level, BlockPos pos) {
        return getByChunk(level.getChunkAt(pos));
    }
    
    default T getByChunk(Level level, int chunkX, int chunkY) {
        return getByChunk(level.getChunk(chunkX, chunkY));
    }
    
    T getByChunk(LevelChunk chunk);
    
    default ChunkAccess<T> filterChunk(Predicate<LevelChunk> predicate) {
        return chunk -> {
            if (predicate.test(chunk)) {
                return this.getByChunk(chunk);
            }
            return null;
        };
    }
}
