package codersafterdark.reskillable.api.data;

import java.util.ArrayList;
import java.util.List;

import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerUnlockableInfo extends PlayerSkillInfo {

    private static final String TAG_RANK = "rank";
    private static final String TAG_MODIFIERS = "modifiers";

    public final Unlockable unlockable;
    private int rank;
    private final List<AttributeModifier> unlockableAttributes = new ArrayList<>();

    public PlayerUnlockableInfo(Unlockable unlockable) {
        super(unlockable.getParentSkill());
        this.unlockable = unlockable;
        this.rank = 0;
    }

    @Override
	public void loadFromNBT(NBTTagCompound cmp) {
        this.rank = cmp.getInteger(TAG_RANK);

        this.unlockableAttributes.clear();
        NBTTagList modifiersCmp = cmp.getTagList(TAG_MODIFIERS, 10);

        for (int i = 0; i < modifiersCmp.tagCount(); i++) {
            NBTTagCompound attributeCmp = modifiersCmp.getCompoundTagAt(i);
            this.unlockableAttributes.add(SharedMonsterAttributes.readAttributeModifierFromNBT(attributeCmp));
        }
    }

    @Override
	public void saveToNBT(NBTTagCompound cmp) {
        cmp.setInteger(TAG_RANK, this.rank);

        NBTTagList modifiersCmp = new NBTTagList();
        for (AttributeModifier a : this.unlockableAttributes) {
            modifiersCmp.appendTag(SharedMonsterAttributes.writeAttributeModifierToNBT(a));
        }

        cmp.setTag(TAG_MODIFIERS, modifiersCmp);
    }

    @Override
	public int getRank() {
        if (this.rank > this.unlockable.getCap()) {
            this.rank = this.unlockable.getCap();
        }
        return this.rank;
    }

    public void setRank(int rank) {
        int interval = super.skill.getSkillPointInterval();
        spendSkillPoints(rank/interval - this.rank/interval);
        this.rank = rank;
    }

    public List<AttributeModifier> getUnlockableAttributes() {
        return this.unlockableAttributes;
    }

    @Override
	public boolean isCapped() {
        return this.rank >= this.unlockable.getCap();
    }

    @Override
	public int getLevelUpCost() {
        return this.unlockable.getCost();
    }

    @Override
	public void levelUp() {
        this.rank++;
    }

    public void addAttributeModifier(IAttributeInstance attributeInstance, AttributeModifier modifier) {
        this.unlockableAttributes.add(modifier);
        attributeInstance.applyModifier(modifier);
    }

    public void removeUnlockableAttribute(IAttributeInstance attributeInstance) {
        for (AttributeModifier modifier : this.unlockableAttributes) {
            if (modifier.getName().equals(attributeInstance.getAttribute().getName())) {
                attributeInstance.removeModifier(modifier);
            }
        }
        this.unlockableAttributes.clear();
    }

}
