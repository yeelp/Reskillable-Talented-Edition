package codersafterdark.reskillable.common.profession.mage.alchemist;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;

public class TalentQuickDrinker extends AbstractPotionEffectBasedTalent {
	public TalentQuickDrinker() {
		super(ImmutableSet.of(new PotionEffectBasedAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, "QuickDrinker Knockback Resistance Bonus", ImmutableSet.of(MobEffects.REGENERATION, e2, e3), 0.5, 0)), new ResourceLocation(MOD_ID, "quickDrinker"), 1, 1, new ResourceLocation(MOD_ID, "mage"), new ResourceLocation(MOD_ID, "alchemist"), 3, "profession|reskillable:mage|13");
	}
}
