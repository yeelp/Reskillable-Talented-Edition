package codersafterdark.reskillable.common.profession.rogue.archer;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.util.ResourceLocation;

public class TalentSniper extends Talent {

    public TalentSniper() {
        super(new ResourceLocation(MOD_ID, "sniper"), 0, 2, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "archer"), 3, "profession|reskillable:rogue|13");
    }
}
