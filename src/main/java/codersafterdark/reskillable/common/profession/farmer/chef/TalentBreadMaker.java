package codersafterdark.reskillable.common.profession.farmer.chef;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import java.util.UUID;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerTalentInfo;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import yeelp.scalingfeast.init.SFAttributes;

public class TalentBreadMaker extends Talent {

	private static final AttributeModifier MODIFIER = new AttributeModifier(UUID.fromString("d374fbf9-2136-445e-9334-a0f6a74332d9"), "Bread Maker Bonus", 8, 0);
	
	public TalentBreadMaker() {
		super(new ResourceLocation(MOD_ID, "bread_maker"), 12, 12, new ResourceLocation(MOD_ID, "farmer"), new ResourceLocation(MOD_ID, "chef"), 12, "profession|reskillable:farmer|13");
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void onUnlock(EntityPlayer player) {
		PlayerData data = PlayerDataHandler.get(player);
		PlayerTalentInfo info = data.getTalentInfo(this);
		info.addAttributeModifier(player.getEntityAttribute(SFAttributes.MAX_HUNGER_MOD), MODIFIER);
		data.saveAndSync();
		super.onUnlock(player);
	}

	@Override
	public void onLock(EntityPlayer player) {
		PlayerData data = PlayerDataHandler.get(player);
		PlayerTalentInfo info = data.getTalentInfo(this);
		info.removeTalentAttribute(player.getEntityAttribute(SFAttributes.MAX_HUNGER_MOD));
		data.saveAndSync();
		super.onLock(player);
	}

	
}
