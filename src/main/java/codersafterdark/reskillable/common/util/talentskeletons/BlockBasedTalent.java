package codersafterdark.reskillable.common.util.talentskeletons;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public abstract class BlockBasedTalent extends Talent {

	private final String id;
	private final IAttribute attribute;
	private final AttributeModifier modifier;
	
	public BlockBasedTalent(Block block, IAttribute attribute, AttributeModifier modifier, ResourceLocation name, int x, int y, ResourceLocation professionName, ResourceLocation subProfessionName, int cost, String... defaultRequirements) {
		this(block.getRegistryName().toString(), attribute, modifier, name, x, y, professionName, subProfessionName, cost, defaultRequirements);
	}
	
	public BlockBasedTalent(String regId, IAttribute attribute, AttributeModifier modifier, ResourceLocation name, int x, int y, ResourceLocation professionName, ResourceLocation subProfessionName, int cost, String... defaultRequirements) {
		super(name, x, y, professionName, subProfessionName, cost, defaultRequirements);
		this.id = regId;
		this.attribute = attribute;
		this.modifier = modifier;
	}
	
	private boolean isPlayerOnRightBlock(EntityPlayer player) {
		return player.world.getBlockState(player.getPosition()).getBlock().getRegistryName().toString().equals(this.id);
	}
	
	@SubscribeEvent
	public final void onPlayerTick(PlayerTickEvent evt) {
		final IAttributeInstance attributeInstance = evt.player.getEntityAttribute(this.attribute);
		final boolean hasModifier = attributeInstance.hasModifier(this.modifier);
		if(Talent.hasTalentUnlocked(evt.player, this) && isPlayerOnRightBlock(evt.player)) {
			if(!hasModifier) {
				attributeInstance.applyModifier(this.modifier);
			}
		}
		else {
			if(hasModifier) {
				attributeInstance.removeModifier(this.modifier);
			}
		}
	}

}
