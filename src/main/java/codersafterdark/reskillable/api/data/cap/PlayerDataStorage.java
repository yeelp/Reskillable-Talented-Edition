package codersafterdark.reskillable.api.data.cap;

import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class PlayerDataStorage implements Capability.IStorage<IPlayerData> {
    private static final String TAG_SKILLS_CMP = "SkillLevels";

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IPlayerData> capability, IPlayerData instance, EnumFacing side) {
        NBTTagCompound compound = new NBTTagCompound();
        for (PlayerSkillInfo info : instance.getAllSkillInfo()) {
            String key = info.skill.getKey();
            NBTTagCompound infoCmp = new NBTTagCompound();
            info.saveToNBT(infoCmp);
            compound.setTag(key, infoCmp);
        }
        compound.setTag(TAG_SKILLS_CMP, compound);
        return compound;
    }

    @Override
    public void readNBT(Capability<IPlayerData> capability, IPlayerData instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound compound = (NBTTagCompound) nbt;
        NBTTagCompound skillsCmp = compound.getCompoundTag(TAG_SKILLS_CMP);
        for (PlayerSkillInfo info : instance.getAllSkillInfo()) {
            String key = info.skill.getKey();
            if (skillsCmp.hasKey(key)) {
                NBTTagCompound infoCmp = skillsCmp.getCompoundTag(key);
                info.loadFromNBT(infoCmp);
            }
        }
    }
}
