package codersafterdark.reskillable.common.profession.mage.wizard;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.util.ResourceLocation;

public class TalentWizardNovice extends Talent {

    public TalentWizardNovice() {
        super(new ResourceLocation(MOD_ID, "wizard_novice"), 1, 4, new ResourceLocation(MOD_ID, "mage"), new ResourceLocation(MOD_ID, "wizard"),
                3, "profession|reskillable:mage|6");
    }

}
