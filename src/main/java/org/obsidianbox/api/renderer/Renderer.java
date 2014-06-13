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
package org.obsidianbox.api.renderer;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.flowpowered.math.vector.Vector2f;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.obsidianbox.api.Game;
import org.obsidianbox.api.addon.Addon;

import org.spout.renderer.api.Camera;
import org.spout.renderer.api.Creatable;
import org.spout.renderer.api.Material;
import org.spout.renderer.api.data.ShaderSource;
import org.spout.renderer.api.data.Uniform;
import org.spout.renderer.api.data.UniformHolder;
import org.spout.renderer.api.data.VertexData;
import org.spout.renderer.api.gl.Context;
import org.spout.renderer.api.gl.Program;
import org.spout.renderer.api.gl.Shader;
import org.spout.renderer.api.gl.Texture;
import org.spout.renderer.api.gl.Texture.FilterMode;
import org.spout.renderer.api.gl.Texture.Format;
import org.spout.renderer.api.gl.Texture.InternalFormat;
import org.spout.renderer.api.gl.VertexArray;
import org.spout.renderer.api.model.Model;
import org.spout.renderer.api.model.StringModel;
import org.spout.renderer.api.model.StringModel.AntiAliasing;
import org.spout.renderer.api.util.CausticUtil;
import org.spout.renderer.api.util.MeshGenerator;

/**
 * Handles creation of gl objects and rendering them via Caustic
 */
@SideOnly (Side.CLIENT)
public abstract class Renderer {
    protected final Game game;
    // Caustic properties
    protected final Map<Addon, Map<String, Program>> ADDON_PROGRAMS = new HashMap<>();
    protected final Map<Addon, Map<String, Texture>> ADDON_TEXTURES = new HashMap<>();
    protected final Map<Addon, Map<String, Font>> ADDON_FONTS = new HashMap<>();
    // Display properties
    protected final Vector2f displaySize = new Vector2f(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
    protected Context context;
    protected float aspectRatio;
    // Minecraft properties
    protected ScaledResolution scaledResolution;
    protected int scaledAspectRatio = 0;
    protected int scaledFactor = 0;
    protected ScaledResolution previousScaledResolution;
    protected int previousScaledAspectRatio = 0;
    protected int previousScaledFactor = 0;

    public Renderer(Game game) {
        this.game = game;
    }

    public static void setDebugMode(boolean debug) {
        CausticUtil.setDebugEnabled(debug);
    }

    public void initialize(Context context) {
        this.context = context;
        aspectRatio = displaySize.getX() / displaySize.getY();

        try {
            final Field createdfield = Creatable.class.getDeclaredField("created");
            createdfield.setAccessible(true);
            createdfield.setBoolean(context, true);
            createdfield.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void render();

    public abstract void dispose();

    public abstract Renderer addModel(Addon addon, Model model);

    public Font getFont(Addon addon, String fontName) {
        Map<String, Font> fonts = ADDON_FONTS.get(addon);
        if (fonts != null) {
            return fonts.get(fontName);
        }
        return null;
    }

    public StringModel createString(Addon addon, String programName, String initialText, String fontName, AntiAliasing aliasing, int style, int fontSize) throws IOException, FontFormatException {
        final StringModel model = new StringModel(context, loadProgram(addon, programName), initialText, loadFont(addon, fontName).deriveFont(style, fontSize), aliasing, (int) displaySize.getX());
        model.setString(initialText);
        return model;
    }

    public Program loadProgram(Addon addon, String name) throws IOException {
        Map<String, Program> programs = ADDON_PROGRAMS.get(addon);
        if (programs != null) {
            final Program program = programs.get(name);
            if (program != null) {
                return program;
            }
        }

        final String shaderPath = "shaders/" + addon.getDescription().getIdentifier() + "/glsl" + (context.getGLVersion().getGLSLFull() >= 150 ? 330 : 120) + "/" + name;

        final InputStream verStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Game.MOD_ID.toLowerCase(), shaderPath + ".vert")).getInputStream();
        final InputStream fragStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Game.MOD_ID.toLowerCase(), shaderPath + ".frag")).getInputStream();

        // SHADERS
        final Shader vert = context.newShader();
        vert.create();
        vert.setSource(new ShaderSource(verStream));
        vert.compile();

        final Shader frag = context.newShader();
        frag.create();
        frag.setSource(new ShaderSource(fragStream));
        frag.compile();

        // PROGRAM
        final Program program = context.newProgram();
        program.create();
        program.attachShader(vert);
        program.attachShader(frag);
        program.link();

        if (programs == null) {
            programs = new HashMap<>();
            ADDON_PROGRAMS.put(addon, programs);
        }
        programs.put(name, program);
        return program;
    }

    public Font loadFont(Addon addon, String fontName) throws IOException, FontFormatException {
        Map<String, Font> fonts = ADDON_FONTS.get(addon);
        if (fonts == null) {
            fonts = new HashMap<>();
            ADDON_FONTS.put(addon, fonts);
        }

        Font font = fonts.get(fontName);
        if (font == null) {
            final InputStream fontStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Game.MOD_ID.toLowerCase(), "fonts/" + addon.getDescription().getIdentifier() + "/" + fontName + ".ttf")).getInputStream();
            font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
        }
        return font;
    }

    public StringModel createString(Addon addon, String programName, String initialText, Font font, AntiAliasing aliasing, int style, int fontSize) throws IOException {
        final StringModel model = new StringModel(context, loadProgram(addon, programName), initialText, font.deriveFont(style, fontSize), aliasing, (int) displaySize.getX());
        model.setString(initialText);
        return model;
    }

    public abstract Texture createTexture(Addon addon, String name, Format format, FilterMode min, FilterMode mag, InternalFormat internalFormat, float anisotropicFiltering) throws IOException;

    public Model createColoredPlane(Addon addon, String programName, UniformHolder holder, Color color, Vector2f size) throws IOException {
        holder.add(new Uniform.Vector4Uniform("inputColor", color.asFloatVector()));

        return createPlane(addon, programName, holder, size);
    }

    public Model createPlane(Addon addon, String programName, UniformHolder uniforms, Vector2f size) throws IOException {
        // Generate the plane
        final VertexData data = MeshGenerator.generatePlane(size);
        final VertexArray array = context.newVertexArray();
        array.create();
        array.setData(data);

        // Grab the program
        Program program = loadProgram(addon, programName);

        // Create a material for it
        final Material material = new Material(program);
        material.getUniforms().addAll(uniforms);

        return new Model(array, material);
    }

    public abstract Model createTexturedPlane(Addon addon, String programName, UniformHolder holder, Texture diffuse, Texture normal, Texture specular, Vector2f size) throws IOException;

    public abstract Camera getCamera();

    public Vector2f getDisplaySize() {
        return displaySize;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

    public ScaledResolution getPreviousScaledResolution() {
        return previousScaledResolution;
    }

    public int getScaledAspectRatio() {
        return scaledAspectRatio;
    }

    public int getPreviousScaledAspectRatio() {
        return previousScaledAspectRatio;
    }

    public int getScaledFactor() {
        return scaledFactor;
    }

    public int getPreviousScaledFactor() {
        return previousScaledFactor;
    }

    public void updateScaledAttributes() {
        previousScaledResolution = scaledResolution;
        previousScaledAspectRatio = scaledAspectRatio;
        previousScaledFactor = scaledFactor;

        scaledResolution = new ScaledResolution(Minecraft.getMinecraft().gameSettings, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        scaledAspectRatio = scaledResolution.getScaledWidth() / scaledResolution.getScaledHeight();
        scaledFactor = scaledResolution.getScaleFactor();
    }
}
