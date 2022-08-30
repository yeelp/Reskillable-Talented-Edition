package codersafterdark.reskillable.common.util.talentskeletons;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerTalentInfo;
import codersafterdark.reskillable.api.event.LockTalentEvent;
import codersafterdark.reskillable.api.event.UnlockTalentEvent;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class TrainingTalent extends Talent {

	private final IAttribute attribute;
	private final TrainingLevel level;
	private static final String KEY = "reskillable.firstunlock";

	protected static enum TrainingLevel {
		NOVICE,
		ADVANCED,
		MASTER;
	}

	public TrainingTalent(IAttribute attribute, TrainingLevel level, int x, int y, ResourceLocation professionName, ResourceLocation subProfessionName, int cost, String[] defaultRequirements) {
		super(new ResourceLocation(MOD_ID, String.format("%s_%s", subProfessionName.getPath(), level.name().toLowerCase())), x, y, professionName, subProfessionName, cost, defaultRequirements);
		this.attribute = attribute;
		this.level = level;
		MinecraftForge.EVENT_BUS.register(this);
	}

	protected final IAttribute getAttributeBeingModified() {
		return this.attribute;
	}

	protected abstract float getAttributeModifierAmount(TrainingLevel level);

	protected abstract int getOperation();
	
	@SubscribeEvent
	public final void onTalentUnlock(UnlockTalentEvent.Post evt) {
		if(evt.getTalent().getKey().equals(this.getKey()) && !evt.getEntityPlayer().world.isRemote) {
			EntityPlayer player = evt.getEntityPlayer();
			PlayerData data = PlayerDataHandler.get(player);	
			PlayerTalentInfo info = data.getTalentInfo(this);
			info.addAttributeModifier(player.getEntityAttribute(this.attribute), new AttributeModifier(this.level.name() + " Training Bonus", this.getAttributeModifierAmount(this.level), this.getOperation()));
			data.saveAndSync();
			if(this.level == TrainingLevel.NOVICE) {
				NBTTagCompound root = player.getEntityData();
				NBTTagCompound tag;
				byte unlocks = 0;
				if(root.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
					tag = root.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
				}
				else {
					root.setTag(EntityPlayer.PERSISTED_NBT_TAG, tag = new NBTTagCompound());
				}
				if(tag.hasKey(KEY)) {
					unlocks = tag.getByte(KEY);
				}
				if((unlocks & this.getParentProfession().getID()) == 0) {
					unlocks ^= this.getParentProfession().getID();
					tag.setByte(KEY, unlocks);
					//TODO Give sub profession kit
				}
			}
		}
	}
	
	@SubscribeEvent
	public final void onTalentLock(LockTalentEvent.Post evt) {
		if(evt.getTalent().getKey().equals(this.getKey())) {
			EntityPlayer player = evt.getEntityPlayer();
			PlayerData data = PlayerDataHandler.get(player);
			PlayerTalentInfo info = data.getTalentInfo(this);
			info.removeTalentAttribute(player.getEntityAttribute(this.attribute));
			data.saveAndSync();
		}
	}
}
