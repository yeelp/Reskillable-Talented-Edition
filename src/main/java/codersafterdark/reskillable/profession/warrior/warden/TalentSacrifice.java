package codersafterdark.reskillable.profession.warrior.warden;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TalentSacrifice extends Talent {

    public TalentSacrifice() {
        super(new ResourceLocation(MOD_ID, "sacrifice"), 2, 2, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "warden"),
                3, "reskillable:attack 10", "reskillable:defense 8");
        setCap(5);
        this.setIcon(new ResourceLocation("textures/items/iron_sword.png"));
    }

}
