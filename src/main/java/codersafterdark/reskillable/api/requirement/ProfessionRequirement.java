package codersafterdark.reskillable.api.requirement;

import java.util.Objects;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.profession.Profession;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class ProfessionRequirement extends Requirement {
    private final Profession profession;
    private final int level;

    public ProfessionRequirement(Profession profession, int level) {
        this.profession = profession;
        this.level = level;
        this.tooltip = TextFormatting.GRAY + " - " + new TextComponentTranslation("reskillable.requirements.format.profession", TextFormatting.DARK_AQUA, profession.getName(),
                "%s", level).getUnformattedComponentText();
    }

    @Override
    public boolean achievedByPlayer(EntityPlayer entityPlayer) {
        return PlayerDataHandler.get(entityPlayer).getProfessionInfo(this.profession).getLevel() >= this.level;
    }

    public Profession getProfession() {
        return this.profession;
    }

    public int getLevel() {
        return this.level;
    }

    @Override
    public RequirementComparison matches(Requirement other) {
        if (other instanceof ProfessionRequirement) {
            ProfessionRequirement professionRequirement = (ProfessionRequirement) other;
            if (this.profession == null || professionRequirement.profession == null) {
                //If they are both invalid don't bother checking the level.
                return RequirementComparison.NOT_EQUAL;
            }
            if (this.profession.getKey().equals(professionRequirement.profession.getKey())) {
                if (this.level == professionRequirement.level) {
                    return RequirementComparison.EQUAL_TO;
                }
                return this.level > professionRequirement.level ? RequirementComparison.GREATER_THAN : RequirementComparison.LESS_THAN;
            }
        }
        return RequirementComparison.NOT_EQUAL;
    }

    @Override
    public boolean isEnabled() {
        return this.profession != null && this.profession.isEnabled();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ProfessionRequirement) {
            ProfessionRequirement pReq = (ProfessionRequirement) o;
            return this.profession.equals(pReq.profession) && this.level == pReq.level;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.profession, this.level);
    }

}
