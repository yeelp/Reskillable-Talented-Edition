package codersafterdark.reskillable.common.profession.warrior.berserker;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentEdge extends Talent {
    public TalentEdge() {
        super(new ResourceLocation(MOD_ID, "edge"), 2, 4, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "berserker"),
                3, "reskillable:attack 10", "reskillable:defense 8");
        setCap(5);
    }
}
