package codersafterdark.reskillable.api.skill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class Skill extends IForgeRegistryEntry.Impl<Skill> implements Comparable<Skill> {
    private final Map<Integer, ResourceLocation> customSprites = new HashMap<>();
    private final List<Unlockable> unlockables = new ArrayList<>();
    private final ResourceLocation spriteLocation;
    private final String name;
    protected ResourceLocation background;
    protected SkillConfig skillConfig;
    private boolean hidden;

    public Skill(ResourceLocation name, ResourceLocation background) {
        this.name = name.toString().replace(":", ".");
        this.background = background;
        this.spriteLocation = new ResourceLocation(name.getNamespace(), "textures/skills/" + name.getPath() + ".png");
        this.setRegistryName(name);
        this.skillConfig = ReskillableAPI.getInstance().getSkillConfig(name);
    }

    public void addUnlockable(Unlockable unlockable) {
        this.unlockables.add(unlockable);
    }

    public List<Unlockable> getUnlockables() {
        return this.unlockables;
    }

    public String getKey() {
        return this.name;
    }

    public String getName() {
        return new TextComponentTranslation("reskillable.skill." + getKey()).getUnformattedComponentText();
    }

    public ResourceLocation getBackground() {
        return this.background;
    }

    public void setBackground(ResourceLocation resourceLocation) {
        this.background = resourceLocation;
    }

    public int getCap() {
        return this.skillConfig.getLevelCap();
    }

    public boolean isEnabled() {
        return this.skillConfig.isEnabled();
    }

    public boolean hasLevelButton() {
        return this.skillConfig.hasLevelButton();
    }

    public ResourceLocation getSpriteLocation() {
        return this.spriteLocation;
    }

    @SuppressWarnings("static-method")
	public Pair<Integer, Integer> getSpriteFromRank(int rank) {
        //TODO: If we ever end up having more images than 4 when the Math.min is changed make sure to also change the value rank is divided by
        return new MutablePair<>(Math.min(rank / 2, 3) * 16, 0);
    }

    public void setCustomSprite(int rank, ResourceLocation location) {
        this.customSprites.put(rank, location);
    }

    public void removeCustomSprite(int rank) {
        this.customSprites.remove(rank);
    }

    public ResourceLocation getSpriteLocation(int rank) {
        if (this.customSprites.containsKey(rank)) {
            return this.customSprites.get(rank);
        }
        for (int i = rank - 1; i >= 0; i--) {
            if (this.customSprites.containsKey(i)) {
                return this.customSprites.get(i);
            }
        }
        return null;
    }

    public boolean hasCustomSprites() {
        return !this.customSprites.isEmpty();
    }

    @Override
    public int compareTo(@Nonnull Skill o) {
        return o.getName().compareTo(this.getName());
    }

    public int getSkillPointInterval() {
        return this.skillConfig.getSkillPointInterval();
    }

    public int getLevelUpCost(int level) {
        int cost = this.skillConfig.getLevelStaggering()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey() < level + 1)
                .mapToInt(Map.Entry::getValue)
                .sum() + this.skillConfig.getBaseLevelCost();
        return cost < 0 ? 0 : cost;
    }

    public final SkillConfig getSkillConfig() {
        return this.skillConfig;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public int getRank(int level) {
        return 8 * level / getCap();
    }
}