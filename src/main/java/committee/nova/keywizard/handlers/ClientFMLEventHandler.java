package committee.nova.keywizard.handlers;

import committee.nova.keywizard.gui.GuiKeyWizard;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

import static committee.nova.keywizard.key.KeyInit.keyOpenKeyWizard;

public class ClientFMLEventHandler {
    @SubscribeEvent
    public void keyPressed(InputEvent.KeyInputEvent e) {
        if (keyOpenKeyWizard.isPressed()) {
            FMLClientHandler.instance().getClient().displayGuiScreen(new GuiKeyWizard(FMLClientHandler.instance().getClient(), null));
        }
    }
}
