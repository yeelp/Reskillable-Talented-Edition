package codersafterdark.reskillable.client.gui.compat;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.lib.LibMisc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

public class SeasonsOverlay extends Gui {

    public static SeasonsOverlay instance = new SeasonsOverlay();
    private static final ResourceLocation TEXTURE = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/compat/seasons_hud.png");
    private static final int glyphWidth = 64;
    private static final int glyphHeight = 16;

    /*
    @SubscribeEvent
    public void renderGameOverlayEvent(RenderGameOverlayEvent event) {
        if (event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            return;
        }
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (PlayerDataHandler.get(player).getSkillInfo(LibSkills.farming).isUnlocked(LibSkills.traitSeasons) & TalentedConfig.traits.farming.enableSeasonHUD) {

            Season season = SeasonHelper.getSeasonState(player.world).getSeason();
            Season.SubSeason subSeason = SeasonHelper.getSeasonState(player.world).getSubSeason();
            GlStateManager.disableLighting();
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            Minecraft mc = Minecraft.getMinecraft();
            mc.renderEngine.bindTexture(TEXTURE);

            int x = TalentedConfig.traits.farming.seasonsHUDx;
            int y = TalentedConfig.traits.farming.seasonsHUDy + 26;

            if (season == Season.SPRING) {
                drawTexturedModalRect(TalentedConfig.traits.farming.seasonsHUDx, TalentedConfig.traits.farming.seasonsHUDy, 0, 16, 22, 22);
                drawTexturedModalRect(TalentedConfig.traits.farming.seasonsHUDx + 3, TalentedConfig.traits.farming.seasonsHUDy + 3, 0, 0, 16, glyphHeight);
            }

            if (season == Season.SUMMER) {
                drawTexturedModalRect(TalentedConfig.traits.farming.seasonsHUDx, TalentedConfig.traits.farming.seasonsHUDy, 0, 16, 22, 22);
                drawTexturedModalRect(TalentedConfig.traits.farming.seasonsHUDx + 3, TalentedConfig.traits.farming.seasonsHUDy + 3, 16, 0, 16, glyphHeight);
            }

            if (season == Season.AUTUMN) {
                drawTexturedModalRect(TalentedConfig.traits.farming.seasonsHUDx, TalentedConfig.traits.farming.seasonsHUDy, 0, 16, 22, 22);
                drawTexturedModalRect(TalentedConfig.traits.farming.seasonsHUDx + 3, TalentedConfig.traits.farming.seasonsHUDy + 3, 32, 0, 16, glyphHeight);
            }

            if (season == Season.WINTER) {
                drawTexturedModalRect(TalentedConfig.traits.farming.seasonsHUDx, TalentedConfig.traits.farming.seasonsHUDy, 0, 16, 22, 22);
                drawTexturedModalRect(TalentedConfig.traits.farming.seasonsHUDx + 3, TalentedConfig.traits.farming.seasonsHUDy + 3, 48, 0, 16, glyphHeight);
            }

            x = fontRenderer.drawString("" + new TextComponentTranslation(localizeSubSeason(subSeason)).getUnformattedComponentText(), x, y, colorizeSeasonText(subSeason));
        }
    }

    public String localizeSubSeason(Season.SubSeason subSeason) {
        switch (subSeason) {
            case EARLY_SPRING: return "talented.hud.early_spring";
            case MID_SPRING: return "talented.hud.mid_spring";
            case LATE_SPRING: return "talented.hud.late_spring";
            case EARLY_SUMMER: return "talented.hud.early_summer";
            case MID_SUMMER: return "talented.hud.mid_summer";
            case LATE_SUMMER: return "talented.hud.late_summer";
            case EARLY_AUTUMN: return "talented.hud.early_autumn";
            case MID_AUTUMN: return "talented.hud.mid_autumn";
            case LATE_AUTUMN: return "talented.hud.late_autumn";
            case EARLY_WINTER: return "talented.hud.early_winter";
            case MID_WINTER: return "talented.hud.mid_winter";
            case LATE_WINTER: return "talented.hud.late_winter";
            default: return "talented.hud.invalid_subseason";
        }
    }

    public int colorizeSeasonText(Season.SubSeason subSeason) {
        switch (subSeason) {
            case EARLY_SPRING:
            case MID_SPRING:
            case LATE_SPRING:
                return 0xDE5479;
            case EARLY_SUMMER:
            case MID_SUMMER:
            case LATE_SUMMER:
                return 0xF4D35E;
            case EARLY_AUTUMN:
            case MID_AUTUMN:
            case LATE_AUTUMN:
                return 0xEE964B;
            case EARLY_WINTER:
            case MID_WINTER:
            case LATE_WINTER:
                return 0x2A45CB;
            //return 0x549BDE;
            default: return 0xffffffff;
        }
    }
     */
}


