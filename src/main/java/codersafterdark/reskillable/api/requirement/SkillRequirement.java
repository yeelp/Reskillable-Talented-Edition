package codersafterdark.reskillable.api.requirement;

import java.util.Objects;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class SkillRequirement extends Requirement {
    private final Skill skill;
    private final int level;

    public SkillRequirement(Skill skill, int level) {
        this.skill = skill;
        this.level = level;
        this.tooltip = TextFormatting.GRAY + " - " + new TextComponentTranslation("reskillable.requirements.format.skill", TextFormatting.DARK_AQUA, skill.getName(),
                "%s", level).getUnformattedComponentText();
    }

    @Override
    public boolean achievedByPlayer(EntityPlayer entityPlayer) {
        return PlayerDataHandler.get(entityPlayer).getSkillInfo(this.skill).getLevel() >= this.level;
    }

    public Skill getSkill() {
        return this.skill;
    }

    public int getLevel() {
        return this.level;
    }

    @Override
    public RequirementComparison matches(Requirement other) {
        if (other instanceof SkillRequirement) {
            SkillRequirement skillRequirement = (SkillRequirement) other;
            if (this.skill == null || skillRequirement.skill == null) {
                //If they are both invalid don't bother checking the level.
                return RequirementComparison.NOT_EQUAL;
            }
            if (this.skill.getKey().equals(skillRequirement.skill.getKey())) {
                if (this.level == skillRequirement.level) {
                    return RequirementComparison.EQUAL_TO;
                }
                return this.level > skillRequirement.level ? RequirementComparison.GREATER_THAN : RequirementComparison.LESS_THAN;
            }
        }
        return RequirementComparison.NOT_EQUAL;
    }

    @Override
    public boolean isEnabled() {
        return this.skill != null && this.skill.isEnabled();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof SkillRequirement) {
            SkillRequirement sReq = (SkillRequirement) o;
            return this.skill.equals(sReq.skill) && this.level == sReq.level;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.skill, this.level);
    }
}