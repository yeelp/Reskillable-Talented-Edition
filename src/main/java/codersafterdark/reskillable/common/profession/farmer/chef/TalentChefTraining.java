package codersafterdark.reskillable.common.profession.farmer.chef;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.common.util.talentskeletons.TrainingTalent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;

public abstract class TalentChefTraining extends TrainingTalent {

	public TalentChefTraining(TrainingLevel level, int x, int y, int cost, String... defaultRequirements) {
		super(SharedMonsterAttributes.ATTACK_SPEED, level, x, y, new ResourceLocation(MOD_ID, "farmer"), new ResourceLocation(MOD_ID, "chef"), cost, defaultRequirements);
	}

	@Override
	protected float getAttributeModifierAmount(TrainingLevel level) {
		return level.ordinal() == 0 ? 0.05f : 0.1f;
	}

	@Override
	protected int getOperation() {
		return 2;
	}

	public static final class ChefNovice extends TalentChefTraining {
		public ChefNovice() {
			super(TrainingLevel.NOVICE, 5, 5, 5, "profession|reskillable:farmer|13");
		}
	}
	
	public static final class ChefAdvanced extends TalentChefTraining {
		public ChefAdvanced() {
			super(TrainingLevel.ADVANCED, 6, 6, 5, "profession|reskillable:farmer|13");
		}
	}
	
	public static final class ChefMaster extends TalentChefTraining {
		public ChefMaster() {
			super(TrainingLevel.MASTER, 6, 6, 5, "profession|reskillable:farmer|13");
		}
	}
}
