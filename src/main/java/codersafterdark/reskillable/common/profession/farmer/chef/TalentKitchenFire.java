package codersafterdark.reskillable.common.profession.farmer.chef;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import java.util.UUID;

import codersafterdark.reskillable.common.skill.attributes.ReskillableAttributes;
import codersafterdark.reskillable.common.util.talentskeletons.BlockBasedTalent;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TalentKitchenFire extends BlockBasedTalent {

	private static final UUID KITCHEN_FLOOR_RESIST_UUID = UUID.fromString("bc72f9f7-0cba-4bd5-ab09-672f63b0556c");
	public TalentKitchenFire() {
		super("cookingforblockheads:kitchen_floor", ReskillableAttributes.DAMAGE_RESIST, new AttributeModifier(KITCHEN_FLOOR_RESIST_UUID, "Kitchen Fire Damage Resist", 0.1, 2), new ResourceLocation(MOD_ID, "kitchen_fire"), 12, 12, new ResourceLocation(MOD_ID, "farmer"), new ResourceLocation(MOD_ID, "chef"), 5, "profession|reskillable:farmer|13");
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public final void onLivingUpdate(LivingUpdateEvent evt) {
		if(evt.getEntityLiving() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) evt.getEntityLiving();
			player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 10, 2, true, false));
		}
	}
}
