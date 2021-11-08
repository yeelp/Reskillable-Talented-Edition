package codersafterdark.reskillable.api.unlockable;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.Level;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.common.Reskillable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class Unlockable extends IForgeRegistryEntry.Impl<Unlockable> implements Comparable<Unlockable> {
    private final String name;
    protected UnlockableConfig unlockableConfig;
    private ResourceLocation icon;
    private Skill parentSkill;

    public Unlockable(ResourceLocation name, int x, int y, ResourceLocation skillName, int cost, String... defaultRequirements) {
        this.name = name.toString().replace(":", ".");
        setRegistryName(name);
        setIcon(new ResourceLocation(name.getNamespace(), "textures/unlockables/" + name.getPath() + ".png"));
        this.unlockableConfig = ReskillableAPI.getInstance().getTraitConfig(name, x, y, cost, defaultRequirements);
        setParentSkill(skillName);
    }

    @Nonnull
    public Skill getParentSkill() {
        return this.parentSkill;
    }

    protected void setParentSkill(ResourceLocation skillName) {
        if (this.parentSkill != null) {
            if (skillName != null && skillName.equals(this.parentSkill.getRegistryName())) {
                //The skill is already the parent skill
                return;
            }
            //Remove from old parent skill if there already is a parent skill
            this.parentSkill.getUnlockables().remove(this);
        }
        this.parentSkill = Objects.requireNonNull(ReskillableRegistries.SKILLS.getValue(skillName));
        if (isEnabled()) {
            if (this.parentSkill.isEnabled()) {
                this.parentSkill.addUnlockable(this);
            } else {
                Reskillable.logger.log(Level.ERROR, getName() + " is enabled but the parent skill: " + this.parentSkill.getName() + " is disabled. Disabling: " + getName());
                this.unlockableConfig.setEnabled(false);
            }
        }
    }

    public RequirementHolder getRequirements() {
        return this.unlockableConfig.getRequirementHolder();
    }

    public String getKey() {
        return this.name;
    }

    public String getName() {
        return new TextComponentTranslation("reskillable.unlock." + getKey()).getUnformattedComponentText();
    }

    public String getDescription() {
        return new TextComponentTranslation("reskillable.unlock." + getKey() + ".desc").getUnformattedComponentText();
    }

    public void setCap(int cap) {this.unlockableConfig.setRankCap(cap);}

    public int getCap() {return this.unlockableConfig.getRankCap();}

    public ResourceLocation getIcon() {
        return this.icon;
    }

    protected void setIcon(ResourceLocation newIcon) {
        this.icon = newIcon;
    }

    public void onUnlock(@SuppressWarnings("unused") EntityPlayer player) {
    	//no-op
    }

    public void onLock(@SuppressWarnings("unused") EntityPlayer player) {
    	//no-op
    }

    @SuppressWarnings("static-method")
	public boolean hasSpikes() {
        return false;
    }

    public boolean isEnabled() {
        return this.unlockableConfig.isEnabled();
    }

    @Override
    public int compareTo(@Nonnull Unlockable o) {
        int skillCmp = getParentSkill().compareTo(o.getParentSkill());
        if (skillCmp == 0) {
            return getName().compareTo(o.getName());
        }
        return skillCmp;
    }

    public int getCost() {
        return this.unlockableConfig.getCost();
    }

    public int getX() {
        return this.unlockableConfig.getX();
    }

    public int getY() {
        return this.unlockableConfig.getY();
    }

    public final UnlockableConfig getUnlockableConfig() {
        return this.unlockableConfig;
    }
}