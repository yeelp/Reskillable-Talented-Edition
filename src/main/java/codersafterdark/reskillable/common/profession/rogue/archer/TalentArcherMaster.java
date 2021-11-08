package codersafterdark.reskillable.common.profession.rogue.archer;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.util.ResourceLocation;

public class TalentArcherMaster extends Talent {
    public TalentArcherMaster() {
        super(new ResourceLocation(MOD_ID, "archer_master"), 1, 0, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "archer"),
                3, "profession|reskillable:rogue|32", "talent|reskillable:assassin_advanced");
    }

}
