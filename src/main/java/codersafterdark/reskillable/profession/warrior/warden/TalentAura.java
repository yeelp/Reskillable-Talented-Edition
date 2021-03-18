package codersafterdark.reskillable.profession.warrior.warden;

import codersafterdark.reskillable.api.talent.Talent;
import hellfirepvp.astralsorcery.common.potion.PotionCheatDeath;
import net.minecraft.init.PotionTypes;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TalentAura extends Talent {
    public TalentAura() {
        super(new ResourceLocation(MOD_ID, "aura"), 0, 4, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "warden"),
                3, "reskillable:attack 10", "reskillable:defense 8");
        setCap(5);
        this.setIcon(new ResourceLocation("textures/items/iron_sword.png"));
    }

}
