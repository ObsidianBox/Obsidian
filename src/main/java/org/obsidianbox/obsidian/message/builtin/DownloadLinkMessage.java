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
import java.net.MalformedURLException;
import java.net.URL;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.SerializationUtils;

import org.obsidianbox.magma.Game;
import org.obsidianbox.magma.message.Message;

public class DownloadLinkMessage implements Message {
    private String addonIdentifier;
    private URL url;

    private DownloadLinkMessage() {
    }

    public DownloadLinkMessage(String addonIdentifier, String url) throws MalformedURLException {
        this(addonIdentifier, new URL(url));
    }

    public DownloadLinkMessage(String addonIdentifier, URL url) {
        this.addonIdentifier = addonIdentifier;
        this.url = url;
    }

    @Override
    public void decode(Game game, ByteBuf buf) throws Exception {
        if (game.getSide().isServer()) {
            throw new IOException("Server is not allowed to receive download links!");
        }
        addonIdentifier = ByteBufUtils.readUTF8String(buf);
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        url = SerializationUtils.deserialize(data);
    }

    @Override
    public void encode(Game game, ByteBuf buf) throws Exception {
        if (game.getSide().isClient()) {
            throw new IOException("Client is not allowed to send download links!");
        }
        final byte[] data = SerializationUtils.serialize(url);
        ByteBufUtils.writeUTF8String(buf, addonIdentifier);
        buf.writeBytes(data);
    }
}
