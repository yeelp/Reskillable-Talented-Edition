package codersafterdark.reskillable.common.profession.warrior.warden;

import codersafterdark.reskillable.api.event.LockTalentEvent;
import codersafterdark.reskillable.api.event.UnlockTalentEvent;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.common.skill.attributes.ReskillableAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentWardenNovice extends Talent {
    private UUID modifierID = null;

    public TalentWardenNovice() {
        super(new ResourceLocation(MOD_ID, "warden_novice"), 1, 4, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "warden"),
                3, "reskillable:attack 10", "reskillable:defense 8");
        setCap(1);
        MinecraftForge.EVENT_BUS.register(this);
    }

    //AttributeModifier modifier = new AttributeModifier("generic.armor", 1.0F, 0);
    IAttribute damageResist = ReskillableAttributes.DAMAGE_RESIST;

    @SubscribeEvent
    public void onUnlock(UnlockTalentEvent.Post event) {
        EntityPlayer player = event.getEntityPlayer();
        if (event.getTalent() instanceof TalentWardenNovice) {
            player.sendMessage(new TextComponentString("Unlocked " + event.getTalent().getName()));
            //IAttributeInstance armor = player.getEntityAttribute(SharedMonsterAttributes.ARMOR);
            //armor.applyModifier(modifier);

            IAttributeInstance AttributeDamageResist = player.getEntityAttribute(damageResist);
            AttributeModifier modifier = new AttributeModifier("reskillable.damageResistance", 5.0, 0);
            modifierID = modifier.getID();
            AttributeDamageResist.applyModifier(modifier);

        }
    }

    @SubscribeEvent
    public void onLock(LockTalentEvent.Post event) {
        EntityPlayer player = event.getEntityPlayer();
        if (event.getTalent() instanceof TalentWardenNovice) {
            player.sendMessage(new TextComponentString("Unlocked " + event.getTalent().getName()));
            //IAttributeInstance armor = player.getEntityAttribute(SharedMonsterAttributes.ARMOR);
            IAttributeInstance AttributeDamageResist = player.getEntityAttribute(damageResist);
            AttributeDamageResist.removeModifier(modifierID);
        }
    }
}
