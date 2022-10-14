package committee.nova.keywizard.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class FloatGui {
    public static final ResourceLocation OPTIONS_BACKGROUND = new ResourceLocation("textures/gui/options_background.png");
    public static final ResourceLocation STAT_ICONS = new ResourceLocation("textures/gui/container/stats_icons.png");
    public static final ResourceLocation ICONS = new ResourceLocation("textures/gui/icons.png");
    protected float zLevel;

    /**
     * Draws a thin horizontal line between two points.
     */
    protected void drawHorizontalLine(double startX, double endX, double y, int color) {
        if (endX < startX) {
            double i = startX;
            startX = endX;
            endX = i;
        }

        drawRect(startX, y, endX + 1, y + 1, color);
    }

    /**
     * Draw a 1 pixel wide vertical line. Args : x, y1, y2, color
     */
    protected void drawVerticalLine(double x, double startY, double endY, int color) {
        if (endY < startY) {
            double i = startY;
            startY = endY;
            endY = i;
        }

        drawRect(x, startY + 1, x + 1, endY, color);
    }


    protected void drawNoFillRect(double left, double top, double right, double bottom, int color) {
        drawHorizontalLine(left, right, top, color);
        drawHorizontalLine(left, right, bottom, color);
        drawVerticalLine(left, top, bottom, color);
        drawVerticalLine(right, top, bottom, color);
    }

    /**
     * Draws a solid color rectangle with the specified coordinates and color.
     *
     * @param color 0xAARRGGBB
     */
    public void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(f, f1, f2, f3);
        tessellator.startDrawingQuads();
        tessellator.addVertex(left, bottom, this.zLevel);
        tessellator.addVertex(right, bottom, this.zLevel);
        tessellator.addVertex(right, top, this.zLevel);
        tessellator.addVertex(left, top, this.zLevel);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    /**
     * Draws a rectangle with a vertical gradient between the specified colors (ARGB format). Args : x1, y1, x2, y2,
     * topColor, bottomColor
     */
    protected void drawGradientRect(double left, double top, double right, double bottom, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(7425);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(right, top, this.zLevel);
        tessellator.addVertex(left, top, this.zLevel);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(left, bottom, this.zLevel);
        tessellator.addVertex(right, bottom, this.zLevel);
        tessellator.draw();
        GL11.glShadeModel(7424);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Renders the specified text to the screen, center-aligned. Args : renderer, string, x, y, color
     */
    public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
        fontRendererIn.drawStringWithShadow(text, (x - fontRendererIn.getStringWidth(text) / 2), y, color);
    }

    //todo: it was float

    /**
     * Renders the specified text to the screen. Args : renderer, string, x, y, color
     */
    public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
        fontRendererIn.drawStringWithShadow(text, x, y, color);
    }

    /*
     * The methods below this point are currently unused
     */

    /**
     * Draws a textured rectangle at the current z-value.
     */
    public void drawTexturedModalRect(double x, double y, int textureX, int textureY, double width, double height) {
        //float f = 0.00390625F; Unused??
        //float f1 = 0.00390625F; Unused??
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((x + 0), (y + height), this.zLevel, ((float) (textureX) * 0.00390625F), (float) (textureY + height) * 0.00390625F);
        tessellator.addVertexWithUV((x + width), (y + height), this.zLevel, ((float) (textureX + width) * 0.00390625F), (float) (textureY + height) * 0.00390625F);
        tessellator.addVertexWithUV((x + width), (y + 0), this.zLevel, ((float) (textureX + width) * 0.00390625F), (float) (textureY) * 0.00390625F);
        tessellator.addVertexWithUV((x + 0), (y + 0), this.zLevel, ((float) (textureX) * 0.00390625F), (float) (textureY) * 0.00390625F);
        tessellator.draw();
    }

    /**
     * Draws a textured rectangle using the texture currently bound to the TextureManager
     */
    public void drawTexturedModalRect(int xCoord, int yCoord, int minU, int minV, int maxU, int maxV) {
        //float f = 0.00390625F; Unused??
        //float f1 = 0.00390625F; Unused??
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(xCoord + 0.0F, (yCoord + (float) maxV), this.zLevel, ((float) (minU) * 0.00390625F), ((float) (minV + maxV) * 0.00390625F));
        tessellator.addVertexWithUV((xCoord + (float) maxU), (yCoord + (float) maxV), this.zLevel, ((float) (minU + maxU) * 0.00390625F), ((float) (minV + maxV) * 0.00390625F));
        tessellator.addVertexWithUV((xCoord + (float) maxU), (yCoord + 0.0F), this.zLevel, ((float) (minU + maxU) * 0.00390625F), ((float) (minV) * 0.00390625F));
        tessellator.addVertexWithUV((xCoord + 0.0F), (yCoord + 0.0F), this.zLevel, ((float) (minU) * 0.00390625F), ((float) (minV) * 0.00390625F));
        tessellator.draw();
    }

    /**
     * Draws a texture rectangle using the texture currently bound to the TextureManager
     */
    public void drawTexturedModalRect(int xCoord, int yCoord, TextureAtlasSprite textureSprite, int widthIn, int heightIn) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(xCoord, yCoord + heightIn, this.zLevel, textureSprite.getMinU(), textureSprite.getMaxV());
        tessellator.addVertexWithUV(xCoord + widthIn, yCoord + heightIn, this.zLevel, textureSprite.getMaxU(), textureSprite.getMaxV());
        tessellator.addVertexWithUV(xCoord + widthIn, yCoord, this.zLevel, textureSprite.getMaxU(), textureSprite.getMinV());
        tessellator.addVertexWithUV(xCoord, yCoord, this.zLevel, textureSprite.getMinU(), textureSprite.getMinV());
        tessellator.draw();
    }

    /**
     * Draws a textured rectangle at z = 0. Args: x, y, u, v, width, height, textureWidth, textureHeight
     */
    public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, (y + height), 0.0D, (u * f), ((v + (float) height) * f1));
        tessellator.addVertexWithUV((x + width), (y + height), 0.0D, ((u + (float) width) * f), ((v + (float) height) * f1));
        tessellator.addVertexWithUV((x + width), y, 0.0D, ((u + (float) width) * f), (v * f1));
        tessellator.addVertexWithUV(x, y, 0.0D, (u * f), (v * f1));
        tessellator.draw();
    }

    /**
     * Draws a scaled, textured, tiled modal rect at z = 0. This method isn't used anywhere in vanilla code.
     */
    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, 0.0D, u * f, (v + (float) vHeight) * f1);
        tessellator.addVertexWithUV(x + width, y + height, 0.0D, (u + (float) uWidth) * f, (v + (float) vHeight) * f1);
        tessellator.addVertexWithUV(x + width, y, 0.0D, (u + (float) uWidth) * f, v * f1);
        tessellator.addVertexWithUV(x, y, 0.0D, u * f, v * f1);
        tessellator.draw();
    }
}