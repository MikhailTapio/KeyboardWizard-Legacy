package committee.nova.keywizard.handlers;

import committee.nova.keywizard.config.KeyWizardConfig;
import committee.nova.keywizard.gui.GuiKeyWizard;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;

@SideOnly(Side.CLIENT)
public class ClientForgeEventHandler {

    @SubscribeEvent
    public void controlsGuiInit(InitGuiEvent e) {
        if (KeyWizardConfig.canOpenFromControlsGui() && e.gui instanceof GuiControls) {
            int width = e.gui.width;
            int buttonY = 0;
            for (Object b : e.buttonList) {
                if (!(b instanceof GuiButton)) continue;
                final GuiButton button = (GuiButton) b;
                if (button.id == 200) { // Done
                    button.width = 100;
                    button.xPosition = width / 2 + 60;
                    buttonY = button.yPosition;
                }
                if (button.id == 201) { // Reset All
                    button.width = 100;
                    button.xPosition = width / 2 - 160;
                }
            }
            e.buttonList.add(new GuiButton(203, width / 2 - 50, buttonY, 100, 20, I18n.format("gui.openKeyWiz")));
        }
    }

    @SubscribeEvent
    public void controlsGuiActionPreformed(ActionPerformedEvent e) {
        if (KeyWizardConfig.canOpenFromControlsGui() && e.gui instanceof GuiControls && e.button.id == 203) {
            FMLClientHandler.instance().getClient().displayGuiScreen(new GuiKeyWizard(FMLClientHandler.instance().getClient(), e.gui));
        }
    }
}
