package codersafterdark.reskillable.common.profession.farmer.chef;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import java.util.Arrays;

import com.tmtravlr.potioncore.potion.PotionCure;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.command.ICommandManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TalentMorningShift extends Talent {

	public TalentMorningShift() {
		super(new ResourceLocation(MOD_ID, "morning_shift"), 12, 12, new ResourceLocation(MOD_ID, "farmer"), new ResourceLocation(MOD_ID, "chef"), 5, "profession|reskillable:farmer|13");
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public final void onPlayerWakeUp(PlayerWakeUpEvent evt) {
		if(evt.shouldSetSpawn() && Talent.hasTalentUnlocked(evt.getEntityPlayer(), this)) {
			evt.getEntityPlayer().addPotionEffect(new PotionEffect(PotionCure.INSTANCE, 1));
			MinecraftServer serv = evt.getEntityPlayer().world.getMinecraftServer();
			ICommandManager cmd = serv.commandManager;
			String name = evt.getEntityPlayer().getName();
			Arrays.stream(new String[]{"grain", "protein", "dairy", "vegetable", "fruit"}).map((nutrient) -> String.format("/nutrition add %s %s 10", name, nutrient)).forEach((s) -> cmd.executeCommand(serv, s));
		}
	}
}
