package codersafterdark.reskillable.common.profession.rogue.archer;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

public class TalentArcherAdvanced extends Talent {
    public TalentArcherAdvanced() {
        super(new ResourceLocation(MOD_ID, "archer_advanced"), 1, 2, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "archer"),
                3, "profession|reskillable:rogue|19", "talent|reskillable:assassin_novice");
        MinecraftForge.EVENT_BUS.register(this);
    }

}
