package codersafterdark.reskillable.common.profession.mage.alchemist;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import yeelp.scalingfeast.init.SFPotion;

public class TalentSoftHearted extends AbstractPotionEffectBasedTalent {
	public TalentSoftHearted() {
		super(ImmutableSet.of(new PotionEffectBasedAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "SoftHearted Speed Bonus", SFPotion.softstomach, 0.1, 2)), new ResourceLocation(MOD_ID, "softHearted"), 2, 1, new ResourceLocation(MOD_ID, "mage"), new ResourceLocation(MOD_ID, "alchemist"), 3, "profession|reskillable:mage|13");
	}
}
