package codersafterdark.reskillable.common.profession.farmer.chef;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import com.google.common.collect.Sets;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerTalentInfo;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.init.SFAttributes;
import yeelp.scalingfeast.init.SFPotion;

public class TalentEloquentTaste extends Talent {

	private static final Set<UUID> EATING_PLAYERS = Sets.newHashSet();
	private static final AttributeModifier MODIFIER = new AttributeModifier(UUID.fromString("179a65c7-bbea-499b-9be7-22d0e3743549"), "EloquentTaste Food Efficiency Bonus", 0.2, 2);

	public TalentEloquentTaste() {
		super(new ResourceLocation(MOD_ID, "eloquent_taste"), 5, 5, new ResourceLocation(MOD_ID, "farmer"), new ResourceLocation(MOD_ID, "chef"), 5, "profession|reskillable:farmer|13");
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public final void onFoodConsumeStart(LivingEntityUseItemEvent.Start evt) {
		updatePlayerEatingStatus(evt, EATING_PLAYERS::add);
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public final void onFoodConsumeStop(LivingEntityUseItemEvent.Stop evt) {
		updatePlayerEatingStatus(evt, EATING_PLAYERS::remove);
	}

	@SubscribeEvent
	public final void onFoodConsumeFinish(LivingEntityUseItemEvent.Finish evt) {
		if(evt.getEntityLiving() instanceof EntityPlayer && Talent.hasTalentUnlocked((EntityPlayer) evt.getEntityLiving(), this) && EATING_PLAYERS.remove(evt.getEntityLiving().getUniqueID())) {
			ItemStack stack = evt.getItem();
			ItemFood food = (ItemFood) stack.getItem();
			int amp = food.getHealAmount(stack) / 4 - 1;
			if(amp != -1) {
				float durMod = 2 * food.getSaturationModifier(stack);
				evt.getEntityLiving().world.getEntitiesWithinAABB(EntityPlayer.class, evt.getEntityLiving().getCollisionBoundingBox().grow(16), evt.getEntityLiving()::equals).forEach((player) -> player.addPotionEffect(new PotionEffect(SFPotion.metabolism, (int) (200 * durMod), amp)));
			}
		}
	}

	@Override
	public void onUnlock(EntityPlayer player) {
		PlayerData data = PlayerDataHandler.get(player);
		PlayerTalentInfo info = data.getTalentInfo(this);
		info.addAttributeModifier(player.getEntityAttribute(SFAttributes.FOOD_EFFICIENCY), MODIFIER);
		data.saveAndSync();
		super.onUnlock(player);
	}

	@Override
	public void onLock(EntityPlayer player) {
		PlayerData data = PlayerDataHandler.get(player);
		data.getTalentInfo(this).removeTalentAttribute(player.getEntityAttribute(SFAttributes.FOOD_EFFICIENCY));
		data.saveAndSync();
		super.onLock(player);
	}

	private static void updatePlayerEatingStatus(LivingEntityUseItemEvent evt, Consumer<UUID> action) {
		if(evt.getEntityLiving() instanceof EntityPlayer && evt.getItem().getItem() instanceof ItemFood) {
			EntityPlayer player = (EntityPlayer) evt.getEntityLiving();
			action.accept(player.getUniqueID());
		}
	}
}
