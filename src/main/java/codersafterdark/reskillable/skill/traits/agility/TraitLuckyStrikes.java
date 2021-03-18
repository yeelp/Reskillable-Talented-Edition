package codersafterdark.reskillable.skill.traits.agility;

import codersafterdark.reskillable.api.event.UnlockUnlockableEvent;
import codersafterdark.reskillable.api.unlockable.Trait;
import codersafterdark.reskillable.lib.LibMisc;
import hellfirepvp.astralsorcery.common.constellation.perk.PerkAttributeHelper;
import hellfirepvp.astralsorcery.common.constellation.perk.attribute.PerkAttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class TraitLuckyStrikes extends Trait {
    public static final String TYPE_CRIT_CHANCE = "astralsorcery.critchance";

    public TraitLuckyStrikes() {
        super(new ResourceLocation(LibMisc.MOD_ID, "lucky_strikes"), 1, 3, new ResourceLocation("reskillable", "agility"), 1, "");
        MinecraftForge.EVENT_BUS.register(this);
    }

    PerkAttributeModifier modifier = new PerkAttributeModifier("astralsorcery.critchance", PerkAttributeModifier.Mode.ADDITION, 100.0F);

    @SubscribeEvent
    public void onUnlock(UnlockUnlockableEvent.Post event) {
        EntityPlayer player = event.getEntityPlayer();
        Side side = player.world.isRemote ? Side.CLIENT : Side.SERVER;
        if (event.getUnlockable() instanceof TraitLuckyStrikes) {
            player.sendMessage(new TextComponentString("Unlocked " + event.getUnlockable().getName()));
            PerkAttributeHelper.getOrCreateMap(player, side)
                    .applyModifier(player, TYPE_CRIT_CHANCE, modifier);
        }
    }
}
