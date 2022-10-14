package committee.nova.keywizard.gui;

import committee.nova.keywizard.util.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;

public abstract class GuiScrollingList {
    private final Minecraft client;
    protected final int listWidth;
    protected final int listHeight;
    protected final int screenWidth;
    protected final int screenHeight;
    protected final int top;
    protected final int bottom;
    protected final int right;
    protected final int left;
    protected final int slotHeight;
    private int scrollUpActionId;
    private int scrollDownActionId;
    protected int mouseX;
    protected int mouseY;
    private float initialMouseClickY = -2.0F;
    private float scrollFactor;
    private float scrollDistance;
    protected int selectedIndex = -1;
    private long lastClickTime = 0L;
    private boolean highlightSelected = true;
    private boolean hasHeader;
    private int headerHeight;
    protected boolean captureMouse = true;

    @Deprecated // We need to know screen size.
    public GuiScrollingList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight) {
        this(client, width, height, top, bottom, left, entryHeight, width, height);
    }

    public GuiScrollingList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight) {
        this.client = client;
        this.listWidth = width;
        this.listHeight = height;
        this.top = top;
        this.bottom = bottom;
        this.slotHeight = entryHeight;
        this.left = left;
        this.right = width + this.left;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    @Deprecated // Unused, remove in 1.9.3?
    public void func_27258_a(boolean p_27258_1_) {
        this.highlightSelected = p_27258_1_;
    }

    @Deprecated
    protected void func_27259_a(boolean hasFooter, int footerHeight) {
        setHeaderInfo(hasFooter, footerHeight);
    }

    protected void setHeaderInfo(boolean hasHeader, int headerHeight) {
        this.hasHeader = hasHeader;
        this.headerHeight = headerHeight;
        if (!hasHeader) this.headerHeight = 0;
    }

    protected abstract int getSize();

    protected abstract void elementClicked(int index, boolean doubleClick);

    protected abstract boolean isSelected(int index);

    protected int getContentHeight() {
        return this.getSize() * this.slotHeight + this.headerHeight;
    }

    protected abstract void drawBackground();

    /**
     * Draw anything special on the screen. GL_SCISSOR is enabled for anything that
     * is rendered outside the view box. Do not mess with SCISSOR unless you support this.
     */
    protected abstract void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess);

    @Deprecated
    protected void func_27260_a(int entryRight, int relativeY, Tessellator tess) {
    }

    /**
     * Draw anything special on the screen. GL_SCISSOR is enabled for anything that
     * is rendered outside the view box. Do not mess with SCISSOR unless you support this.
     */
    protected void drawHeader(int entryRight, int relativeY, Tessellator tess) {
        func_27260_a(entryRight, relativeY, tess);
    }

    @Deprecated
    protected void func_27255_a(int x, int y) {
    }

    protected void clickHeader(int x, int y) {
        func_27255_a(x, y);
    }

    @Deprecated
    protected void func_27257_b(int mouseX, int mouseY) {
    }

    /**
     * Draw anything special on the screen. GL_SCISSOR is enabled for anything that
     * is rendered outside the view box. Do not mess with SCISSOR unless you support this.
     */
    protected void drawScreen$1(int mouseX, int mouseY) {
        func_27257_b(mouseX, mouseY);
    }

    @Deprecated // Unused, Remove in 1.9.3?
    public int func_27256_c(int x, int y) {
        int left = this.left + 1;
        int right = this.left + this.listWidth - 7;
        int relativeY = y - this.top - this.headerHeight + (int) this.scrollDistance - 4;
        int entryIndex = relativeY / this.slotHeight;
        return x >= left && x <= right && entryIndex >= 0 && relativeY >= 0 && entryIndex < this.getSize() ? entryIndex : -1;
    }

    // FIXME: is this correct/still needed?
    public void registerScrollButtons(List<GuiButton> buttons, int upActionID, int downActionID) {
        this.scrollUpActionId = upActionID;
        this.scrollDownActionId = downActionID;
    }

    private void applyScrollLimits() {
        int listHeight = this.getContentHeight() - (this.bottom - this.top - 4);

        if (listHeight < 0) {
            listHeight /= 2;
        }

        if (this.scrollDistance < 0.0F) {
            this.scrollDistance = 0.0F;
        }

        if (this.scrollDistance > (float) listHeight) {
            this.scrollDistance = (float) listHeight;
        }
    }

    public void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == this.scrollUpActionId) {
                this.scrollDistance -= (float) (this.slotHeight * 2 / 3);
                this.initialMouseClickY = -2.0F;
                this.applyScrollLimits();
            } else if (button.id == this.scrollDownActionId) {
                this.scrollDistance += (float) (this.slotHeight * 2 / 3);
                this.initialMouseClickY = -2.0F;
                this.applyScrollLimits();
            }
        }
    }


    public void handleMouseInput(int mouseX, int mouseY) {
        boolean isHovering = mouseX >= this.left && mouseX <= this.left + this.listWidth &&
                mouseY >= this.top && mouseY <= this.bottom;
        if (!isHovering)
            return;

        int scroll = Mouse.getEventDWheel();
        if (scroll != 0) {
            this.scrollDistance += (-1 * scroll / 120.0F) * this.slotHeight / 2;
        }
    }

    public void drawScreen(int mouseX, int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.drawBackground();

        boolean isHovering = mouseX >= this.left && mouseX <= this.left + this.listWidth &&
                mouseY >= this.top && mouseY <= this.bottom;
        int listLength = this.getSize();
        int scrollBarWidth = 6;
        int scrollBarRight = this.left + this.listWidth;
        int scrollBarLeft = scrollBarRight - scrollBarWidth;
        int entryLeft = this.left;
        int entryRight = scrollBarLeft - 1;
        int viewHeight = this.bottom - this.top;
        int border = 4;

        if (Mouse.isButtonDown(0)) {
            if (this.initialMouseClickY == -1.0F) {
                if (isHovering) {
                    int mouseListY = mouseY - this.top - this.headerHeight + (int) this.scrollDistance - border;
                    int slotIndex = mouseListY / this.slotHeight;

                    if (mouseX <= entryRight && slotIndex >= 0 && mouseListY >= 0 && slotIndex < listLength) {
                        this.elementClicked(slotIndex, slotIndex == this.selectedIndex && System.currentTimeMillis() - this.lastClickTime < 250L);
                        this.selectedIndex = slotIndex;
                        this.lastClickTime = System.currentTimeMillis();
                    } else if (mouseX >= entryLeft && mouseX <= entryRight && mouseListY < 0) {
                        this.clickHeader(mouseX - entryLeft, mouseY - this.top + (int) this.scrollDistance - border);
                    }

                    if (mouseX >= scrollBarLeft && mouseX <= scrollBarRight) {
                        this.scrollFactor = -1.0F;
                        int scrollHeight = this.getContentHeight() - viewHeight - border;
                        if (scrollHeight < 1) scrollHeight = 1;

                        int var13 = (int) ((float) (viewHeight * viewHeight) / (float) this.getContentHeight());

                        if (var13 < 32) var13 = 32;
                        if (var13 > viewHeight - border * 2)
                            var13 = viewHeight - border * 2;

                        this.scrollFactor /= (float) (viewHeight - var13) / (float) scrollHeight;
                    } else {
                        this.scrollFactor = 1.0F;
                    }

                    this.initialMouseClickY = mouseY;
                } else {
                    this.initialMouseClickY = -2.0F;
                }
            } else if (this.initialMouseClickY >= 0.0F) {
                this.scrollDistance -= ((float) mouseY - this.initialMouseClickY) * this.scrollFactor;
                this.initialMouseClickY = (float) mouseY;
            }
        } else {
            this.initialMouseClickY = -1.0F;
        }

        this.applyScrollLimits();

        Tessellator tess = Tessellator.instance;

        ScaledResolution res = new ScaledResolution(client, client.displayWidth, client.displayHeight);
        double scaleW = client.displayWidth / res.getScaledWidth_double();
        double scaleH = client.displayHeight / res.getScaledHeight_double();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (left * scaleW), (int) (client.displayHeight - (bottom * scaleH)),
                (int) (listWidth * scaleW), (int) (viewHeight * scaleH));

        if (this.client.theWorld != null) {
            this.drawGradientRect(this.left, this.top, this.right, this.bottom, 0xC0101010, 0xD0101010);
        } else // Draw dark dirt background
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_FOG);
            this.client.renderEngine.bindTexture(Gui.optionsBackground);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            final float scale = 32.0F;
            tess.startDrawingQuads();
            tess.setColorRGBA(0x20, 0x20, 0x20, 0xFF);
            tess.addVertexWithUV(this.left, this.bottom, 0.0D, this.left / scale, (this.bottom + (int) this.scrollDistance) / scale);
            tess.addVertexWithUV(this.right, this.bottom, 0.0D, this.right / scale, (this.bottom + (int) this.scrollDistance) / scale);
            tess.addVertexWithUV(this.right, this.top, 0.0D, this.right / scale, (this.top + (int) this.scrollDistance) / scale);
            tess.addVertexWithUV(this.left, this.top, 0.0D, this.left / scale, (this.top + (int) this.scrollDistance) / scale);
            tess.draw();
        }

        int baseY = this.top + border - (int) this.scrollDistance;

        if (this.hasHeader) {
            this.drawHeader(entryRight, baseY, tess);
        }

        for (int slotIdx = 0; slotIdx < listLength; ++slotIdx) {
            int slotTop = baseY + slotIdx * this.slotHeight + this.headerHeight;
            int slotBuffer = this.slotHeight - border;

            if (slotTop <= this.bottom && slotTop + slotBuffer >= this.top) {
                if (this.highlightSelected && this.isSelected(slotIdx)) {
                    int min = this.left;
                    int max = entryRight;
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tess.startDrawingQuads();
                    tess.setColorRGBA(0x80, 0x80, 0x80, 0xFF);
                    tess.addVertexWithUV(min, slotTop + slotBuffer + 2, 0, 0, 1);
                    tess.addVertexWithUV(max, slotTop + slotBuffer + 2, 0, 1, 1);
                    tess.addVertexWithUV(max, slotTop - 2, 0, 1, 0);
                    tess.addVertexWithUV(min, slotTop - 2, 0, 0, 0);
                    tess.setColorRGBA(0x00, 0x00, 0x00, 0xFF);
                    tess.addVertexWithUV(min + 1, slotTop + slotBuffer + 1, 0, 0, 1);
                    tess.addVertexWithUV(max - 1, slotTop + slotBuffer + 1, 0, 1, 1);
                    tess.addVertexWithUV(max - 1, slotTop - 1, 0, 1, 0);
                    tess.addVertexWithUV(min + 1, slotTop - 1, 0, 0, 0);
                    tess.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                }

                this.drawSlot(slotIdx, entryRight, slotTop, slotBuffer, tess);
            }
        }

        GL11.glDisable(GL11.GL_DEPTH_TEST);

        int extraHeight = (this.getContentHeight() + border) - viewHeight;
        if (extraHeight > 0) {
            int height = (viewHeight * viewHeight) / this.getContentHeight();

            if (height < 32) height = 32;

            if (height > viewHeight - border * 2)
                height = viewHeight - border * 2;

            int barTop = (int) this.scrollDistance * (viewHeight - height) / extraHeight + this.top;
            if (barTop < this.top) {
                barTop = this.top;
            }

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tess.startDrawingQuads();
            tess.setColorRGBA(0x00, 0x00, 0x00, 0xFF);
            tess.addVertexWithUV(scrollBarLeft, this.bottom, 0.0D, 0.0D, 1.0D);
            tess.addVertexWithUV(scrollBarRight, this.bottom, 0.0D, 1.0D, 1.0D);
            tess.addVertexWithUV(scrollBarRight, this.top, 0.0D, 1.0D, 0.0D);
            tess.addVertexWithUV(scrollBarLeft, this.top, 0.0D, 0.0D, 0.0D);
            tess.draw();
            tess.startDrawingQuads();
            tess.setColorRGBA(0x80, 0x80, 0x80, 0xFF);
            tess.addVertexWithUV(scrollBarLeft, barTop + height, 0.0D, 0.0D, 1.0D);
            tess.addVertexWithUV(scrollBarRight, barTop + height, 0.0D, 1.0D, 1.0D);
            tess.addVertexWithUV(scrollBarRight, barTop, 0.0D, 1.0D, 0.0D);
            tess.addVertexWithUV(scrollBarLeft, barTop, 0.0D, 0.0D, 0.0D);
            tess.draw();
            tess.startDrawingQuads();
            tess.setColorRGBA(0xC0, 0xC0, 0xC0, 0xFF);
            tess.addVertexWithUV(scrollBarLeft, barTop + height - 1, 0.0D, 0.0D, 1.0D);
            tess.addVertexWithUV(scrollBarRight - 1, barTop + height - 1, 0.0D, 1.0D, 1.0D);
            tess.addVertexWithUV(scrollBarRight - 1, barTop, 0.0D, 1.0D, 0.0D);
            tess.addVertexWithUV(scrollBarLeft, barTop, 0.0D, 0.0D, 0.0D);
            tess.draw();
        }

        this.drawScreen$1(mouseX, mouseY);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    protected void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6) {
        GuiUtils.drawGradientRect(par1, par2, par3, par4, par5, par6);
    }
}
