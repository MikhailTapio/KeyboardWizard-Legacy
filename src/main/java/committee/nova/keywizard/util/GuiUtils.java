package committee.nova.keywizard.util;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiUtils {
    private static final ItemStack cachedTooltipStack = new ItemStack(Blocks.air);

    public static void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6) {
        float f = (float) (par5 >> 24 & 255) / 255.0F;
        float f1 = (float) (par5 >> 16 & 255) / 255.0F;
        float f2 = (float) (par5 >> 8 & 255) / 255.0F;
        float f3 = (float) (par5 & 255) / 255.0F;
        float f4 = (float) (par6 >> 24 & 255) / 255.0F;
        float f5 = (float) (par6 >> 16 & 255) / 255.0F;
        float f6 = (float) (par6 >> 8 & 255) / 255.0F;
        float f7 = (float) (par6 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer wr = tessellator.getWorldRenderer();
        wr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        wr.pos(par3, par2, 0.0D).color(f1, f2, f3, f).endVertex();
        wr.pos(par1, par2, 0.0D).color(f1, f2, f3, f).endVertex();
        wr.pos(par1, par4, 0.0D).color(f5, f6, f7, f4).endVertex();
        wr.pos(par3, par4, 0.0D).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawGradientRect(int zLevel, int left, int top, int right, int bottom, int startColor, int endColor) {
        float startAlpha = (float) (startColor >> 24 & 255) / 255.0F;
        float startRed = (float) (startColor >> 16 & 255) / 255.0F;
        float startGreen = (float) (startColor >> 8 & 255) / 255.0F;
        float startBlue = (float) (startColor & 255) / 255.0F;
        float endAlpha = (float) (endColor >> 24 & 255) / 255.0F;
        float endRed = (float) (endColor >> 16 & 255) / 255.0F;
        float endGreen = (float) (endColor >> 8 & 255) / 255.0F;
        float endBlue = (float) (endColor & 255) / 255.0F;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);

        Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer wr = tessellator.getWorldRenderer();
        wr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        wr.pos(right, top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        wr.pos(left, top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        wr.pos(left, bottom, zLevel).color(endRed, endGreen, endBlue, endAlpha).endVertex();
        wr.pos(right, bottom, zLevel).color(endRed, endGreen, endBlue, endAlpha).endVertex();
        tessellator.draw();

        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Draws a tooltip box on the screen with text in it.
     * Automatically positions the box relative to the mouse to match Mojang's implementation.
     * Automatically wraps text when there is not enough space on the screen to display the text without wrapping.
     * Can have a maximum width set to avoid creating very wide tooltips.
     *
     * @param textLines    the lines of text to be drawn in a hovering tooltip box.
     * @param mouseX       the mouse X position
     * @param mouseY       the mouse Y position
     * @param screenWidth  the available screen width for the tooltip to drawn in
     * @param screenHeight the available  screen height for the tooltip to drawn in
     * @param maxTextWidth the maximum width of the text in the tooltip box.
     *                     Set to a negative number to have no max width.
     * @param font         the font for drawing the text in the tooltip box
     */
    public static void drawHoveringText(List<String> textLines, int mouseX, int mouseY, int screenWidth, int screenHeight, int maxTextWidth, FontRenderer font) {
        drawHoveringText(cachedTooltipStack, textLines, mouseX, mouseY, screenWidth, screenHeight, maxTextWidth, font);
    }

    /**
     * Use this version if calling from somewhere where ItemStack context is available.
     *
     * @see #drawHoveringText(List, int, int, int, int, int, FontRenderer)
     */
    public static void drawHoveringText(@Nonnull final ItemStack stack, List<String> textLines, int mouseX, int mouseY, int screenWidth, int screenHeight, int maxTextWidth, FontRenderer font) {
        if (!textLines.isEmpty()) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int tooltipTextWidth = 0;

            for (String textLine : textLines) {
                int textLineWidth = font.getStringWidth(textLine);

                if (textLineWidth > tooltipTextWidth) {
                    tooltipTextWidth = textLineWidth;
                }
            }

            boolean needsWrap = false;

            int titleLinesCount = 1;
            int tooltipX = mouseX + 12;
            if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
                tooltipX = mouseX - 16 - tooltipTextWidth;
                if (tooltipX < 4) // if the tooltip doesn't fit on the screen
                {
                    if (mouseX > screenWidth / 2) {
                        tooltipTextWidth = mouseX - 12 - 8;
                    } else {
                        tooltipTextWidth = screenWidth - 16 - mouseX;
                    }
                    needsWrap = true;
                }
            }

            if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth) {
                tooltipTextWidth = maxTextWidth;
                needsWrap = true;
            }

            if (needsWrap) {
                int wrappedTooltipWidth = 0;
                List<String> wrappedTextLines = new ArrayList<>();
                for (int i = 0; i < textLines.size(); i++) {
                    String textLine = textLines.get(i);
                    List<String> wrappedLine = font.listFormattedStringToWidth(textLine, tooltipTextWidth);
                    if (i == 0) {
                        titleLinesCount = wrappedLine.size();
                    }

                    for (String line : wrappedLine) {
                        int lineWidth = font.getStringWidth(line);
                        if (lineWidth > wrappedTooltipWidth) {
                            wrappedTooltipWidth = lineWidth;
                        }
                        wrappedTextLines.add(line);
                    }
                }
                tooltipTextWidth = wrappedTooltipWidth;
                textLines = wrappedTextLines;

                if (mouseX > screenWidth / 2) {
                    tooltipX = mouseX - 16 - tooltipTextWidth;
                } else {
                    tooltipX = mouseX + 12;
                }
            }

            int tooltipY = mouseY - 12;
            int tooltipHeight = 8;

            if (textLines.size() > 1) {
                tooltipHeight += (textLines.size() - 1) * 10;
                if (textLines.size() > titleLinesCount) {
                    tooltipHeight += 2; // gap between title lines and next lines
                }
            }

            if (tooltipY < 4) {
                tooltipY = 4;
            } else if (tooltipY + tooltipHeight + 4 > screenHeight) {
                tooltipY = screenHeight - tooltipHeight - 4;
            }

            final int zLevel = 300;
            int backgroundColor = 0xF0100010;
            int borderColorStart = 0x505000FF;
            int borderColorEnd = (borderColorStart & 0xFEFEFE) >> 1 | borderColorStart & 0xFF000000;
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);


            for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber) {
                String line = textLines.get(lineNumber);
                font.drawStringWithShadow(line, tooltipX, tooltipY, -1);

                if (lineNumber + 1 == titleLinesCount) {
                    tooltipY += 2;
                }

                tooltipY += 10;
            }

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }
}
