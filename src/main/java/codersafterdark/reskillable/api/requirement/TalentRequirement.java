package codersafterdark.reskillable.api.requirement;

import java.util.Objects;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class TalentRequirement extends Requirement {
    private Talent talent;

    public TalentRequirement(Talent talent) {
        this.talent = talent;
        this.tooltip = TextFormatting.GRAY + " - " + TextFormatting.LIGHT_PURPLE + new TextComponentTranslation("reskillable.requirements.format.talent", "%s",
                this.talent.getName()).getUnformattedComponentText();
    }

    @Override
    public boolean achievedByPlayer(EntityPlayer entityPlayer) {
        return PlayerDataHandler.get(entityPlayer).getProfessionInfo(this.talent.getParentProfession()).isUnlocked(this.talent);
    }

    public Profession getProfession() {
        return this.talent.getParentProfession();
    }

    public Talent getTalent() {
        return this.talent;
    }

    @Override
    public RequirementComparison matches(Requirement other) {
        return other instanceof TalentRequirement ? this.talent.getKey().equals(((TalentRequirement) other).talent.getKey()) ?
                RequirementComparison.EQUAL_TO : RequirementComparison.NOT_EQUAL : RequirementComparison.NOT_EQUAL;
    }

    @Override
    public boolean isEnabled() {
        return this.talent != null && this.talent.isEnabled();
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof TalentRequirement && this.talent.equals(((TalentRequirement) o).talent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.talent);
    }

}
