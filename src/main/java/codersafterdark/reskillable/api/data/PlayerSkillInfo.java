package codersafterdark.reskillable.api.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Ability;
import codersafterdark.reskillable.api.unlockable.IAbilityEventHandler;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class PlayerSkillInfo {
    private static final String TAG_LEVEL = "level";
    private static final String TAG_SKILL_POINTS = "skillPoints";
    private static final String TAG_UNLOCKABLES = "unlockables";

    public final Skill skill;

    private int level;
    private int skillPoints;
    private List<Unlockable> unlockables = new ArrayList<>();

    public PlayerSkillInfo(Skill skill) {
        this.skill = skill;
        this.level = 1;
        respec();
    }

    public void loadFromNBT(NBTTagCompound cmp) {
        this.level = cmp.getInteger(TAG_LEVEL);
        this.skillPoints = cmp.getInteger(TAG_SKILL_POINTS);

        this.unlockables.clear();
        NBTTagCompound unlockablesCmp = cmp.getCompoundTag(TAG_UNLOCKABLES);

        for (String s : unlockablesCmp.getKeySet()) {
            Optional.ofNullable(ReskillableRegistries.UNLOCKABLES.getValue(new ResourceLocation(s.replace(".", ":"))))
                    .ifPresent(this.unlockables::add);
        }
    }

    public void saveToNBT(NBTTagCompound cmp) {
        cmp.setInteger(TAG_LEVEL, this.level);
        cmp.setInteger(TAG_SKILL_POINTS, this.skillPoints);

        NBTTagCompound unlockablesCmp = new NBTTagCompound();
        for (Unlockable u : this.unlockables) {
            unlockablesCmp.setBoolean(u.getKey(), true);
        }
        cmp.setTag(TAG_UNLOCKABLES, unlockablesCmp);
    }

    public int getLevel() {
        if (this.level <= 0) {
            this.level = 1;
        }
        if (this.level > this.skill.getCap()) {
            this.level = this.skill.getCap();
        }

        return this.level;
    }

    public void setLevel(int level) {
        int interval = this.skill.getSkillPointInterval();
        this.skillPoints += level / interval - this.level / interval;
        this.level = level;
    }

    public int getRank() {
        return this.skill.getRank(this.level);
    }

    public int getSkillPoints() {
        return this.skillPoints;
    }

    public void spendSkillPoints(int amount) {
        this.skillPoints -= amount;
    }

    public boolean isCapped() {
        return this.level >= this.skill.getCap();
    }

    public int getLevelUpCost() {
        return this.skill.getLevelUpCost(this.level);
    }

    public boolean isUnlocked(Unlockable u) {
        return this.unlockables.contains(u);
    }

    public void addAbilities(Set<Ability> abilities) {
        for (Unlockable u : this.unlockables) {
            if (u instanceof Ability) {
                abilities.add((Ability) u);
            }
        }
    }

    //TODO decide if this should just call setLevel(level + 1);
    public void levelUp() {
        this.level++;
        if (this.level % this.skill.getSkillPointInterval() == 0) {
            this.skillPoints++;
        }
    }

    public void lock(Unlockable u, EntityPlayer p) {
        this.skillPoints += u.getCost();
        this.unlockables.remove(u);
        u.onLock(p);
    }

    public void unlock(Unlockable u, EntityPlayer p) {
        this.skillPoints -= u.getCost();
        this.unlockables.add(u);
        u.onUnlock(p);
    }

    public void respec() {
        this.unlockables.clear();
        this.skillPoints = this.level / this.skill.getSkillPointInterval();
    }

    public void forEachEventHandler(Consumer<IAbilityEventHandler> consumer) {
        this.unlockables.forEach(u -> {
            if (u.isEnabled() && u instanceof IAbilityEventHandler) {
                consumer.accept((IAbilityEventHandler) u);
            }
        });
    }
}