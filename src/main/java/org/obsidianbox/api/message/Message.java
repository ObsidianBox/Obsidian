/**
 * This file is part of Obsidian, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013-2014 ObsidianBox <http://obsidianbox.org/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.obsidianbox.api.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import org.obsidianbox.api.Game;

/**
 * Represents a simple message that can be sent over the pipeline
 *
 * Note: All child classes MUST have one empty constructor!
 */
public interface Message {
    /**
     * Decodes this message from the {@link io.netty.buffer.ByteBuf}
     *
     * @param game The {@link org.obsidianbox.api.Game} game object
     * @param buf Raw data to be decoded
     */
    public void decode(Game game, ByteBuf buf) throws Exception;

    /**
     * Encodes this message into the {@link io.netty.buffer.ByteBuf}
     *
     * @param game The {@link org.obsidianbox.api.Game} game object
     * @param buf Where to encode the data into
     */
    public void encode(Game game, ByteBuf buf) throws Exception;

    /**
     * Called after decode is successful
     *
     * {@link #decode(org.obsidianbox.api.Game, io.netty.buffer.ByteBuf)}
     *
     * @param game The {@link org.obsidianbox.api.Game} game object
     * @param player The target {@link net.minecraft.entity.player.EntityPlayer} of this message
     */
    public void handle(Game game, EntityPlayer player);
}
