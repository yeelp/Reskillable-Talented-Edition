package codersafterdark.reskillable.common.profession.warrior.berserker;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.util.ResourceLocation;

public class TalentBerserkMaster extends Talent {
    public TalentBerserkMaster() {
        super(new ResourceLocation(MOD_ID, "berserk_master"), 1, 0, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "berserker"),
                3, "profession|reskillable:warrior|32");
    }
}
