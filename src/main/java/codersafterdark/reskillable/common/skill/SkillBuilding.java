package codersafterdark.reskillable.common.skill;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.util.ResourceLocation;

public class SkillBuilding extends Skill {
    public SkillBuilding() {
        super(new ResourceLocation(MOD_ID, "building"), new ResourceLocation("textures/blocks/brick.png"));
        this.skillConfig.setLevelCap(100);
    }
}