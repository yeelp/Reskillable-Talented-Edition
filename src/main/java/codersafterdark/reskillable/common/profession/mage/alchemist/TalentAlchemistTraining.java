package codersafterdark.reskillable.common.profession.mage.alchemist;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.common.skill.attributes.ReskillableAttributes;
import codersafterdark.reskillable.common.util.talentskeletons.TrainingTalent;
import net.minecraft.util.ResourceLocation;

public abstract class TalentAlchemistTraining extends TrainingTalent {

	protected TalentAlchemistTraining(TrainingLevel level, int x, int y, int cost, String... defaultRequirements) {
		super(ReskillableAttributes.POTION_POTENCY, level, x, y, new ResourceLocation(MOD_ID, "mage"), new ResourceLocation(MOD_ID, "alchemist"), cost, defaultRequirements);
	}

	@Override
	protected float getAttributeModifierAmount(TrainingLevel level) {
		return 0.1f + level.ordinal() * 0.2f;
	}

	@Override
	protected int getOperation() {
		return 0;
	}

	public final class AlchemistNovice extends TalentAlchemistTraining {
		public AlchemistNovice() {
			super(TrainingLevel.NOVICE, 13,	13, 5, "profession|reskillable:mage|13");
		}		
	}
	
	public final class AlchemistAdvanced extends TalentAlchemistTraining {
		public AlchemistAdvanced() {
			super(TrainingLevel.ADVANCED, 14, 14, 6, "profession|reskillable:mage|13");
		}
	}
	
	public final class AlchemistMaster extends TalentAlchemistTraining {
		public AlchemistMaster() {
			super(TrainingLevel.MASTER, 15, 15, 7, "profession|reskillable:mage|13");
		}
	}
}
