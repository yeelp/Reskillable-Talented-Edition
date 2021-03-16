package codersafterdark.reskillable.profession.warrior;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TalentFrenzy extends Talent {

    public TalentFrenzy() {
        super(new ResourceLocation(MOD_ID, "frenzy"), 1, 4, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "berserker"),
                3, "reskillable:attack 10", "reskillable:defense 8");
        setCap(5);
        this.setIcon(new ResourceLocation("textures/items/iron_sword.png"));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onAttack(LivingDamageEvent event) {
        if ((event.getSource().getTrueSource() instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            if (!PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)) {return;}
            float health = player.getHealth();
            float maxHealth = player.getMaxHealth();
            float damage = event.getAmount();
            float healAmount = damage * 0.15f;
            if (health < maxHealth) {
                player.heal(Math.round(healAmount));
            }
        }
    }
}
