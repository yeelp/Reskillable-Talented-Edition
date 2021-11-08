package codersafterdark.reskillable.api.data;

import java.util.ArrayList;
import java.util.List;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerTalentInfo extends PlayerProfessionInfo {

    private static final String TAG_RANK = "rank";
    private static final String TAG_MODIFIERS = "modifiers";

    public final Talent talent;
    private int rank;
    private final List<AttributeModifier> talentAttributes = new ArrayList<>();

    public PlayerTalentInfo(Talent talent) {
        super(talent.getParentProfession());
        this.talent = talent;
        this.rank = 0;
    }

    @Override
	public void loadFromNBT(NBTTagCompound cmp) {
        this.rank = cmp.getInteger(TAG_RANK);

        this.talentAttributes.clear();
        NBTTagList modifiersCmp = cmp.getTagList(TAG_MODIFIERS, 10);

        for (int i = 0; i < modifiersCmp.tagCount(); i++) {
            NBTTagCompound attributeCmp = modifiersCmp.getCompoundTagAt(i);
            this.talentAttributes.add(SharedMonsterAttributes.readAttributeModifierFromNBT(attributeCmp));
        }
    }

    @Override
	public void saveToNBT(NBTTagCompound cmp) {
        cmp.setInteger(TAG_RANK, this.rank);

        NBTTagList modifiersCmp = new NBTTagList();
        for (AttributeModifier a : this.talentAttributes) {
            modifiersCmp.appendTag(SharedMonsterAttributes.writeAttributeModifierToNBT(a));
        }

        cmp.setTag(TAG_MODIFIERS, modifiersCmp);
    }

    @Override
	public int getRank() {
        if (this.rank > this.talent.getCap()) {
            this.rank = this.talent.getCap();
        }
        return this.rank;
    }

    public void setRank(int rank) {
        int interval = super.profession.getSkillPointInterval();
        spendSkillPoints(rank/interval - this.rank/interval);
        this.rank = rank;
    }

    public List<AttributeModifier> getTalentAttributes() {return this.talentAttributes;}

    @Override
	public boolean isCapped() {
        return this.rank >= this.talent.getCap();
    }

    @Override
	public int getLevelUpCost() {
        return this.talent.getCost();
    }

    @Override
	public void levelUp() {
        this.rank++;
    }

    public void addAttributeModifier(IAttributeInstance attributeInstance, AttributeModifier modifier) {
        this.talentAttributes.add(modifier);
        attributeInstance.applyModifier(modifier);
    }

    public void removeTalentAttribute(IAttributeInstance attributeInstance) {
        for (AttributeModifier modifier : this.talentAttributes) {
            if (modifier.getName().equals(attributeInstance.getAttribute().getName())) {
                attributeInstance.removeModifier(modifier);
            }
        }
        this.talentAttributes.clear();
    }

}
