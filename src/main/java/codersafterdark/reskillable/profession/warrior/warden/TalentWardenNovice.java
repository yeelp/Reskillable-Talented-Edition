package codersafterdark.reskillable.profession.warrior.warden;

import codersafterdark.reskillable.api.event.LockTalentEvent;
import codersafterdark.reskillable.api.event.UnlockTalentEvent;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TalentWardenNovice extends Talent {
    public TalentWardenNovice() {
        super(new ResourceLocation(MOD_ID, "warden_novice"), 1, 4, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "warden"),
                3, "reskillable:attack 10", "reskillable:defense 8");
        setCap(1);
        this.setIcon(new ResourceLocation("textures/items/iron_sword.png"));
        MinecraftForge.EVENT_BUS.register(this);
    }

    AttributeModifier modifier = new AttributeModifier("generic.armor", 1.0F, 0);

    @SubscribeEvent
    public void onUnlock(UnlockTalentEvent.Post event) {
        EntityPlayer player = event.getEntityPlayer();
        //Side side = player.world.isRemote ? Side.CLIENT : Side.SERVER;
        if (event.getTalent() instanceof TalentWardenNovice) {
            player.sendMessage(new TextComponentString("Unlocked " + event.getTalent().getName()));
            IAttributeInstance armor = player.getEntityAttribute(SharedMonsterAttributes.ARMOR);
            armor.applyModifier(modifier);
        }
    }

    @SubscribeEvent
    public void onLock(LockTalentEvent.Post event) {
        EntityPlayer player = event.getEntityPlayer();
        //Side side = player.world.isRemote ? Side.CLIENT : Side.SERVER;
        if (event.getTalent() instanceof TalentWardenNovice) {
            player.sendMessage(new TextComponentString("Unlocked " + event.getTalent().getName()));
            IAttributeInstance armor = player.getEntityAttribute(SharedMonsterAttributes.ARMOR);
            armor.removeModifier(modifier);
        }
    }
}
