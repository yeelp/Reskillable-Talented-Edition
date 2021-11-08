package codersafterdark.reskillable.common.skill;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.util.ResourceLocation;

public class SkillDefense extends Skill {
    public SkillDefense() {
        super(new ResourceLocation(MOD_ID, "defense"), new ResourceLocation("textures/blocks/quartz_block_side.png"));
        this.skillConfig.setLevelCap(100);
    }
}