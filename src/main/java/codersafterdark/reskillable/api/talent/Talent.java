package codersafterdark.reskillable.api.talent;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.Level;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.common.Reskillable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class Talent extends IForgeRegistryEntry.Impl<Talent> implements Comparable<Talent> {
    private final String name;
    protected TalentConfig talentConfig;
    private ResourceLocation icon;
    private Profession parentProfession;
    private Profession.SubProfession subProfession;

    public Talent(ResourceLocation name, int x, int y, ResourceLocation professionName, ResourceLocation subProfessionName, int cost, String... defaultRequirements) {
        this.name = name.toString().replace(":", ".");
        setRegistryName(name);
        this.talentConfig = ReskillableAPI.getInstance().getTalentConfig(name, x, y, cost, defaultRequirements);
        setParentProfession(professionName);
        setSubProfession(subProfessionName);
        setIcon(new ResourceLocation(name.getNamespace(), "textures/talents/" + getSubProfession().getUnformattedName() + "/" + name.getPath() + ".png"));
        //setIcon(new ResourceLocation(name.getNamespace(), "textures/talents/" + name.getPath() + ".png"));
    }

    public Profession.SubProfession getSubProfession() {
        return this.subProfession;
    }

    public void setSubProfession(ResourceLocation subProfession) {
        if (this.parentProfession != null && !this.parentProfession.getAllSubProfessions().isEmpty()) {
            Optional<Profession.SubProfession> subP = this.parentProfession.getAllSubProfessions().stream().filter(subprofession1 -> subprofession1.getUnformattedName().equals(subProfession.getPath())).findFirst();
            subP.ifPresent(profession -> this.subProfession = profession);
        }
    }

    @Nonnull
    public Profession getParentProfession() {
        return this.subProfession.getProfession();
    }

    protected void setParentProfession(ResourceLocation professionName) {
        if (this.parentProfession != null) {
            if (professionName != null && professionName.equals(this.parentProfession.getRegistryName())) {
                //The skill is already the parent profession
                return;
            }
            //Remove from old parent profession if there already is a parent profession
            this.parentProfession.getTalents().remove(this);
        }
        this.parentProfession = Objects.requireNonNull(ReskillableRegistries.PROFESSIONS.getValue(professionName));
        if (isEnabled()) {
            if (this.parentProfession.isEnabled()) {
                this.parentProfession.addTalent(this);
            } else {
                Reskillable.logger.log(Level.ERROR, getName() + " is enabled but the parent skill: " + this.parentProfession.getName() + " is disabled. Disabling: " + getName());
                this.talentConfig.setEnabled(false);
            }
        }
    }

    public RequirementHolder getRequirements() {
        return this.talentConfig.getRequirementHolder();
    }

    public String getKey() {
        return this.name;
    }

    public String getName() {
        return new TextComponentTranslation("reskillable.talent." + getKey()).getUnformattedComponentText();
    }

    public String getDescription() {
        return new TextComponentTranslation("reskillable.talent." + getKey() + ".desc").getUnformattedComponentText();
    }

    public void setCap(int cap) {this.talentConfig.setRankCap(cap);}

    public int getCap() {return this.talentConfig.getRankCap();}

    public ResourceLocation getIcon() {
        return this.icon;
    }

    protected void setIcon(ResourceLocation newIcon) {
        this.icon = newIcon;
    }

    /**
     * On unlock callback
     * @param player player unlocking talent
     */
    public void onUnlock(EntityPlayer player) {
    	//no-op
    }

    /**
     * on lock callback
     * @param player player locking talent
     */
    public void onLock(EntityPlayer player) {
    	//no-op
    }

    @SuppressWarnings("static-method")
	public boolean hasSpikes() {return false;}

    public boolean isEnabled() {return this.talentConfig.isEnabled();}

    @Override
    public int compareTo(@Nonnull Talent o) {
        int profCmp = getParentProfession().compareTo(o.getParentProfession());
        if (profCmp == 0) {
            return getName().compareTo(o.getName());
        }
        return profCmp;
    }

    public int getCost() {
        return this.talentConfig.getCost();
    }

    public int getX() {
        return this.talentConfig.getX();
    }

    public int getY() {
        return this.talentConfig.getY();
    }

    public final TalentConfig getTalentConfig() {
        return this.talentConfig;
    }
    
    public static final boolean hasTalentUnlocked(EntityPlayer player, Talent t) {
    	return PlayerDataHandler.get(player).getProfessionInfo(t.getParentProfession()).isUnlocked(t);
    }

}
