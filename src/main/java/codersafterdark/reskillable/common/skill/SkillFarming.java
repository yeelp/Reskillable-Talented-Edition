package codersafterdark.reskillable.common.skill;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.util.ResourceLocation;

public class SkillFarming extends Skill {
    public SkillFarming() {
        super(new ResourceLocation(MOD_ID, "farming"), new ResourceLocation("textures/blocks/dirt.png"));
        this.skillConfig.setLevelCap(100);
    }
}