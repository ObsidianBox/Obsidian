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
package org.obsidianbox.obsidian;

import java.nio.ByteBuffer;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.Display;
import org.obsidianbox.magma.addon.AddonManager;
import org.obsidianbox.magma.block.renderer.BlockRenderer;
import org.obsidianbox.magma.block.renderer.SimpleOBJRenderer;
import org.obsidianbox.magma.resource.FileSystem;
import org.obsidianbox.magma.util.RenderUtil;
import org.obsidianbox.obsidian.lang.CommonLanguageRegistry;
import org.obsidianbox.obsidian.message.CommonMessagePipeline;
import org.obsidianbox.obsidian.renderer.GuiRenderer;
import org.obsidianbox.obsidian.resource.ClientFileSystem;
import org.obsidianbox.obsidian.resource.CommonFileSystem;
import org.obsidianbox.magma.Game;
import org.obsidianbox.obsidian.addon.ClientAddonManager;
import org.obsidianbox.obsidian.addon.CommonAddonManager;

import org.spout.renderer.lwjgl.gl32.GL32Context;

@Mod (modid = Game.MOD_ID)
public class ObsidianMod {
    private static CustomTabs customTabs;
    private final Game game;

    public ObsidianMod() {
        game = new Game(FMLCommonHandler.instance().getEffectiveSide());
    }

    public static CustomTabs getCustomTabs() {
        return customTabs;
    }

    /*
     * Setup game
     */
    @EventHandler
    public void onInitialize(FMLInitializationEvent event) {
        final AddonManager addonManager;
        final FileSystem fileSystem;
        switch (game.getSide()) {
            case CLIENT:
                addonManager = new ClientAddonManager(game);
                fileSystem = new ClientFileSystem();
                break;
            case SERVER:
                addonManager = new CommonAddonManager(game);
                fileSystem = new CommonFileSystem();
                break;
            default:
                return;
        }
        final CommonLanguageRegistry languages = new CommonLanguageRegistry(game);
        final CommonMessagePipeline messagePipeline = new CommonMessagePipeline(game);
        game.setAddonManager(addonManager);
        game.setFileSystem(fileSystem);
        game.setLanguages(languages);
        game.setPipeline(messagePipeline);

        // Setup creative tab
        customTabs = new CustomTabs(game);
        game.setTabs(customTabs);

        // Setup file system
        try {
            ((CommonFileSystem) fileSystem).setupFileSystem();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize FileSystem!", e);
        }

        if (game.getSide().isClient()) {
            // Set the title
            Display.setTitle(Game.MOD_ID);

            // Set the icons
            final ByteBuffer windowIcon = RenderUtil.createImageBufferFrom(new ResourceLocation(Game.MOD_ID.toLowerCase(), "textures/window_icon.png"), true);
            final ByteBuffer taskbarIcon = RenderUtil.createImageBufferFrom(new ResourceLocation(Game.MOD_ID.toLowerCase(), "textures/taskbar_icon.png"), true);
            if (windowIcon != null && taskbarIcon != null) {
                Display.setIcon(new ByteBuffer[] {windowIcon, taskbarIcon});
            }
            BlockRenderer renderer = new SimpleOBJRenderer();
            RenderingRegistry.registerBlockHandler(renderer);
            game.setBlockRenderer(renderer);
        } else {
            // Load addons
            addonManager.loadAddons(CommonFileSystem.ADDONS_PATH);

            // Initialize addons
            ((CommonAddonManager) addonManager).initialize();

            // Lock filesystem
            ((CommonFileSystem) fileSystem).lockFileSystem();

            // Lock message pipeline
            messagePipeline.lockPipeline();
        }

        // Setup Gui Renderer
        if (game.getSide().isClient()) {
            setupRenderer();
        }

        // Register internal events
        final GameListener listener = new GameListener(game);
        MinecraftForge.EVENT_BUS.register(listener);
        FMLCommonHandler.instance().bus().register(listener);
        FMLCommonHandler.instance().bus().register(this);
    }

    /*
     * Enable addons in MULTIPLAYER
     */
    @EventHandler
    public void onPostInitialize(FMLPostInitializationEvent event) {
        if (game.getSide().isServer()) {
            ((CommonAddonManager) game.getAddonManager()).enable();
        }
    }

    /*
     * Load/Enable addons in SINGLEPLAYER
     */
    @SubscribeEvent
    public void onClientConnectedToServerEvent(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (event.isLocal) {
            final CommonAddonManager addonManager = (CommonAddonManager) game.getAddonManager();
            final CommonFileSystem fileSystem = (CommonFileSystem) game.getFileSystem();
            final CommonMessagePipeline messagePipeline = (CommonMessagePipeline) game.getPipeline();

            // Load addons
            addonManager.loadAddons(CommonFileSystem.ADDONS_PATH);

            // Initialize addons
            addonManager.initialize();

            // Register our language map into Forge
            ((CommonLanguageRegistry) game.getLanguages()).register();

            // Refresh for addon additions in initialize
            Minecraft.getMinecraft().refreshResources();

            // Lock filesystem
            fileSystem.lockFileSystem();

            // Lock message pipeline
            messagePipeline.lockPipeline();

            // Enable addons
            addonManager.enable();
        }
    }

    @SideOnly(Side.CLIENT)
    private void setupRenderer() {
        final GuiRenderer guiRenderer = new GuiRenderer(game);
        game.setGuiRenderer(guiRenderer);
        try {
            guiRenderer.initialize(new GL32Context());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
