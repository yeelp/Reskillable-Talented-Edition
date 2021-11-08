package codersafterdark.reskillable.common.profession.gatherer.seafarer;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.util.ResourceLocation;

public class TalentDiet extends Talent {
    public TalentDiet() {
        super(new ResourceLocation(MOD_ID, "salty_diet"), 1, 2, new ResourceLocation(MOD_ID, "gatherer"), new ResourceLocation(MOD_ID, "seafarer"),
                3, "profession|reskillable:gathere|19");
    }

}
