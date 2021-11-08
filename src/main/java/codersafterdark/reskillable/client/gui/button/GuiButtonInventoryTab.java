package codersafterdark.reskillable.client.gui.button;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.client.gui.GuiSkills;
import codersafterdark.reskillable.client.gui.handler.InventoryTabHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiButtonInventoryTab extends GuiButton {
    public final TabType type;
    private final Predicate<GuiScreen> selectedPred;

    public GuiButtonInventoryTab(int id, int x, int y, TabType type, Predicate<GuiScreen> selectedPred) {
        super(id, x, y, 32, 28, "");
        this.type = type;
        this.selectedPred = selectedPred;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float f) {
        this.enabled = this.type.shouldRender() && !mc.player.getRecipeBook().isGuiOpen();

        GuiScreen curr = mc.currentScreen;
        if (curr instanceof GuiContainerCreative && ((GuiContainerCreative) curr).getSelectedTabIndex() != CreativeTabs.INVENTORY.getIndex()) {
            this.enabled = false;
        }

        if (this.enabled) {
            GlStateManager.color(1F, 1F, 1F);
            mc.renderEngine.bindTexture(GuiSkills.SKILLS_RES);

            int x = this.x;
            int y = this.y;
            int u = 176;
            int v = 0;
            int w = this.width;
            int h = this.height;

            if (isSelected()) {
                x += 4;
                u += w;
            }

            drawTexturedModalRect(x, y, u, v, w, h);
            drawTexturedModalRect(this.x + 12, y + 6, 176 + this.type.iconIndex * 16, 28, 16, 16);

            if (mouseX > this.x && mouseY > this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) {
                InventoryTabHandler.tooltip = new TextComponentTranslation("reskillable.tab." + this.type.name().toLowerCase()).getUnformattedComponentText();
                InventoryTabHandler.mx = mouseX;
                InventoryTabHandler.my = mouseY;
            }
        }
    }

    public boolean isSelected() {
        return this.selectedPred.test(Minecraft.getMinecraft().currentScreen);
    }

    public enum TabType {
        PROFESSIONS(2, null),
        INVENTORY(1, null),
        SKILLS(0, null);

        public final int iconIndex;
        private Predicate<PlayerData> renderPred;

        TabType(int iconIndex, Predicate<PlayerData> render) {
            this.iconIndex = iconIndex;
            this.renderPred = render;
            if (this.renderPred == null) {
                this.renderPred = data -> true;
            }
        }

        public boolean shouldRender() {
            PlayerData data = PlayerDataHandler.get(Minecraft.getMinecraft().player);
            return data != null && this.renderPred.test(data);
        }
    }
}