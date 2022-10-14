package committee.nova.keywizard.config;

import committee.nova.keywizard.util.KeyboardLayout;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Objects;

public class KeyWizardConfig {
    public static Configuration config;
    private static boolean openFromControlsGui;
    private static int maxMouseButtons;
    private static String layoutStr;

    public static void init(FMLPreInitializationEvent event) {
        config = new Configuration(new File(event.getModConfigurationDirectory(), "KeyboardWizard.cfg"));
        config.load();
        openFromControlsGui = config.getBoolean("openFromControlsGui", Configuration.CATEGORY_GENERAL, true, "If true, keyboard wizard will be accessible through a button or a keybinding in the controls gui. (Default: F8)");
        maxMouseButtons = config.getInt("maxMouseButtons", Configuration.CATEGORY_GENERAL, 5, 3, 15, "The number of mouse buttons to show (default:5).");
        layoutStr = config.getString("layout", Configuration.CATEGORY_GENERAL, "QWERTY", "Valid values: QWERTY, NUMPAD, AUXILIARY");
        config.save();
    }

    public static int getMaxMouseButtons() {
        return maxMouseButtons;
    }

    public static boolean canOpenFromControlsGui() {
        return openFromControlsGui;
    }

    public static KeyboardLayout getLayout() {
        for (final KeyboardLayout l : KeyboardLayout.values())
            if (Objects.equals(layoutStr, l.getDisplayName())) return l;
        return null;
    }

    public static KeyboardLayout layout = KeyboardLayout.QWERTY;

}
