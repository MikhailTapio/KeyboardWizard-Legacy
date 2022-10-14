package committee.nova.keywizard;

import committee.nova.keywizard.handlers.ClientFMLEventHandler;
import committee.nova.keywizard.handlers.ClientForgeEventHandler;
import committee.nova.keywizard.key.KeyInit;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = KeyWizard.MODID, useMetadata = true, dependencies = "required-after:mkb@[2.0.0,)")
public class KeyWizard {

    public static final String MODID = "keywizard";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    private static String[] conflictingMods = {"controlling"};

    @Mod.Instance
    public static KeyWizard instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        if (e.getSide() != Side.CLIENT) return;
        LOGGER.log(Level.INFO, "Let's do some keyboard magic!");
        KeyWizardConfig.init(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        if (e.getSide() != Side.CLIENT) return;
        KeyInit.init();
        MinecraftForge.EVENT_BUS.register(new ClientForgeEventHandler());
        FMLCommonHandler.instance().bus().register(new ClientFMLEventHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        if (e.getSide() != Side.CLIENT) return;
        boolean flag = false;
        if (KeyWizardConfig.canOpenFromControlsGui()) {
            LOGGER.log(Level.WARN, "Controls gui override enabled, this may cause problems with other mods");
            for (String id : conflictingMods) {
                if (Loader.isModLoaded(id)) {
                    flag = true;
                    break;
                }
            }
            if (flag) LOGGER.log(Level.WARN, "Conflicting mod detected, controls gui override may not work");
        }
    }
}
