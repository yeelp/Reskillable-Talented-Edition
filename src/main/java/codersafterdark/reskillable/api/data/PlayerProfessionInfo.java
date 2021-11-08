package codersafterdark.reskillable.api.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.api.talent.TalentActive;
import codersafterdark.reskillable.api.unlockable.IAbilityEventHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class PlayerProfessionInfo {
    private static final String TAG_LEVEL = "level";
    private static final String TAG_SKILL_POINTS = "skillPoints";
    private static final String TAG_TALENTS = "talents";

    public final Profession profession;

    private int level;
    private int skillPoints;
    private List<Talent> talents = new ArrayList<>();

    public PlayerProfessionInfo(Profession profession) {
        this.profession = profession;
        this.level = 0;
        respec();
    }

    public void loadFromNBT(NBTTagCompound cmp) {
        this.level = cmp.getInteger(TAG_LEVEL);
        this.skillPoints = cmp.getInteger(TAG_SKILL_POINTS);

        this.talents.clear();
        NBTTagCompound talentsCmp = cmp.getCompoundTag(TAG_TALENTS);

        for (String s : talentsCmp.getKeySet()) {
            Optional.ofNullable(ReskillableRegistries.TALENTS.getValue(new ResourceLocation(s.replace(".", ":"))))
                    .ifPresent(this.talents::add);
        }
    }

    public void saveToNBT(NBTTagCompound cmp) {
        cmp.setInteger(TAG_LEVEL, this.level);
        cmp.setInteger(TAG_SKILL_POINTS, this.skillPoints);

        NBTTagCompound talentsCmp = new NBTTagCompound();
        for (Talent t : this.talents) {
            talentsCmp.setBoolean(t.getKey(), true);
        }
        cmp.setTag(TAG_TALENTS, talentsCmp);
    }

    public int getLevel() {
        if (this.level > this.profession.getCap()) {
            this.level = this.profession.getCap();
        }

        return this.level;
    }

    public void setLevel(int level) {
        int interval = this.profession.getSkillPointInterval();
        this.skillPoints += level / interval - this.level / interval;
        this.level = level;
    }

    public int getRank() {
        return this.profession.getRank(this.level);
    }

    public int getSkillPoints() {return this.skillPoints;}

    public void spendSkillPoints(int amount) {
        this.skillPoints -= amount;
    }

    public int getProfessionPoints() {
        return this.skillPoints;
    }

    public boolean isCapped() {
        return this.level >= this.profession.getCap();
    }

    public int getLevelUpCost() {
        return this.profession.getLevelUpCost(this.level);
    }

    public boolean isUnlocked(Talent t) {return this.talents.contains(t);}

    //TODO decide if this should just call setLevel(level + 1);
    public void levelUp() {
        this.level++;
        if (this.level % this.profession.getSkillPointInterval() == 0) {
            this.skillPoints++;
        }
    }

    public void addActiveTalents(Set<TalentActive> activeTalents) {
        for (Talent t : this.talents) {
            if (t instanceof TalentActive) {
                activeTalents.add((TalentActive) t);
            }
        }
    }

    public void lock(Talent t, EntityPlayer p) {
        this.skillPoints += t.getCost();
        this.talents.remove(t);
        t.onLock(p);
    }

    public void unlock(Talent u, EntityPlayer p) {
        this.skillPoints -= u.getCost();
        this.talents.add(u);
        PlayerDataHandler.get(p).getTalentInfo(u).levelUp();
        u.onUnlock(p);
    }

    public void respec() {
        this.talents.clear();
        this.skillPoints = this.level / this.profession.getSkillPointInterval();
    }

    public void forEachEventHandler(Consumer<IAbilityEventHandler> consumer) {
        this.talents.forEach(t -> {
            if (t.isEnabled() && t instanceof IAbilityEventHandler) {
                consumer.accept((IAbilityEventHandler) t);
            }
        });
    }
}
