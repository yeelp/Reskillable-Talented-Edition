package codersafterdark.reskillable.api.data;

import java.util.Objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class ItemInfo extends NBTLockKey {
    private int metadata;
    private Item item;

    public ItemInfo(Item item, int metadata) {
        this(item, metadata, null);
    }

    public ItemInfo(Item item, int metadata, NBTTagCompound tag) {
        super(tag);
        this.item = item;
        this.metadata = metadata;
    }

    public ItemInfo(ItemStack stack) {
        this(stack.getItem(), stack.getMetadata(), stack.getTagCompound());
    }

    @Override
    public LockKey getNotFuzzy() {
        return isNotFuzzy() ? this : new ItemInfo(this.item, this.metadata);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemInfo)) {
            return false;
        }
        ItemInfo other = (ItemInfo) o;
        if (getItem() != other.getItem()) {
            return false;
        }
        if (getTag() == null) {
            return other.getTag() == null && (getMetadata() == OreDictionary.WILDCARD_VALUE || other.getMetadata() == OreDictionary.WILDCARD_VALUE || getMetadata() == other.getMetadata());
        }
        return getTag().equals(other.getTag()) && (getMetadata() == OreDictionary.WILDCARD_VALUE || other.getMetadata() == OreDictionary.WILDCARD_VALUE || getMetadata() == other.getMetadata());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.item, this.tag);
    }

    public Item getItem() {
        return this.item;
    }

    public int getMetadata() {
        return this.metadata;
    }
}
