package codersafterdark.reskillable.common.skill;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.util.ResourceLocation;

public class SkillAgility extends Skill {
    public SkillAgility() {
        super(new ResourceLocation(MOD_ID, "agility"), new ResourceLocation("textures/blocks/gravel.png"));
        this.skillConfig.setLevelCap(100);
    }
}