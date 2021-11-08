package codersafterdark.reskillable.client.gui;

import static codersafterdark.reskillable.client.core.RenderHelper.renderTooltip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerProfessionInfo;
import codersafterdark.reskillable.api.data.PlayerTalentInfo;
import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.client.gui.button.GuiButtonLevelUp;
import codersafterdark.reskillable.client.gui.handler.InventoryTabHandler;
import codersafterdark.reskillable.client.gui.handler.KeyBindings;
import codersafterdark.reskillable.common.lib.LibMisc;
import codersafterdark.reskillable.common.network.MessageLevelUpProfession;
import codersafterdark.reskillable.common.network.MessageUnlockTalent;
import codersafterdark.reskillable.common.network.MessageUpgradeTalent;
import codersafterdark.reskillable.common.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class GuiProfessionInfo extends GuiScreen {

    public static final ResourceLocation PROFESSION_INFO_RES = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/profession_info1.png");
    public static final ResourceLocation PROFESSION_INFO_RES2 = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/profession_info1b.png");
    public static final ResourceLocation PROFESSION_INFO_RES3 = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/profession_info2.png");

    private final Profession profession;

    private int guiWidth, guiHeight;
    private ResourceLocation sprite;

    private GuiButtonLevelUp levelUpButton;
    private Talent hoveredTalent;
    private boolean hoveredLevelButton;
    @SuppressWarnings("unused")
	private boolean hoveredSwapButton;
    private boolean canPurchase;
    private boolean canUpgrade;
    private int color;
    @SuppressWarnings("unused")
	private int professionIndex;

    public GuiProfessionInfo(Profession profession) {this.profession = profession;}

    /** Called to load the basic GUI parameters */
    @Override
    public void initGui() {
        this.guiWidth = 256;
        this.guiHeight = 235;

        int left = this.width / 2 - this.guiWidth / 2;
        int top = this.height / 2 - this.guiHeight / 2;

        this.buttonList.clear();
        if (ConfigHandler.enableLevelUp && this.profession.hasLevelButton()) {
            this.buttonList.add(this.levelUpButton = new GuiButtonLevelUp(left - 32, top + 196));
        }

        InventoryTabHandler.addTabs(this, this.buttonList);
        this.sprite = this.profession.getBackground();
    }

    /** Called when the screen is drawn */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Dims the background
        drawDefaultBackground();

        // Defines the top-left anchor for the GUI; dependent on current screen size
        int left = this.width / 2 - this.guiWidth / 2;
        int top = this.height / 2 - this.guiHeight / 2;

        PlayerData data = PlayerDataHandler.get(this.mc.player);
        PlayerProfessionInfo professionInfo = data.getProfessionInfo(this.profession);

        // Draws the iterative background fill for the tree windows
        this.mc.renderEngine.bindTexture(this.sprite);
        GlStateManager.color(0.5F, 0.5F, 0.5F);
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 12; j++) {
                drawTexturedRec(left - 9 + i * 16, top + 6 + j * 16, 16, 16);
            }
        }

        // Draws the background plate and window borders in pieces, centering and fitting the unorthodox GUI width
        GlStateManager.color(1F, 1F, 1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        if (ConfigHandler.enableLevelUp & this.profession.hasLevelButton()) {
            this.mc.renderEngine.bindTexture(PROFESSION_INFO_RES);
            drawTexturedModalRect(left - 42, top, 0, 0, this.guiWidth, this.guiHeight);
        } else {            // The left side of the GUI is drawn with a texture without the level button, if appropriate
            this.mc.renderEngine.bindTexture(PROFESSION_INFO_RES2);
            drawTexturedModalRect(left - 42, top, 0, 0, this.guiWidth, this.guiHeight);
        }
        this.mc.renderEngine.bindTexture(PROFESSION_INFO_RES3);  // The right side of the GUI is drawn and offset the appropriate amount
        drawTexturedModalRect(left + 214, top, 0, 0, this.guiWidth - 190, this.guiHeight);

        // Draw the Skill Bar Fill
        float barUnit = 183.0f / this.profession.getCap();
        int barHeight = (int)(barUnit * professionInfo.getLevel());
        this.color = this.profession.getColor() == 0 ? 13619151 : this.profession.getColor();
        drawTexturedColoredRect(left - 33, top + 192 - barHeight, 66, 233 - barHeight, 16, barHeight, this.color);

        // Draw the bottom-left Profession icon
        GuiProfessions.drawProfession(left - 33, top + 212, this.profession);

        // Draw the information strings
        //String levelStr = String.format("%d/%d [ %s ]", professionInfo.getLevel(), profession.getCap(), new TextComponentTranslation("reskillable.rank." + professionInfo.getRank()).getUnformattedComponentText());
        String levelStr = String.format("%s %d/%d", new TextComponentTranslation("reskillable.misc.level").getUnformattedComponentText(), professionInfo.getLevel(), this.profession.getCap());
        drawCenteredString(this.mc.fontRenderer,TextFormatting.BOLD + this.profession.getName(), left + 128, top + 216, 15921906);
        this.mc.fontRenderer.drawString(levelStr, left -10 , top + 216, 4210752);
        this.mc.fontRenderer.drawString(new TextComponentTranslation("reskillable.misc.skill_points", professionInfo.getProfessionPoints()).getUnformattedComponentText(), left + 192, top + 216, 4210752);

        // Draw the sub profession names
        List<Profession.SubProfession> subProfessions = this.profession.getAllSubProfessions();
        for (int i = 0; i < subProfessions.toArray().length; i++) {
            if (subProfessions.get(i) != null) {
                int guiIndex = subProfessions.get(i).getGuiIndex();
                drawCenteredString(this.mc.fontRenderer, new TextComponentTranslation(subProfessions.get(i).getRegistryName()).getUnformattedComponentText(), left + 35 + guiIndex * 93, top + 199, 15921906);
            }
        }

        // Draw a tooltip when the level-up button is hovered
        int cost = professionInfo.getLevelUpCost();
        if (ConfigHandler.enableLevelUp && this.profession.hasLevelButton()) {
            drawButtonTooltip(professionInfo, mouseX, mouseY);
            this.levelUpButton.setCost(cost);
        }

        //Draw the talents
        this.hoveredTalent = null;
        this.profession.getTalents().forEach(t -> drawTalent(professionInfo, t, mouseX, mouseY));

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.hoveredTalent != null) {
            makeTalentTooltip(data, professionInfo, mouseX, mouseY);
        }
    }

    /** Used to draw the iterative background fill */
    public void drawTexturedRec(int x, int y, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, this.zLevel).tex(0, 1).endVertex();
        bufferbuilder.pos(x + width, y + height, this.zLevel).tex(1, 1).endVertex();
        bufferbuilder.pos(x + width, y, this.zLevel).tex(1, 0).endVertex();
        bufferbuilder.pos(x, y, this.zLevel).tex(0, 0).endVertex();
        tessellator.draw();
    }

    /** Draws a colored overlay, used to provide color to the skill bar */
    private void drawTexturedColoredRect(int x, int y, int textureX, int textureY, int width, int height, int color) {
        float a = ((color >> 24) & 255) / 255f;
        if (a <= 0f)
            a = 1f;
        float r = ((color >> 16) & 255) / 255f;
        float g = ((color >> 8) & 255) / 255f;
        float b = (color & 255) / 255f;
        GlStateManager.color(r, g, b, a);
        drawTexturedModalRect(x, y, textureX, textureY, width, height);
        GlStateManager.color(1f, 1f, 1f, 1f);
    }

    private void drawTalent(PlayerProfessionInfo info, Talent talent, int mx, int my) {
        PlayerTalentInfo talentInfo = PlayerDataHandler.get(Minecraft.getMinecraft().player).getTalentInfo(talent);

        int x = this.width / 2 - this.guiWidth / 2 - 5 + 93 * talent.getSubProfession().getGuiIndex() + talent.getX() * 27;
        int y = this.height / 2 - this.guiHeight / 2 + 10 + talent.getY() * 37;
        this.mc.renderEngine.bindTexture(PROFESSION_INFO_RES3);
        boolean unlocked = info.isUnlocked(talent);

        int u = 84;
        int v = 133;

        /*
        if (talent.hasSpikes()) {
            u += 26;
        }
        */

        // Unlocked, but cannot purchase
        if (unlocked) {
            if (talentInfo.isCapped()) {
                v += 22 * 3;
            } else if (info.getProfessionPoints() >= talent.getCost()) {
                v += 22 * 2;
            } else if (info.getProfessionPoints() < talent.getCost()) {
                v += 22;
            }
        }

        GlStateManager.color(1F, 1F, 1F);
        drawTexturedModalRect(x, y, u, v, 22, 22);

        this.mc.renderEngine.bindTexture(talent.getIcon());
        drawModalRectWithCustomSizedTexture(x + 1, y + 1, 0, 0, 20, 20, 20, 20);

        if (mx >= x && my >= y && mx < x + 24 && my < y + 24) {
            this.canPurchase = !unlocked && info.getProfessionPoints() >= talent.getCost();
            this.canUpgrade = !talentInfo.isCapped() && info.getProfessionPoints() >= talent.getCost();
            this.hoveredTalent = talent;
        }
    }

    /** Draws the Talent tooltip */
    private void makeTalentTooltip(PlayerData data, PlayerProfessionInfo info, int mouseX, int mouseY) {
        List<String> tooltip = new ArrayList<>();
        TextFormatting tf = this.hoveredTalent.hasSpikes() ? TextFormatting.AQUA : TextFormatting.YELLOW;

        tooltip.add(tf + this.hoveredTalent.getName());

        if (isShiftKeyDown()) {
            addLongStringToTooltip(tooltip, this.hoveredTalent.getDescription(), this.guiWidth);
        } else {
            tooltip.add(TextFormatting.GRAY + new TextComponentTranslation("reskillable.misc.shift").getUnformattedComponentText());
            tooltip.add("");
        }

        if (!info.isUnlocked(this.hoveredTalent)) {
            this.hoveredTalent.getRequirements().addRequirementsToTooltip(data, tooltip);
        } else {
            tooltip.add(TextFormatting.GREEN + new TextComponentTranslation("reskillable.misc.unlocked").getUnformattedComponentText());
        }

        int rank = data.getTalentInfo(this.hoveredTalent).getRank();

        tooltip.add(TextFormatting.GRAY + new TextComponentTranslation("reskillable.misc.skill_points", this.hoveredTalent.getCost()).getUnformattedComponentText());
        tooltip.add(TextFormatting.GRAY + new TextComponentTranslation("reskillable.misc.talent_rank", rank, this.hoveredTalent.getCap()).getUnformattedComponentText());

        renderTooltip(mouseX, mouseY, tooltip);
    }

    private void addLongStringToTooltip(List<String> tooltip, String longStr, int maxLen) {
        String[] tokens = longStr.split(" ");
        String curr = TextFormatting.GRAY.toString();
        int i = 0;

        while (i < tokens.length) {
            while (this.fontRenderer.getStringWidth(curr) < maxLen && i < tokens.length) {
                curr = curr + tokens[i] + ' ';
                i++;
            }
            tooltip.add(curr);
            curr = TextFormatting.GRAY.toString();
        }
        tooltip.add(curr);
    }

    /** Draws the tooltip of the level-up button, displaying the cost to level up */
    private void drawButtonTooltip(PlayerProfessionInfo info, int mx, int my) {
        int x = this.width / 2 - this.guiWidth / 2 - 32;
        int y = this.height / 2 - this.guiHeight / 2 + 196;

        if (mx >= x && my >= y && mx <= x + 16 && my < y + 16) {
            this.hoveredLevelButton = true;
        } else {
            this.hoveredLevelButton = false;
        }

        String costStr = String.format("%s %d %s", new TextComponentTranslation("reskillable.misc.cost").getUnformattedComponentText(), info.getLevelUpCost(), new TextComponentTranslation("reskillable.misc.levels").getUnformattedComponentText());
        String desc;

        if (info.getLevel() < 1) {
            desc = new TextComponentTranslation("reskillable.misc.profession_button", this.profession.getName()).getUnformattedComponentText();
        } else {
            desc = null;
        }

        if (info.isCapped()) {
            costStr = new TextComponentTranslation("reskillable.misc.capped").getUnformattedComponentText();
        }

        GlStateManager.color(1F, 1F, 1F);
        if (this.hoveredLevelButton) {
            this.levelUpButton.drawLevelButtonTooltip(desc, costStr, mx, my);
        }
    }

    /** Called when the level-up button is pressed, sending a packet to the server containing the level information */
    @Override
    protected void actionPerformed(GuiButton button) {
        if (ConfigHandler.enableLevelUp && this.profession.hasLevelButton() && button == this.levelUpButton) {
            MessageLevelUpProfession message = new MessageLevelUpProfession(this.profession.getRegistryName());
            PacketHandler.INSTANCE.sendToServer(message);
        }
    }

    /** Called when the mouse is clicked to supply sounds, detect when a talent is unlocked, and return to the previous menu */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0 && this.hoveredTalent != null && this.canPurchase) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            MessageUnlockTalent message = new MessageUnlockTalent(this.profession.getRegistryName(), this.hoveredTalent.getRegistryName());
            PacketHandler.INSTANCE.sendToServer(message);
        } else if (mouseButton == 0 && this.hoveredTalent != null && this.canUpgrade) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            MessageUpgradeTalent message = new MessageUpgradeTalent(this.profession.getRegistryName(), this.hoveredTalent.getRegistryName());
            PacketHandler.INSTANCE.sendToServer(message);
        } else if (mouseButton == 1 || mouseButton == 3) {
            this.mc.displayGuiScreen(new GuiProfessions());
        }
    }

    /** Called when either the profession keybind or RM2 are pressed */
    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);

            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        } else if (keyCode == KeyBindings.keyProfessionGui.getKeyCode() || keyCode == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen != null) {
                this.mc.setIngameFocus();
            }
        }
    }

    /** Opening the Profession Info GUI does not pause the game */
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
