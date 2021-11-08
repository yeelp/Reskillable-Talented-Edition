package codersafterdark.reskillable.api.requirement;

import java.util.Optional;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.RequirementHolder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class AdvancementRequirement extends Requirement {
    private ResourceLocation advancementName;

    public AdvancementRequirement(ResourceLocation advancementName) {
        this.advancementName = advancementName;

    }

    @Override
    public boolean achievedByPlayer(EntityPlayer entityPlayer) {
        return Optional.ofNullable(this.getAdvancement())
                .map(advancement -> ReskillableAPI.getInstance().getAdvancementProgress(entityPlayer, advancement))
                .map(AdvancementProgress::isDone)
                .orElse(false);
    }

    @Override
    public String getToolTip(PlayerData data) {
        if (this.tooltip.isEmpty()) {
            Advancement adv = getAdvancement();
            this.tooltip = TextFormatting.GRAY + " - " + TextFormatting.GOLD + new TextComponentTranslation("reskillable.requirements.format.advancement",
                    "%S", adv == null ? "" : adv.getDisplayText().getUnformattedText().replaceAll("[\\[\\]]", "")).getUnformattedComponentText();
        }
        return super.getToolTip(data);
    }

    public Advancement getAdvancement() {
        return RequirementHolder.getAdvancementList().getAdvancement(this.advancementName);
    }

    @Override
    public RequirementComparison matches(Requirement other) {
        return other instanceof AdvancementRequirement && this.advancementName.equals(((AdvancementRequirement) other).advancementName)
                ? RequirementComparison.EQUAL_TO : RequirementComparison.NOT_EQUAL;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof AdvancementRequirement && this.advancementName.equals(((AdvancementRequirement) o).advancementName);
    }

    @Override
    public int hashCode() {
        return this.advancementName.hashCode();
    }
}