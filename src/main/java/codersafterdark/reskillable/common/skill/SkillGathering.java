package codersafterdark.reskillable.common.skill;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.util.ResourceLocation;

public class SkillGathering extends Skill {
    public SkillGathering() {
        super(new ResourceLocation(MOD_ID, "gathering"), new ResourceLocation("textures/blocks/log_oak.png"));
        this.skillConfig.setLevelCap(100);
    }
}