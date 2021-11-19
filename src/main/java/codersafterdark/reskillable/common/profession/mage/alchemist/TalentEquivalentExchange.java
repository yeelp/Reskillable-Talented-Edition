package codersafterdark.reskillable.common.profession.mage.alchemist;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import com.google.common.collect.ImmutableSet;
import com.tmtravlr.potioncore.PotionCoreAttributes;

import codersafterdark.reskillable.common.skill.attributes.ReskillableAttributes;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;

public class TalentEquivalentExchange extends AbstractPotionEffectBasedTalent {

	public TalentEquivalentExchange() {
		super(ImmutableSet.of(new PotionEffectBasedAttributeModifier(ReskillableAttributes.POTION_POTENCY, "Equivalent Exchange Potency", MobEffects.WEAKNESS, 0.25, 0), new PotionEffectBasedAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "EquivalentExchange Attack Speed Bonus", MobEffects.SLOWNESS, 0.2, 2), new PotionEffectBasedAttributeModifier(PotionCoreAttributes.DAMAGE_RESISTANCE, "EquivalentExchange Damage Resistance Bonus", MobEffects.MINING_FATIGUE, 0.1, 2), new PotionEffectBasedAttributeModifier(EntityPlayer.REACH_DISTANCE, "EquivalentExchange Reach Distance Bonus", MobEffects.BLINDNESS, 1, 0)), new ResourceLocation(MOD_ID, "equivalentExchange"), 3, 1, new ResourceLocation(MOD_ID, "mage"), new ResourceLocation(MOD_ID, "alchemist"), 3, "profession|reskillable:mage|13");
	}
}
