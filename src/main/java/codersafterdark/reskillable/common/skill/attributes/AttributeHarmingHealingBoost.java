package codersafterdark.reskillable.common.skill.attributes;

import codersafterdark.reskillable.common.potion.ReskillablePotion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AttributeHarmingHealingBoost {
	public static class PotionEffectBoost extends Potion {
		private Type type;

		public PotionEffectBoost(Type type) {
			super(type.isBad(), type.getColor());
			this.type = type;
			this.setRegistryName(this.type.getRegistryName());
			this.setPotionName(this.type.getPotionName());
		}

		@Override
		public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
			this.type.performEffect(entityLivingBaseIn, amplifier);
		}

		@Override
		public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entityLivingBaseIn, int amplifier, double health) {
			float mod = 0.0f;
			if(indirectSource instanceof EntityPlayer) {
				mod = (float) ((EntityPlayer) indirectSource).getEntityAttribute(ReskillableAttributes.POTION_POTENCY).getAttributeValue();
			}
			this.type.getRelatedPotion().affectEntity(source, indirectSource, entityLivingBaseIn, amplifier, health * mod);
		}

		@Override
		public boolean isReady(int duration, int amplifier) {
			return this.type.getRelatedPotion().isReady(duration, amplifier);
		}

		@Override
		public boolean isInstant() {
			return true;
		}

		public enum Type {
			HEALING(MobEffects.INSTANT_HEALTH) {
				@Override
				protected String getRegistryName() {
					return "healingboost";
				}

				@Override
				protected void performEffect(EntityLivingBase entity, int amp) {
					entity.heal((float) entity.getEntityAttribute(ReskillableAttributes.POTION_POTENCY).getAttributeValue() * Math.max(4 << amp, 0));
				}
			},
			DAMAGE(MobEffects.INSTANT_DAMAGE) {
				@Override
				protected String getRegistryName() {
					return "harmingboost";
				}

				@Override
				protected void performEffect(EntityLivingBase entity, int amp) {
					entity.attackEntityFrom(DamageSource.MAGIC, (float) entity.getEntityAttribute(ReskillableAttributes.POTION_POTENCY).getAttributeValue() * (6 << amp));
				}
			};

			private final Potion type;

			private Type(Potion effect) {
				this.type = effect;
			}

			protected Potion getRelatedPotion() {
				return this.type;
			}

			protected boolean isBad() {
				return this.type.isBadEffect();
			}

			protected int getColor() {
				return this.type.getLiquidColor();
			}

			protected abstract String getRegistryName();

			protected abstract void performEffect(EntityLivingBase entity, int amp);

			protected String getPotionName() {
				return "effect.".concat(this.getRegistryName());
			}
		}
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onInstantPotionApply(PotionAddedEvent evt) {
		PotionEffect effect = evt.getPotionEffect();
		EntityLivingBase user = evt.getEntityLiving();
		float mod = (float) user.getEntityAttribute(ReskillableAttributes.POTION_POTENCY).getAttributeValue();
		if(mod == 1) {
			return;
		}
		Potion pot;
		if(effect.getPotion() == MobEffects.INSTANT_HEALTH) {
			pot = ReskillablePotion.HEALING_BOOST;
		}
		else if(effect.getPotion() == MobEffects.INSTANT_DAMAGE) {
			pot = ReskillablePotion.HARMING_BOOST;
		}
		else {
			return;
		}
		evt.getEntityLiving().addPotionEffect(new PotionEffect(pot, effect.getDuration(), effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles()));
	}
}
