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
package org.obsidianbox.obsidian.renderer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.*;
import org.spout.renderer.api.Camera;
import org.spout.renderer.api.Material;
import org.spout.renderer.api.Pipeline;
import org.spout.renderer.api.data.UniformHolder;
import org.spout.renderer.api.data.VertexData;
import org.spout.renderer.api.gl.Context;
import org.spout.renderer.api.gl.Program;
import org.spout.renderer.api.gl.Texture;
import org.spout.renderer.api.gl.Texture.FilterMode;
import org.spout.renderer.api.gl.Texture.Format;
import org.spout.renderer.api.gl.Texture.InternalFormat;
import org.spout.renderer.api.gl.VertexArray;
import org.spout.renderer.api.model.Model;
import org.spout.renderer.api.util.CausticUtil;
import org.spout.renderer.api.util.MeshGenerator;
import org.spout.renderer.api.util.Rectangle;
import org.spout.renderer.lwjgl.LWJGLUtil;

import com.flowpowered.math.vector.Vector2f;
import com.flowpowered.math.vector.Vector3f;

import org.obsidianbox.magma.Game;
import org.obsidianbox.magma.addon.Addon;
import org.obsidianbox.magma.renderer.Renderer;
import org.obsidianbox.obsidian.addon.CommonAddonManager;

/**
 * Renders the Addon GUI API using the Caustic rendering library.
 */
@SideOnly(Side.CLIENT)
public class GuiRenderer extends Renderer {
    // Caustic GUI properties
    private final LinkedList<Model> guiRenderList = new LinkedList<>();
    private final Rectangle rectangle = new Rectangle();
    private Camera camera;
    private Pipeline pipeline;

    public GuiRenderer(Game game) {
        super(game);
    }

    @Override
    public void initialize(Context context) {
        super.initialize(context);
        updateScaledAttributes();

        camera = Camera.createOrthographic(1, 0, 1 / aspectRatio, 0, 0.1f, 1000);
        context.setCamera(camera);

        pipeline = new Pipeline.PipelineBuilder().renderModels(guiRenderList).build();
    }

    @Override
    public void render() {
        // Snapshot Minecraft rendering
        final int mcProgId = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
        final int mcVAID = GL11.glGetInteger(GL11.GL_VERTEX_ARRAY);

        updateScaledAttributes();

        LWJGLUtil.checkForGLError();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        pipeline.run(context);
        GL11.glDisable(GL11.GL_BLEND);
        LWJGLUtil.checkForGLError();

        GL20.glUseProgram(mcProgId);
        GL30.glBindVertexArray(mcVAID);
        LWJGLUtil.checkForGLError();
    }

    @Override
    public void dispose() {

    }

    @Override
    public GuiRenderer addModel(Addon addon, Model model) {
        if (addon == null) {
            throw new IllegalStateException("Cannot add a model with a null addon instance");
        }
        if (model == null) {
            throw new IllegalStateException(addon.getDescription().getName() + " attempted to add a model with a null instance!");
        }
        guiRenderList.add(model);
        return this;
    }

    @Override
    public Texture createTexture(Addon addon, String name, Format format, FilterMode min, FilterMode mag, InternalFormat internalFormat, float anisotropicFiltering) throws IOException {
        if (name == null || name.isEmpty()) {
            throw new IOException(addon.getDescription().getName() + " attempted to create texture with a null or empty name!");
        }

        // Create addon texture store or return loaded texture
        Map<String, Texture> textures = ADDON_TEXTURES.get(addon);
        if (textures == null) {
            textures = new HashMap<>();
            ADDON_TEXTURES.put(addon, textures);
        } else {
            final Texture texture = textures.get(name);
            if (texture != null) {
                return texture;
            }
        }

        final InputStream textureStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Game.MOD_ID.toLowerCase(), "textures/gui/" + addon.getDescription().getIdentifier() + "/" + name + ".png")).getInputStream();

        final Texture texture = context.newTexture();
        texture.create();
        texture.setFilters(min, mag);
        texture.setFormat(internalFormat);
        if (anisotropicFiltering > 0f) {
            texture.setAnisotropicFiltering(anisotropicFiltering);
        }
        final ByteBuffer data = CausticUtil.getImageData(textureStream, format, rectangle);
        texture.setImageData(data, rectangle.getWidth(), rectangle.getHeight());
        textures.put(name, texture);

        return texture;
    }

    @Override
    public Model createTexturedPlane(Addon addon, String programName, UniformHolder holder, Texture diffuse, Texture normal, Texture specular, Vector2f size) throws IOException {
        // We ignore normal and specular for the GUI
        if (diffuse == null) {
            throw new IOException(addon.getDescription().getName() + " attempted to texture a plane with a null diffuse!");
        }

        // Generate the plane
        final VertexData data = MeshGenerator.generatePlane(size);
        final VertexArray array = context.newVertexArray();
        array.create();
        array.setData(data);

        // Grab the textured program
        final Program program = loadProgram(((CommonAddonManager) game.getAddonManager()).getInternalAddon(), programName);

        // Create a material for it
        final Material material = new Material(program);
        if (holder != null) {
            material.getUniforms().addAll(holder);
        }
        material.addTexture(0, diffuse);

        final Model model = new Model(array, material);
        final float aspect = diffuse.getWidth() / (float) diffuse.getHeight();
        model.setScale(new Vector3f(aspect, 1, 1));
        return model;
    }

    @Override
    public Camera getCamera() {
        return camera;
    }
}