package codersafterdark.reskillable.common.profession.rogue.archer;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentArcherNovice extends Talent {

    public TalentArcherNovice() {
        super(new ResourceLocation(MOD_ID, "archer_novice"), 1, 4, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "archer"),
                3, "profession|reskillable:rogue|6", "reskillable:agility|8");
    }

}
