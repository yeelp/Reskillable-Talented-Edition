package codersafterdark.reskillable.api.toast;

import org.apache.commons.lang3.tuple.Pair;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.client.core.RenderHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SkillToast extends AbstractToast {
    private final Skill skill;
    private final int rank;

    public SkillToast(Skill skill, int level) {
        super(skill.getName(), new TextComponentTranslation("reskillable.toast.skill_desc", level).getUnformattedComponentText());
        this.skill = skill;
        this.rank = this.skill.getRank(level);
    }

    @Override
    protected void renderImage(GuiToast guiToast) {
        if (this.skill.hasCustomSprites()) {
            ResourceLocation sprite = this.skill.getSpriteLocation(this.rank);
            if (sprite != null) {
                bindImage(guiToast, sprite);
                Gui.drawModalRectWithCustomSizedTexture(this.x, this.y, 0, 0, 16, 16, 16, 16);
            }
        } else {
            bindImage(guiToast, this.skill.getSpriteLocation());
            Pair<Integer, Integer> pair = this.skill.getSpriteFromRank(this.rank);
            RenderHelper.drawTexturedModalRect(this.x, this.y, 1, pair.getKey(), pair.getValue(), 16, 16, 1f / 64, 1f / 64);
        }
    }

    @Override
    protected boolean hasImage() {
        return !this.skill.hasCustomSprites() || this.skill.getSpriteLocation(this.rank) != null;
    }
}