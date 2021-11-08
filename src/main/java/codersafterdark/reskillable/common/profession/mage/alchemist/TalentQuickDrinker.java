package codersafterdark.reskillable.common.profession.mage.alchemist;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import com.google.common.collect.ImmutableSet;

import codersafterdark.reskillable.api.talent.Talent;
import iguanaman.hungeroverhaul.HungerOverhaul;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.silentchaos512.scalinghealth.init.ModPotions;

public class TalentQuickDrinker extends AbstractPotionEffectBasedTalent {
	public TalentQuickDrinker() {
		super(ImmutableSet.of(new PotionEffectBasedAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, "QuickDrinker Knockback Resistance Bonus", ImmutableSet.of(MobEffects.REGENERATION, ModPotions.bandaged, HungerOverhaul.potionWellFed), 0.5, 0)), new ResourceLocation(MOD_ID, "quickDrinker"), 1, 1, new ResourceLocation(MOD_ID, "mage"), new ResourceLocation(MOD_ID, "alchemist"), 3, "profession|reskillable:mage|13");
	}

	@SubscribeEvent
	public void onItemUse(LivingEntityUseItemEvent.Start evt) {
		if(evt.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) evt.getEntityLiving();
			if(Talent.hasTalentUnlocked(player, this)) {
				evt.setDuration(evt.getDuration() / 2);
			}
		}
	}
}
