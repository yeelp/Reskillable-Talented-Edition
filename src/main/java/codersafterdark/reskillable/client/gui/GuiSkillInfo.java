package codersafterdark.reskillable.client.gui;

import static codersafterdark.reskillable.client.core.RenderHelper.renderTooltip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.client.gui.button.GuiButtonLevelUp;
import codersafterdark.reskillable.client.gui.handler.InventoryTabHandler;
import codersafterdark.reskillable.client.gui.handler.KeyBindings;
import codersafterdark.reskillable.common.lib.LibMisc;
import codersafterdark.reskillable.common.network.MessageLevelUp;
import codersafterdark.reskillable.common.network.MessageUnlockUnlockable;
import codersafterdark.reskillable.common.network.MessageUpgradeUnlockable;
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

public class GuiSkillInfo extends GuiScreen {
    public static final ResourceLocation SKILL_INFO_RES = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/skill_info.png");
    public static final ResourceLocation SKILL_INFO_RES2 = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/skill_info2.png");

    private final Skill skill;

    private int guiWidth, guiHeight;
    private ResourceLocation sprite;

    private GuiButtonLevelUp levelUpButton;
    private Unlockable hoveredUnlockable;
    private boolean canPurchase;
    private boolean canUpgrade;

    public GuiSkillInfo(Skill skill) {
        this.skill = skill;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);

            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        } else if (keyCode == KeyBindings.openGUI.getKeyCode() || keyCode == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen != null) {
                this.mc.setIngameFocus();
            }
        }
    }

    @Override
    public void initGui() {
        this.guiWidth = 176;
        this.guiHeight = 166;

        int left = this.width / 2 - this.guiWidth / 2;
        int top = this.height / 2 - this.guiHeight / 2;

        this.buttonList.clear();
        if (ConfigHandler.enableLevelUp && this.skill.hasLevelButton()) {
            this.buttonList.add(this.levelUpButton = new GuiButtonLevelUp(left + 147, top + 10));
        }
        InventoryTabHandler.addTabs(this, this.buttonList);
        this.sprite = this.skill.getBackground();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        int left = this.width / 2 - this.guiWidth / 2;
        int top = this.height / 2 - this.guiHeight / 2;

        PlayerData data = PlayerDataHandler.get(this.mc.player);
        PlayerSkillInfo skillInfo = data.getSkillInfo(this.skill);

        this.mc.renderEngine.bindTexture(this.sprite);
        GlStateManager.color(0.5F, 0.5F, 0.5F);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 8; j++) {
                drawTexturedRec(left + 16 + i * 16, top + 33 + j * 16, 16, 16);
            }
        }

        GlStateManager.color(1F, 1F, 1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        if (ConfigHandler.enableLevelUp && this.skill.hasLevelButton()) {
            this.mc.renderEngine.bindTexture(SKILL_INFO_RES);
        } else {
            this.mc.renderEngine.bindTexture(SKILL_INFO_RES2);
        }

        drawTexturedModalRect(left, top, 0, 0, this.guiWidth, this.guiHeight);

        GuiSkills.drawSkill(left + 4, top + 9, this.skill);

        String levelStr = String.format("%d/%d [ %s ]", skillInfo.getLevel(), this.skill.getCap(), new TextComponentTranslation("reskillable.rank." + skillInfo.getRank()).getUnformattedComponentText());
        this.mc.fontRenderer.drawString(TextFormatting.BOLD + this.skill.getName(), left + 22, top + 8, 4210752);
        this.mc.fontRenderer.drawString(levelStr, left + 22, top + 18, 4210752);

        this.mc.fontRenderer.drawString(new TextComponentTranslation("reskillable.misc.skill_points", skillInfo.getSkillPoints()).getUnformattedComponentText(), left + 15, top + 154, 4210752);

        int cost = skillInfo.getLevelUpCost();
        String costStr = Integer.toString(cost);
        if (skillInfo.isCapped()) {
            costStr = new TextComponentTranslation("reskillable.misc.capped").getUnformattedComponentText();
        }

        if (ConfigHandler.enableLevelUp && this.skill.hasLevelButton()) {
            drawCenteredString(this.mc.fontRenderer, costStr, left + 138, top + 13, 0xAFFF02);
            this.levelUpButton.setCost(cost);
        }

        this.hoveredUnlockable = null;
        this.skill.getUnlockables().forEach(u -> drawUnlockable(data, skillInfo, u, mouseX, mouseY));
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.hoveredUnlockable != null) {
            makeUnlockableTooltip(data, skillInfo, mouseX, mouseY);
        }
    }

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

    private void drawUnlockable(@SuppressWarnings("unused") PlayerData data, PlayerSkillInfo info, Unlockable unlockable, int mx, int my) {
        PlayerSkillInfo unlockableInfo = PlayerDataHandler.get(Minecraft.getMinecraft().player).getUnlockableInfo(unlockable);

        int x = this.width / 2 - this.guiWidth / 2 + 20 + unlockable.getX() * 28;
        int y = this.height / 2 - this.guiHeight / 2 + 37 + unlockable.getY() * 28;
        this.mc.renderEngine.bindTexture(SKILL_INFO_RES);
        boolean unlocked = info.isUnlocked(unlockable);

        int u = 0;
        int v = this.guiHeight;
        if (unlockable.hasSpikes()) {
            u += 26;
        }
        if (unlocked) {
            v += 26;
        }

        GlStateManager.color(1F, 1F, 1F);
        drawTexturedModalRect(x, y, u, v, 26, 26);

        this.mc.renderEngine.bindTexture(unlockable.getIcon());
        drawModalRectWithCustomSizedTexture(x + 5, y + 5, 0, 0, 16, 16, 16, 16);

        if (mx >= x && my >= y && mx < x + 26 && my < y + 26) {
            this.canPurchase = !unlocked && info.getSkillPoints() >= unlockable.getCost();
            this.canUpgrade = !unlockableInfo.isCapped() && info.getSkillPoints() >= unlockable.getCost();
            this.hoveredUnlockable = unlockable;
        }
    }

    private void makeUnlockableTooltip(PlayerData data, PlayerSkillInfo info, int mouseX, int mouseY) {
        List<String> tooltip = new ArrayList<>();
        TextFormatting tf = this.hoveredUnlockable.hasSpikes() ? TextFormatting.AQUA : TextFormatting.YELLOW;

        tooltip.add(tf + this.hoveredUnlockable.getName());

        if (isShiftKeyDown()) {
            addLongStringToTooltip(tooltip, this.hoveredUnlockable.getDescription(), this.guiWidth);
        } else {
            tooltip.add(TextFormatting.GRAY + new TextComponentTranslation("reskillable.misc.shift").getUnformattedComponentText());
            tooltip.add("");
        }

        if (!info.isUnlocked(this.hoveredUnlockable)) {
            this.hoveredUnlockable.getRequirements().addRequirementsToTooltip(data, tooltip);
        } else {
            tooltip.add(TextFormatting.GREEN + new TextComponentTranslation("reskillable.misc.unlocked").getUnformattedComponentText());
        }

        int rank = data.getUnlockableInfo(this.hoveredUnlockable).getRank();

        tooltip.add(TextFormatting.GRAY + new TextComponentTranslation("reskillable.misc.skill_points", this.hoveredUnlockable.getCost()).getUnformattedComponentText());
        tooltip.add(TextFormatting.GRAY + new TextComponentTranslation("reskillable.misc.talent_rank", rank, this.hoveredUnlockable.getCap()).getUnformattedComponentText());

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

    @Override
    protected void actionPerformed(GuiButton button) {
        if (ConfigHandler.enableLevelUp && this.skill.hasLevelButton() && button == this.levelUpButton) {
            MessageLevelUp message = new MessageLevelUp(this.skill.getRegistryName());
            PacketHandler.INSTANCE.sendToServer(message);
        }
    }

    /** Called when the mouse is clicked to supply sounds, detect when a talent is unlocked, and return to the previous menu */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0 && this.hoveredUnlockable != null && this.canPurchase) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            MessageUnlockUnlockable message = new MessageUnlockUnlockable(this.skill.getRegistryName(), this.hoveredUnlockable.getRegistryName());
            PacketHandler.INSTANCE.sendToServer(message);
        } else if (mouseButton == 0 && this.hoveredUnlockable != null && this.canUpgrade) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            MessageUpgradeUnlockable message = new MessageUpgradeUnlockable(this.skill.getRegistryName(), this.hoveredUnlockable.getRegistryName());
            PacketHandler.INSTANCE.sendToServer(message);
        } else if (mouseButton == 1 || mouseButton == 3) {
            this.mc.displayGuiScreen(new GuiSkills());
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}