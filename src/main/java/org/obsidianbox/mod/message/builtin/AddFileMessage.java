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
package org.obsidianbox.obsidian.message.builtin;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.SerializationUtils;
import org.obsidianbox.magma.addon.InvalidAddonException;
import org.obsidianbox.magma.addon.InvalidDescriptionException;
import org.obsidianbox.magma.message.Message;
import org.obsidianbox.obsidian.resource.CommonFileSystem;
import org.obsidianbox.magma.Game;

public class AddFileMessage implements Message {
    private String addonIdentifier;
    private String name;
    private byte[] data;

    private AddFileMessage() {}

    @SideOnly (Side.SERVER)
    public AddFileMessage(String addonIdentifier, String name, Path path) {
        this.addonIdentifier = addonIdentifier;
        this.name = name;
        data = SerializationUtils.serialize(path.toFile());
    }

    @Override
    public void decode(Game game, ByteBuf buf) throws Exception {
        if (game.getSide().isServer()) {
            throw new IOException("Server is not allowed to receive files!");
        }
        addonIdentifier = ByteBufUtils.readUTF8String(buf);
        name = ByteBufUtils.readUTF8String(buf);
        data = new byte[buf.readableBytes()];
        buf.readBytes(data);
    }

    @Override
    public void encode(Game game, ByteBuf buf) throws Exception {
        if (game.getSide().isClient()) {
            throw new IOException("Client is not allowed to send files!");
        }
        ByteBufUtils.writeUTF8String(buf, addonIdentifier);
        ByteBufUtils.writeUTF8String(buf, name);
        buf.writeBytes(data);
    }

    @Override
    public void handle(Game game, EntityPlayer player) {
        // Re-create the file
        final Path addonJarPath = Paths.get(CommonFileSystem.ADDONS_PATH.toString(), addonIdentifier);
        try {
            Files.write(addonJarPath, data, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            game.getAddonManager().loadAddon(addonJarPath);
        } catch (InvalidAddonException | InvalidDescriptionException e) {
            e.printStackTrace();
        }
        Minecraft.getMinecraft().scheduleResourcesRefresh();
    }
}
