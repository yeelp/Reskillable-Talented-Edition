package codersafterdark.reskillable.common.profession.rogue.archer;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentPenetrate extends Talent {

    public TalentPenetrate() {
        super(new ResourceLocation(MOD_ID, "penetrate"), 0, 4, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "archer"), 3, "profession|reskillable:rogue|13");
    }

}
