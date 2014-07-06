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
package org.obsidianbox.obsidian.message;

import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gnu.trove.map.hash.TByteObjectHashMap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import org.obsidianbox.magma.message.Message;
import org.obsidianbox.magma.message.MessageHandler;
import org.obsidianbox.magma.message.MessagePipeline;
import org.obsidianbox.magma.Game;
import org.obsidianbox.magma.addon.Addon;

@ChannelHandler.Sharable
public class CommonMessagePipeline extends FMLIndexedMessageToMessageCodec<Message> implements MessagePipeline {
    private static Field discriminatorsField;
    private final Game game;
    private final EnumMap<Side, FMLEmbeddedChannel> channels = Maps.newEnumMap(Side.class);
    private final Map<Class<? extends Message>, Class<? extends MessageHandler<? extends Message>>> handlers = new HashMap<>();
    private transient boolean locked = false;

    static {
        try {
            discriminatorsField = FMLIndexedMessageToMessageCodec.class.getDeclaredField("discriminators");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public CommonMessagePipeline(Game game) {
        this.game = game;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, Message message, ByteBuf target) throws Exception {
        // Allow addon API to write data
        message.encode(game, target);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, Message message) {
        // Allow the addon API to decode it into the message's fields
        try {
            message.decode(game, source);
        } catch (Exception e) {
            game.getLogger().fatal("Exception caught decoding message!", e);
            return;
        }
        final Class<? extends MessageHandler> handlerClazz = handlers.get(message.getClass());
        if (handlerClazz == null) {
            return;
        }
        MessageHandler<Message> handler;
        try {
            handler = handlerClazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            game.getLogger().error("Failed to handle message [" + message + "], does class [" + handlerClazz.getSimpleName() + "] have an empty constructor?");
            return;
        }
        if (handler == null) {
            return;
        }
        Message toSendBack;
        // Handle the message
        switch (game.getSide()) {
            case CLIENT:
                toSendBack = handler.handle(game, getClientPlayer(), message);
                if (toSendBack != null) {
                    sendToServer(toSendBack);
                }
                break;
            case SERVER:
                INetHandler net = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
                toSendBack = handler.handle(game, ((NetHandlerPlayServer) net).playerEntity, message);
                if (toSendBack != null) {
                    sendTo(toSendBack, ((NetHandlerPlayServer) net).playerEntity);
                }
                break;
            default:
        }
    }

    @SideOnly (Side.CLIENT)
    private EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        game.getLogger().fatal("Exception caught in pipeline!", cause);
        ctx.fireExceptionCaught(cause);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Message, U extends MessageHandler<T>> void register(Addon addon, Class<T> message, Class<U> handler) {
        if (locked) {
            throw new IllegalStateException(addon.getDescription().getName() + " attempted to register a message after INITIALIZE phase! This is NOT ALLOWED.");
        }
        final TByteObjectHashMap<Class<? extends Message>> discriminators;
        try {
            discriminatorsField.setAccessible(true);
            discriminators = (TByteObjectHashMap<Class<? extends Message>>) discriminatorsField.get(this);
            discriminatorsField.setAccessible(false);
        } catch (IllegalAccessException e) {
            game.getLogger().info("Encountered fatal exception when " + addon.getDescription().getName() + " attempted to register [" + message.getSimpleName() + "]", e);
            return;
        }

        if (discriminators.containsValue(message)) {
            game.getLogger().warn(addon.getDescription().getName() + " attempted to register [" + message + "] twice!");
            return;
        }

        addDiscriminator(discriminators.size() == 0 ? 0 : discriminators.size() + 1, message);

        if (handler != null) {
            handlers.put(message, handler);
            game.getLogger().info(addon.getDescription().getName() + " has registered message [" + message.getSimpleName() + "] with handler [" + handler.getSimpleName() + "] in the pipeline");
        } else {
            game.getLogger().info(addon.getDescription().getName() + " has registered message [" + message.getSimpleName() + "] with no handler in the pipeline");
        }
    }

    @Override
    public void sendToAll(Message message) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    @Override
    public void sendTo(Message message, EntityPlayer player) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    @Override
    public void sendToAllAround(Message message, NetworkRegistry.TargetPoint point) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    @Override
    public void sendToDimension(Message message, int dimensionId) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    @Override
    public void sendToServer(Message message) {
        this.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        this.channels.get(Side.CLIENT).writeAndFlush(message);
    }

    public void lockPipeline() {
        channels.putAll(NetworkRegistry.INSTANCE.newChannel(Game.MOD_ID.toUpperCase(), this));
        locked = true;
    }
}
