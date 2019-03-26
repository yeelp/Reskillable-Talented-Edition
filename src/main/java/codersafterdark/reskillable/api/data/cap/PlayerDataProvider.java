package codersafterdark.reskillable.api.data.cap;

import codersafterdark.reskillable.Reskillable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerDataProvider implements ICapabilitySerializable<NBTTagCompound> {

    IPlayerData data = Reskillable.CAPABILITY_PLAYER_DATA.getDefaultInstance();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == Reskillable.CAPABILITY_PLAYER_DATA;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return hasCapability(capability, facing) ? Reskillable.CAPABILITY_PLAYER_DATA.cast(data) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound) Reskillable.CAPABILITY_PLAYER_DATA.getStorage().writeNBT(Reskillable.CAPABILITY_PLAYER_DATA, data, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        Reskillable.CAPABILITY_PLAYER_DATA.getStorage().readNBT(Reskillable.CAPABILITY_PLAYER_DATA, data, null, nbt);
    }
}
