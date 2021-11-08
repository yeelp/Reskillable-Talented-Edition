package codersafterdark.reskillable.common.skill;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.util.ResourceLocation;

public class SkillMagic extends Skill {
    public SkillMagic() {
        super(new ResourceLocation(MOD_ID, "magic"), new ResourceLocation("textures/blocks/end_stone.png"));
        this.skillConfig.setLevelCap(100);
    }
}