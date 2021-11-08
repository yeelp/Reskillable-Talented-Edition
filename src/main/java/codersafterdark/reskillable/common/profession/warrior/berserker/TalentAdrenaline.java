package codersafterdark.reskillable.common.profession.warrior.berserker;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.event.LockTalentEvent;
import codersafterdark.reskillable.api.event.UnlockTalentEvent;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.common.profession.rogue.assassin.TalentShadow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import zabi.minecraft.extraalchemy.potion.PotionReference;

public class TalentAdrenaline extends Talent {
    public TalentAdrenaline() {
        super(new ResourceLocation(MOD_ID, "adrenaline"), 2, 1, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "berserker"),
                3, "profession|reskillable:warrior|26");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SuppressWarnings("static-method")
	@SubscribeEvent
    public void onUnlock(UnlockTalentEvent event) {
        if (event.getTalent() instanceof TalentAdrenaline) {
            EntityPlayer player = event.getEntityPlayer();
            player.addPotionEffect(new PotionEffect(PotionReference.INSTANCE.CHEAT_DEATH_POTION, 6000));
        }
    }

    @SubscribeEvent
    public void playerTickHandler(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (!player.world.isRemote) {
            if (!PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)) {return;}
            if (event.phase != TickEvent.Phase.START) {return;}
            if (player.isPotionActive(PotionReference.INSTANCE.CHEAT_DEATH_POTION) || player.isPotionActive(PotionReference.INSTANCE.CHEAT_DEATH_POTION_ACTIVE)) {
                return;
            }

            if (player.ticksExisted % 40 == 0) {
                player.addPotionEffect(new PotionEffect(PotionReference.INSTANCE.CHEAT_DEATH_POTION, 6000));
            }
        }
    }

    @SuppressWarnings("static-method")
	@SubscribeEvent
    public void onLock(LockTalentEvent event) {
        if (event.getTalent() instanceof TalentShadow) {
            EntityPlayer player = event.getEntityPlayer();
            if (player.getActivePotionEffects().contains(new PotionEffect(PotionReference.INSTANCE.CHEAT_DEATH_POTION))) {
                player.removeActivePotionEffect(PotionReference.INSTANCE.CHEAT_DEATH_POTION);
            }
        }
    }

}
