package codersafterdark.reskillable.api.data;

import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.*;

public class PlayerUnlockableInfo extends PlayerSkillInfo {

    private static final String TAG_RANK = "rank";
    private static final String TAG_MODIFIERS = "modifiers";

    public final Unlockable unlockable;
    private int rank;
    private final List<AttributeModifier> unlockableAttributes = new ArrayList<>();

    public PlayerUnlockableInfo(Unlockable unlockable) {
        super(unlockable.getParentSkill());
        this.unlockable = unlockable;
        rank = 0;
    }

    public void loadFromNBT(NBTTagCompound cmp) {
        rank = cmp.getInteger(TAG_RANK);

        unlockableAttributes.clear();
        NBTTagList modifiersCmp = cmp.getTagList(TAG_MODIFIERS, 10);

        if (this.unlockable.getKey().equals("reskillable.lucky_strikes")) {
            System.out.println("loaded info data for Lucky Strikes");
        }
        if (!modifiersCmp.isEmpty()) {
            System.out.println("Modifier List is Not Empty");
            debugReadModifier(modifiersCmp);
            System.out.println(modifiersCmp.getCompoundTagAt(0).getString("Name"));
        }

        for (int i = 0; i < modifiersCmp.tagCount(); i++) {
            NBTTagCompound attributeCmp = modifiersCmp.getCompoundTagAt(i);
            AttributeModifier modifier = SharedMonsterAttributes.readAttributeModifierFromNBT(attributeCmp);
            System.out.println("Retrieved attribute " + modifier.getName() + " from NBT Tag");
            unlockableAttributes.add(SharedMonsterAttributes.readAttributeModifierFromNBT(attributeCmp));
            System.out.println("Added attribute " + modifier.getName() + " to unlockable attribute list");
        }

    }

    public void saveToNBT(NBTTagCompound cmp) {
        cmp.setInteger(TAG_RANK, rank);

        if (this.unlockable.getKey().equals("reskillable.lucky_strikes")) {
            System.out.println("saving nbt data for lucky_strikes");
        }
        NBTTagList modifiersCmp = new NBTTagList();
        if (unlockableAttributes.isEmpty()) {System.out.println("Attribute List was deemed empty first during saving NBT");
        for (AttributeModifier a : unlockableAttributes) {
            modifiersCmp.appendTag(SharedMonsterAttributes.writeAttributeModifierToNBT(a));
        }
        cmp.setTag(TAG_MODIFIERS, modifiersCmp);

        // Debugging
        if (this.unlockable.getKey().equals("reskillable.lucky_strikes")
            && !cmp.getTagList(TAG_MODIFIERS, 10).getCompoundTagAt(0).isEmpty()) {
                NBTTagCompound debugTag = cmp.getTagList(TAG_MODIFIERS, 10).getCompoundTagAt(0);
                AttributeModifier debugModifier = SharedMonsterAttributes.readAttributeModifierFromNBT(debugTag);
                System.out.println("Successfully saved " + debugModifier.getName() + " to nbt");
            }
        }
    }

    public int getRank() {
        if (rank > unlockable.getCap()) {
            rank = unlockable.getCap();
        }
        return rank;
    }

    public void setRank(int rank) {
        int interval = super.skill.getSkillPointInterval();
        spendSkillPoints(rank/interval - this.rank/interval);
        this.rank = rank;
    }

    public List<AttributeModifier> getUnlockableAttributes() {
        return unlockableAttributes;
    }

    public boolean isCapped() {
        return rank >= unlockable.getCap();
    }

    public int getLevelUpCost() {
        return unlockable.getCost();
    }

    public void levelUp() {
        rank++;
    }

    public void addAttributeModifier(IAttributeInstance attributeInstance, AttributeModifier modifier) {
        unlockableAttributes.add(modifier);
        attributeInstance.applyModifier(modifier);
    }

    public void removeUnlockableAttribute(IAttributeInstance attributeInstance) {
        if (unlockableAttributes.isEmpty()) {
            System.out.println("Unlockable Attribute List is Empty");
        }
        for (AttributeModifier modifier : unlockableAttributes) {
            System.out.println(modifier.getName());
            System.out.println(attributeInstance.getAttribute().getName());
            if (modifier.getName().equals(attributeInstance.getAttribute().getName())) {
                attributeInstance.removeModifier(modifier);
                System.out.println("Modifier removed");
            }
        }
        unlockableAttributes.clear();
    }

    public void debugReadModifier(NBTTagList tagList) {
        if (!tagList.getCompoundTagAt(0).isEmpty()) {
            for (int i = 0; i < tagList.tagCount(); i++) {
                Set<String> stringSet = tagList.getCompoundTagAt(i).getKeySet();
                for (String s : stringSet) {
                    System.out.println(s);
                }
            }
        }
    }

}
