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

import java.io.Serializable;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import org.obsidianbox.magma.Game;
import org.obsidianbox.magma.addon.Addon;
import org.obsidianbox.magma.message.Message;
import org.obsidianbox.magma.message.MessageHandler;
import org.obsidianbox.obsidian.addon.CommonAddonManager;
import org.obsidianbox.obsidian.resource.CommonFileSystem;
import org.obsidianbox.obsidian.util.map.SerializableHashMap;

/**
 * Communicates the addon list between client and server.
 */
public class AddonListMessage implements Message, MessageHandler<AddonListMessage> {
    private SerializableHashMap map;

    public AddonListMessage() {
    }

    public AddonListMessage(SerializableHashMap map) {
        this.map = map;
    }

    @Override
    public void decode(Game game, ByteBuf buf) throws Exception {
        if (game.getSide().isServer()) {
            map = new SerializableHashMap();
            final int length = buf.readInt();
            final byte[] data = new byte[length];
            buf.readBytes(data);
            map.deserialize(data, true);
        }
    }

    @Override
    public void encode(Game game, ByteBuf buf) throws Exception {
        if (game.getSide().isClient()) {
            final byte[] serialized = map.serialize();
            buf.writeInt(serialized.length);
            buf.writeBytes(serialized);
        }
    }

    @Override
    public Message handle(Game game, EntityPlayer player, AddonListMessage message) {
        switch (game.getSide()) {
            case CLIENT:
                game.getLogger().info("Server has requested my addon list, sending...");
                final SerializableHashMap addonMD5s = ((CommonAddonManager) game.getAddonManager()).getAddonMD5s();
                game.getLogger().info(addonMD5s);
                return new AddonListMessage(addonMD5s);
            case SERVER:
                game.getLogger().info("Received a list of addons for player " + player.getDisplayName());
                game.getLogger().info("Checking if " + player.getDisplayName() + " needs addon installations");

                final CommonAddonManager manager = ((CommonAddonManager) game.getAddonManager());
                final SerializableHashMap serverMap = manager.getAddonMD5s();

                // We'll handle what the client has told us about
                for (Map.Entry<String, Serializable> entry : map.entrySet()) {
                    final String addonIdentifier = entry.getKey();
                    final String clientMD5 = (String) entry.getValue();

                    final String serverMD5 = serverMap.get(addonIdentifier, String.class);

                    // Server does not have this addon
                    if (serverMD5 == null) {
                        game.getLogger().info(player.getDisplayName() + " has addon [" + addonIdentifier + "] installed but the server does not. Checking if the admin has " +
                                "allowed this in the greylist...");
                        ((EntityPlayerMP) player).playerNetServerHandler.kickPlayerFromServer("You have addon " + addonIdentifier + " installed and this server does not allow it!");

                        // TODO Check the greylist, kick this player if that addon is not on the list of accepted ones
                        game.getLogger().info("Admin has allowed addon [" + addonIdentifier + "] for " + player.getDisplayName() + ". Continuing on...");
                    } else {
                        // Server has the addon but does the client have our specific one?
                        if (!serverMD5.equals(clientMD5)) {
                            game.getLogger().info(player.getDisplayName() + " has addon [" + addonIdentifier + "] installed and so does the server but the MD5' mismatch.\n" +
                                    "Server: " + serverMD5 + "\n" +
                                    "Client: " + clientMD5 + "\n" +
                                    "Sending the server's addon to the client...");

                            // TODO This does NOT GO INTO A RELEASE BUILD WITHOUT CLIENT CONFIG CHECK!!!
                            final Addon addon = manager.getAddon(addonIdentifier);
                            game.getPipeline().sendTo(new AddFileMessage(addonIdentifier, addonIdentifier + ".jar", ((CommonFileSystem) game.getFileSystem()).getAddonDataFile(addon).toPath()), player);
                        } else {
                            game.getLogger().info(player.getDisplayName() + " has addon [" + addonIdentifier + "]. Server verifies this is okay.");
                        }
                    }
                }
                break;
        }
        return null;
    }
}
