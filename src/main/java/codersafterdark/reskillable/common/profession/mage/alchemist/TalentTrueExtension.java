package codersafterdark.reskillable.common.profession.mage.alchemist;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import com.tmtravlr.potioncore.potion.PotionExtension;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.common.Reskillable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TalentTrueExtension extends Talent {

	private static final class Timer {
		private long reference;
		public Timer() {
			restart();
		}
		
		public long getTimeDiffInSeconds() {
			return (System.nanoTime() - reference)/1_000_000_000;
		}
		
		public void restart() {
			reference = System.nanoTime();
		}
	}
	
	private static final HashMap<UUID, Timer> counters = new HashMap<UUID, Timer>();
	
	public TalentTrueExtension() {
		super(new ResourceLocation(MOD_ID, "trueExtension"), 1, 1, new ResourceLocation(MOD_ID, "mage"), new ResourceLocation(MOD_ID, "alchemist"), 3, "profession|reskillable:mage|13");
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onPlayerUpdate(LivingUpdateEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayerMP) {
			processPlayer((EntityPlayerMP) event.getEntityLiving());
		}
	}
	
	private void processPlayer(EntityPlayerMP player) {
		if(PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)) {
			UUID uuid = player.getUniqueID();
			Optional<Timer> optTimer = Optional.ofNullable(counters.putIfAbsent(uuid, new Timer()));
			boolean shouldApplyPotion = optTimer.map((t) -> t.getTimeDiffInSeconds() % 10 == 0).orElse(false);		
			if(shouldApplyPotion && !player.isPotionActive(PotionExtension.INSTANCE)) {
				optTimer.get().restart(); //get() always safe here, no check needed.
				player.addPotionEffect(new PotionEffect(PotionExtension.INSTANCE, 100, 1, false, false));
			}
		}
	}
}
