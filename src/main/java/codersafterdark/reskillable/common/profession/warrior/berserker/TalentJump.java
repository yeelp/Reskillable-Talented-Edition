package codersafterdark.reskillable.common.profession.warrior.berserker;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentJump extends Talent {
    public TalentJump() {
        super(new ResourceLocation(MOD_ID, "jump"), 0, 1, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "berserker"),
                3, "profession|reskillable:warrior|26");
        setCap(5);
    }

}
