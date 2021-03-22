package codersafterdark.reskillable.common.skill.attributes;

import codersafterdark.reskillable.common.core.handler.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AttributeCritDamage {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onArrowCt(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityArrow) {
            EntityArrow arrow = (EntityArrow) event.getEntity();
            if (!arrow.getIsCritical()) return; //No crit

            if (arrow.shootingEntity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) arrow.shootingEntity;

                float critDamageMult = (float) player.getEntityAttribute(ReskillableAttributes.CRIT_DAMAGE).getAttributeValue();
                arrow.setDamage(arrow.getDamage() * critDamageMult);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onCrit(CriticalHitEvent event) {
        if (event.getResult() == Event.Result.ALLOW) {
            EntityPlayer player = event.getEntityPlayer();
            float critDamageMult = (float) player.getEntityAttribute(ReskillableAttributes.CRIT_DAMAGE).getAttributeValue();
            if (!player.world.isRemote) {
                player.sendMessage(new TextComponentString("Critical Damage Multiplier: " + MathHelper.round(critDamageMult, 2) + "%"));
            }
            float modifier = critDamageMult / 100F;
            event.setDamageModifier(modifier);
        }
    }
}
