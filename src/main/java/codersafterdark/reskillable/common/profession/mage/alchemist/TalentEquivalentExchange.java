package codersafterdark.reskillable.common.profession.mage.alchemist;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import com.google.common.collect.ImmutableSet;
import com.tmtravlr.potioncore.PotionCoreAttributes;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.common.potion.ReskillablePotion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TalentEquivalentExchange extends AbstractPotionEffectBasedTalent {

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
			this.type.getRelatedPotion().affectEntity(source, indirectSource, entityLivingBaseIn, amplifier, health * 0.25);
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
					entity.heal(0.25f * Math.max(4 << amp, 0));
				}
			},
			DAMAGE(MobEffects.INSTANT_DAMAGE) {
				@Override
				protected String getRegistryName() {
					return "harmingboost";
				}

				@Override
				protected void performEffect(EntityLivingBase entity, int amp) {
					entity.attackEntityFrom(DamageSource.MAGIC, 0.25f * (6 << amp));
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

	public TalentEquivalentExchange() {
		super(ImmutableSet.of(new PotionEffectBasedAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "EquivalentExchange Attack Speed Bonus", MobEffects.SLOWNESS, 0.2, 2), new PotionEffectBasedAttributeModifier(PotionCoreAttributes.DAMAGE_RESISTANCE, "EquivalentExchange Damage Resistance Bonus", MobEffects.MINING_FATIGUE, 0.1, 2), new PotionEffectBasedAttributeModifier(EntityPlayer.REACH_DISTANCE, "EquivalentExchange Reach Distance Bonus", MobEffects.BLINDNESS, 1, 0)), new ResourceLocation(MOD_ID, "equivalentExchange"), 3, 1, new ResourceLocation(MOD_ID, "mage"), new ResourceLocation(MOD_ID, "alchemist"), 3, "profession|reskillable:mage|13");
	}

	@SubscribeEvent
	public void onInstantPotionApply(PotionAddedEvent evt) {
		PotionEffect effect = evt.getPotionEffect();
		EntityLivingBase user = evt.getEntityLiving();
		if(!validUser(user)) {
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

	private boolean validUser(EntityLivingBase thrower) {
		if(thrower instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) thrower;
			return PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this) && player.isPotionActive(MobEffects.WEAKNESS);
		}
		return false;
	}
}
