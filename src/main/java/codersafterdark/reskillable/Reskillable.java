package codersafterdark.reskillable;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.data.cap.IPlayerData;
import codersafterdark.reskillable.api.data.cap.PlayerDataProvider;
import codersafterdark.reskillable.base.CommonProxy;
import codersafterdark.reskillable.lib.LibMisc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = LibMisc.MOD_ID, name = LibMisc.MOD_NAME, version = LibMisc.VERSION, guiFactory = LibMisc.GUI_FACTORY)
public class Reskillable {
    @SidedProxy(serverSide = LibMisc.PROXY_COMMON, clientSide = LibMisc.PROXY_CLIENT)
    public static CommonProxy proxy;

    public static Logger logger;

    @CapabilityInject(IPlayerData.class)
    public static final Capability<IPlayerData> CAPABILITY_PLAYER_DATA = null;

    public Reskillable() {
        ReskillableAPI.setInstance(new ReskillableAPI(new ReskillableModAccess()));
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @SubscribeEvent
    public void setCapabilityPlayerData(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation(LibMisc.MOD_ID, LibMisc.MOD_NAME), new PlayerDataProvider());
            IPlayerData data =;
            if (data != null) {
                data.
            }
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }
}