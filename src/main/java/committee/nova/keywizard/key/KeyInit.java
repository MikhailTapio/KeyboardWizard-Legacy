package committee.nova.keywizard.key;

import committee.nova.keywizard.KeyWizard;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class KeyInit {
    public static KeyBinding keyOpenKeyWizard = new KeyBinding(I18n.format(KeyWizard.MODID + ".keybind.openKeyboardWizard"), Keyboard.KEY_F8, "key.categories.misc");

    public static void init() {
        ClientRegistry.registerKeyBinding(keyOpenKeyWizard);
    }
}
