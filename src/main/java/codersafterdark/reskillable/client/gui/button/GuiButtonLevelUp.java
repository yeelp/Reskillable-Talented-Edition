package codersafterdark.reskillable.client.gui.button;

import static codersafterdark.reskillable.client.core.RenderHelper.renderTooltip;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.client.gui.GuiSkillInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

public class GuiButtonLevelUp extends GuiButton {
    int cost;
    float renderTicks;

    public GuiButtonLevelUp(int x, int y) {
        super(0, x, y, 14, 14, "");
        this.cost = Integer.MAX_VALUE;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float f) {
        this.enabled = mc.player.experienceLevel >= this.cost || mc.player.isCreative();

        if (ConfigHandler.enableLevelUp) {
            if (this.enabled) {
                GlStateManager.color(1F, 1F, 1F);
                mc.renderEngine.bindTexture(GuiSkillInfo.SKILL_INFO_RES);

                int x = this.x;
                int y = this.y;
                int u = 176;
                int v = 0;
                int w = this.width;
                int h = this.height;

                if (mouseX > this.x && mouseY > this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) {
                    v += h;
                } else {
                    float speedModifier = 4;
                    GlStateManager.color(1, 1, 1, (float) (Math.sin(mc.player.ticksExisted / speedModifier) + 1) / 2);
                }
                drawTexturedModalRect(x, y, u, v, w, h);
            }
        }
    }

    @SuppressWarnings("static-method")
	public void drawLevelButtonTooltip(String desc, String cost, int mouseX, int mouseY) {
        List<String> tooltip = new ArrayList<>();

        if (!(desc == null)) {tooltip.add(TextFormatting.YELLOW + desc);}
        tooltip.add(TextFormatting.GRAY + cost);

        renderTooltip(mouseX, mouseY, tooltip);
    }

}