package codersafterdark.reskillable.profession.warrior.berserker;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TalentBerserkAdvanced extends Talent {
    public TalentBerserkAdvanced() {
        super(new ResourceLocation(MOD_ID, "berserk_advanced"), 1, 2, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "berserker"),
                3, "reskillable:attack 10", "reskillable:defense 8");
        setCap(1);
        this.setIcon(new ResourceLocation("textures/items/iron_sword.png"));
    }
}
