package codersafterdark.reskillable.common.skill;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.util.ResourceLocation;

public class SkillMining extends Skill {
    public SkillMining() {
        super(new ResourceLocation(MOD_ID, "mining"), new ResourceLocation("textures/blocks/stone.png"));
        this.skillConfig.setLevelCap(100);
    }
}