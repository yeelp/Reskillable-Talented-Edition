package codersafterdark.reskillable.api.toast;

import org.apache.commons.lang3.tuple.Pair;

import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.client.core.RenderHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class ProfessionToast extends AbstractToast{
    private final Profession profession;
    private final int rank;

    public ProfessionToast(Profession profession, int level) {
        super(profession.getName(), new TextComponentTranslation("reskillable.toast.profession_desc", level).getUnformattedComponentText());
        this.profession = profession;
        this.rank = this.profession.getRank(level);
    }

    @Override
    protected void renderImage(GuiToast guiToast) {
        if (this.profession.hasCustomSprites()) {
            ResourceLocation sprite = this.profession.getSpriteLocation(this.rank);
            if (sprite != null) {
                bindImage(guiToast, sprite);
                Gui.drawModalRectWithCustomSizedTexture(this.x, this.y, 0, 0, 16, 16, 16, 16);
            }
        } else {
            bindImage(guiToast, this.profession.getSpriteLocation());
            Pair<Integer, Integer> pair = this.profession.getSpriteFromRank(this.rank);
            RenderHelper.drawTexturedModalRect(this.x, this.y, 1, pair.getKey(), pair.getValue(), 16, 16, 1f / 64, 1f / 64);
        }
    }

    @Override
    protected boolean hasImage() {
        return !this.profession.hasCustomSprites() || this.profession.getSpriteLocation(this.rank) != null;
    }

}
