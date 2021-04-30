package codersafterdark.reskillable.common.potion;

import codersafterdark.reskillable.common.skill.attributes.ReskillableAttributes;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ReskillablePotion {
    public static final Potion POTION_RESIST_EFFECT = new PotionResist("damage_resist", false, 5925256, 0, 0)
            .registerPotionAttributeModifier(ReskillableAttributes.DAMAGE_RESIST, MathHelper.getRandomUUID().toString(), 10.0D, 0);
    public static final Potion POTION_DARKNESS_EFFECT = new PotionDarkness("darkness", false, 738644, 1, 0)
            .registerPotionAttributeModifier(ReskillableAttributes.CRIT_DAMAGE, MathHelper.getRandomUUID().toString(), 0.5D, 2);
    public static final Potion POTION_EDGE_EFFECT = new PotionEdge("edge", false, 738644, 2, 0)
            .registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, MathHelper.getRandomUUID().toString(), 0.2D, 2)
            .registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, MathHelper.getRandomUUID().toString(), 0.5D, 2)
            .registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, MathHelper.getRandomUUID().toString(), 0.1D, 2);


    public static final PotionType POTION_RESIST = new PotionType("damage_resist", new PotionEffect[] {new PotionEffect(POTION_RESIST_EFFECT, 2400)}).setRegistryName("damageResist");
    public static final PotionType LONG_POTION_RESIST = new PotionType("damage_resist", new PotionEffect[] {new PotionEffect(POTION_RESIST_EFFECT, 4800)}).setRegistryName("long_damageResist");
    public static final PotionType POTION_DARKNESS = new PotionType("darkness", new PotionEffect[] {new PotionEffect(POTION_DARKNESS_EFFECT, 2400)}).setRegistryName("darkness");
    public static final PotionType LONG_POTION_DARKNESS = new PotionType("darkness", new PotionEffect[] {new PotionEffect(POTION_DARKNESS_EFFECT, 4800)}).setRegistryName("long_darkness");
    public static final PotionType POTION_EDGE = new PotionType("edge", new PotionEffect[] {new PotionEffect(POTION_EDGE_EFFECT, 2400)}).setRegistryName("edge");
    public static final PotionType LONG_POTION_EDGE = new PotionType("edge", new PotionEffect[] {new PotionEffect(POTION_EDGE_EFFECT, 4800)}).setRegistryName("long_edge");


    public static void registerPotions() {
        registerPotion(POTION_DARKNESS, LONG_POTION_DARKNESS, POTION_DARKNESS_EFFECT);
        registerPotion(POTION_RESIST, LONG_POTION_RESIST, POTION_RESIST_EFFECT);
        registerPotion(POTION_EDGE, LONG_POTION_EDGE, POTION_EDGE_EFFECT);
    }

    public static void registerPotion(PotionType defaultPotion, PotionType longPotion, Potion effect) {
        ForgeRegistries.POTIONS.register(effect);
        ForgeRegistries.POTION_TYPES.register(defaultPotion);
        ForgeRegistries.POTION_TYPES.register(longPotion);
    }
}
