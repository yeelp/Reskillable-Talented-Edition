package codersafterdark.reskillable.api.data;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerTalentInfo extends PlayerProfessionInfo {

    private static final String TAG_RANK = "rank";
    public final Talent talent;
    private int rank;

    public PlayerTalentInfo(Talent talent) {
        super(talent.getParentProfession());
        this.talent = talent;
        rank = 0;
    }

    public void loadFromNBT(NBTTagCompound cmp) {
        rank = cmp.getInteger(TAG_RANK);
    }

    public void saveToNBT(NBTTagCompound cmp) {
        cmp.setInteger(TAG_RANK, rank);
    }

    public int getRank() {
        if (rank > talent.getCap()) {
            rank = talent.getCap();
        }

        return rank;
    }

    public void setRank(int rank) {
        int interval = super.profession.getSkillPointInterval();
        spendSkillPoints(rank/interval - this.rank/interval);
        this.rank = rank;
    }

    public boolean isCapped() {
        return rank >= talent.getCap();
    }

    public int getLevelUpCost() {
        return talent.getCost();
    }

    public void levelUp() {
        rank++;
    }

}
