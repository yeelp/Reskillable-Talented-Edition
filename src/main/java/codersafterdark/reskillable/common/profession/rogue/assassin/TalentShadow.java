package codersafterdark.reskillable.common.profession.rogue.assassin;

import codersafterdark.reskillable.api.event.LockTalentEvent;
import codersafterdark.reskillable.api.event.UnlockTalentEvent;
import codersafterdark.reskillable.api.talent.Talent;
import com.fantasticsource.dynamicstealth.server.Attributes;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentShadow extends Talent {
    private UUID speedID = null;
    private UUID threatID = null;
    private UUID stealthID = null;

    public TalentShadow() {
        super(new ResourceLocation(MOD_ID, "shadow"), 1, 4, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "assassin"),
                3, "reskillable:attack 10", "reskillable:agility 8");
        setCap(1);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onUnlock(UnlockTalentEvent event) {
        if (event.getTalent() instanceof TalentShadow) {
            EntityPlayer player = event.getEntityPlayer();

            IAttributeInstance speed = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            AttributeModifier modifierSpeed = new AttributeModifier("generic.movementSpeed", 0.05, 1);
            speedID = modifierSpeed.getID();
            speed.applyModifier(modifierSpeed);

            IAttributeInstance threat = player.getEntityAttribute(Attributes.THREATGEN);
            AttributeModifier modifierThreat = new AttributeModifier("dynamicstealth.threatGen", -100.0, 0);
            threatID = modifierThreat.getID();
            threat.applyModifier(modifierThreat);

            IAttributeInstance stealth = player.getEntityAttribute(Attributes.VISIBILITY_REDUCTION);
            AttributeModifier modifierStealth = new AttributeModifier("dynamicstealth.visibilityReduction", 250.0, 0);
            stealthID = modifierStealth.getID();
            stealth.applyModifier(modifierStealth);
        }
    }

    @SubscribeEvent
    public void onLock(LockTalentEvent event) {
        if (event.getTalent() instanceof TalentShadow) {
            EntityPlayer player = event.getEntityPlayer();
            IAttributeInstance speed = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            IAttributeInstance threat = player.getEntityAttribute(Attributes.THREATGEN);
            IAttributeInstance stealth = player.getEntityAttribute(Attributes.VISIBILITY_REDUCTION);
            speed.removeModifier(speedID);
            threat.removeModifier(threatID);
            stealth.removeModifier(stealthID);
        }
    }
}
