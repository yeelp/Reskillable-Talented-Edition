package codersafterdark.reskillable.common.profession.warrior.warden;

import codersafterdark.reskillable.api.event.LockTalentEvent;
import codersafterdark.reskillable.api.event.UnlockTalentEvent;
import codersafterdark.reskillable.api.talent.Talent;
import com.fantasticsource.dynamicstealth.server.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentAura extends Talent {
    private UUID modifierSightID = null;
    private UUID modifierThreatID = null;

    public TalentAura() {
        super(new ResourceLocation(MOD_ID, "aura"), 0, 4, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "warden"),
                3, "reskillable:attack 10", "reskillable:defense 8");
        setCap(5);
        this.setIcon(new ResourceLocation("textures/items/iron_sword.png"));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onUnlock(UnlockTalentEvent.Post event) {
        EntityPlayer player = event.getEntityPlayer();
        if (event.getTalent() instanceof TalentAura) {
            player.sendMessage(new TextComponentString("Unlocked " + event.getTalent().getName()));

            IAttributeInstance threatFromAttacks = player.getEntityAttribute(Attributes.THREATGEN_ATTACK);
            IAttributeInstance visionCone = player.getEntityAttribute(Attributes.SIGHT);
            AttributeModifier modifierSight = new AttributeModifier("dynamicstealth.sight", 500.0, 0);
            AttributeModifier modifierThreat = new AttributeModifier("dynamicstealth.threatGenAttackedBySame", 100.0, 0);
            modifierSightID = modifierSight.getID();
            modifierThreatID = modifierThreat.getID();
            threatFromAttacks.applyModifier(modifierThreat);
            visionCone.applyModifier(modifierSight);
        }
    }

    @SubscribeEvent
    public void onLock(LockTalentEvent.Post event) {
        EntityPlayer player = event.getEntityPlayer();
        if (event.getTalent() instanceof TalentAura) {
            player.sendMessage(new TextComponentString("Unlocked " + event.getTalent().getName()));
            IAttributeInstance threatFromAttacks = player.getEntityAttribute(Attributes.THREATGEN_ATTACK);
            IAttributeInstance visionCone = player.getEntityAttribute(Attributes.SIGHT);
            threatFromAttacks.removeModifier(modifierThreatID);
            visionCone.removeModifier(modifierSightID);
        }
    }
}
