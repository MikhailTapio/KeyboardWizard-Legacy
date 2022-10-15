package committee.nova.keywizard.handlers;

import committee.nova.keywizard.gui.GuiKeyWizard;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import static committee.nova.keywizard.key.KeyInit.keyOpenKeyWizard;

public class ClientFMLEventHandler {
    @SubscribeEvent
    public void keyPressed(InputEvent.KeyInputEvent e) {
        if (keyOpenKeyWizard.isPressed()) {
            FMLClientHandler.instance().getClient().displayGuiScreen(new GuiKeyWizard(FMLClientHandler.instance().getClient(), null));
        }
    }
}
