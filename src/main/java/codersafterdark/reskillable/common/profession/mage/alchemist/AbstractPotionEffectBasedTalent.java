package codersafterdark.reskillable.common.profession.mage.alchemist;

import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableSet;

import codersafterdark.reskillable.api.event.LockTalentEvent;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class AbstractPotionEffectBasedTalent extends Talent {

	protected static class PotionEffectBasedAttributeModifier {
		private IAttribute attribute;
		private AttributeModifier modifier;
		private final Set<Potion> triggers;

		PotionEffectBasedAttributeModifier(IAttribute affectedAttribute, String name, Potion applicableEffect, double amount, int operation) {
			this(affectedAttribute, name, ImmutableSet.of(applicableEffect), amount, operation);
		}
		
		PotionEffectBasedAttributeModifier(IAttribute affectedAttribute, String name, Set<Potion> applicableEffects, double amount, int operation) {
			this.attribute = affectedAttribute;
			this.triggers = applicableEffects;
			this.modifier = new AttributeModifier(name, amount, operation);
			MinecraftForge.EVENT_BUS.register(this);
		}

		public void setModifierIfEffectActive(EntityPlayerMP player) {
			if(this.triggers.stream().anyMatch(player::isPotionActive)) {
				alterModifier(player, IAttributeInstance::applyModifier, Objects::isNull);
			}
		}

		public void removeModifierIfEffectIsNotActive(EntityPlayerMP player) {
			if(this.triggers.stream().noneMatch(player::isPotionActive)) {
				forceRemoveModifier(player);
			}
		}
		
		public void forceRemoveModifier(EntityPlayerMP player) {
			alterModifier(player, IAttributeInstance::removeModifier, Objects::nonNull);
		}
		
		private void alterModifier(EntityPlayerMP player, BiConsumer<IAttributeInstance, AttributeModifier> action, Predicate<AttributeModifier> pred) {
			IAttributeInstance instance = player.getEntityAttribute(this.attribute);
			if(pred.test(instance.getModifier(this.modifier.getID()))) {
				action.accept(instance, this.modifier);
			}
		}
	}

	private final Set<PotionEffectBasedAttributeModifier> mods;

	protected AbstractPotionEffectBasedTalent(Set<PotionEffectBasedAttributeModifier> mods, ResourceLocation name, int x, int y, ResourceLocation professionName, ResourceLocation subProfessionName, int cost, String... defaultRequirements) {
		super(name, x, y, professionName, subProfessionName, cost, defaultRequirements);
		this.mods = mods;
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onPotionApply(PotionAddedEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayerMP) {
			this.mods.forEach((pmod) -> pmod.setModifierIfEffectActive((EntityPlayerMP)event.getEntityLiving()));
		}
	}
	
	@SubscribeEvent
	public void onPotionExpiry(PotionExpiryEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayerMP) {
			this.mods.forEach((pmod) -> pmod.removeModifierIfEffectIsNotActive((EntityPlayerMP)event.getEntityLiving()));
		}
	}
	
	@SubscribeEvent
	public void onLock(LockTalentEvent.Post event) {
		if(this.getClass().isInstance(event.getTalent()) && event.getEntityLiving() instanceof EntityPlayerMP) {
			this.mods.forEach((pmod) -> pmod.forceRemoveModifier((EntityPlayerMP) event.getEntityPlayer()));
		}
	}
}
