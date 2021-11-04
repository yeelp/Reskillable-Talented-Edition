package codersafterdark.reskillable.common.profession.mage.alchemist;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.tmtravlr.potioncore.potion.PotionMagicShield;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;

public class TalentAlchemicDrive extends AbstractPotionEffectBasedTalent {
	public TalentAlchemicDrive() {
		super(ImmutableSet.of(new PotionEffectBasedAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "AlchemicDrive Speed Boost", PotionMagicShield.INSTANCE, 0.1, 2),
				              new PotionEffectBasedAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "AlchemicDrive Attack Speed Boost", PotionMagicShield.INSTANCE, 0.25, 2)), 
				new ResourceLocation(MOD_ID, "alchemicDrive"), 1, 3, new ResourceLocation(MOD_ID, "mage"), new ResourceLocation(MOD_ID, "alchemist"), 3, "profession|reskillable:mage|13");
	}
}