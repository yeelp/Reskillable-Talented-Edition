package codersafterdark.reskillable.common.profession.mage.alchemist;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import com.google.common.collect.ImmutableSet;
import com.tmtravlr.potioncore.PotionCoreAttributes;

import net.minecraft.util.ResourceLocation;
import yeelp.scalingfeast.init.SFPotion;

public class TalentIronHearted extends AbstractPotionEffectBasedTalent {
	public TalentIronHearted() {
		super(ImmutableSet.of(new PotionEffectBasedAttributeModifier(PotionCoreAttributes.DAMAGE_RESISTANCE, "IronHearted Damage Resistance Bonus", SFPotion.ironstomach, 0.1, 2)), new ResourceLocation(MOD_ID, "ironHearted"), 2, 3, new ResourceLocation(MOD_ID, "mage"), new ResourceLocation(MOD_ID, "alchemist"), 3, "profession|reskillable:mage|13");
	}
}
