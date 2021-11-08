package codersafterdark.reskillable.common.skill;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.util.ResourceLocation;

public class SkillAttack extends Skill {
    public SkillAttack() {
        super(new ResourceLocation(MOD_ID, "attack"), new ResourceLocation("textures/blocks/stonebrick.png"));
        this.skillConfig.setLevelCap(100);
    }
}