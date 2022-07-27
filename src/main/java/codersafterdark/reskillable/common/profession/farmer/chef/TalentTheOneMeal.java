package codersafterdark.reskillable.common.profession.farmer.chef;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import java.util.UUID;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import yeelp.scalingfeast.init.SFAttributes;

public class TalentTheOneMeal extends Talent {

	private static final AttributeModifier MODIFIER = new AttributeModifier(UUID.fromString("7b4604a4-0383-4c68-952d-48cf9c84cabe"), "The One Meal Max Hunger Bonus", 8, 0);
	
	public TalentTheOneMeal() {
		super(new ResourceLocation(MOD_ID, "the_one_meal"), 10, 10, new ResourceLocation(MOD_ID, "farmer"), new ResourceLocation(MOD_ID, "chef"), 5, "profession|reskillable:farmer|13");
	}

	@Override
	public void onUnlock(EntityPlayer player) {
		PlayerData data = PlayerDataHandler.get(player);
		data.getTalentInfo(this).addAttributeModifier(player.getEntityAttribute(SFAttributes.MAX_HUNGER_MOD), MODIFIER);
		data.saveAndSync();
		super.onUnlock(player);
	}

	@Override
	public void onLock(EntityPlayer player) {
		PlayerData data = PlayerDataHandler.get(player);
		data.getTalentInfo(this).removeTalentAttribute(player.getEntityAttribute(SFAttributes.MAX_HUNGER_MOD));
		data.saveAndSync();
	}
	
	
}
