package codersafterdark.reskillable.item;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.event.LevelUpEvent;
import codersafterdark.reskillable.base.ReskillableSounds;
import codersafterdark.reskillable.lib.LibMisc;
import dynamicswordskills.api.SkillRegistry;
import dynamicswordskills.entity.DSSPlayerInfo;
import dynamicswordskills.skills.SkillBase;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class ItemScrollRespec extends Item {

    public ItemScrollRespec() {
        setTranslationKey(LibMisc.MOD_ID + ".scroll_respec");
        setRegistryName(new ResourceLocation(LibMisc.MOD_ID, "scroll_respec"));
        setCreativeTab(CreativeTabs.MISC);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        PlayerData data = PlayerDataHandler.get(player);
        Collection<PlayerSkillInfo> allSkills = data.getAllSkillInfo();
        StringBuilder failedSkills = new StringBuilder();
        Iterator var8 = allSkills.iterator();

        if (world.isRemote) {
            while (var8.hasNext()) {
                PlayerSkillInfo skillInfo = (PlayerSkillInfo)var8.next();
                int oldLevel = skillInfo.getLevel();
                if (!MinecraftForge.EVENT_BUS.post(new LevelUpEvent.Pre(player, skillInfo.skill, 1, oldLevel))) {
                    skillInfo.setLevel(1);
                    skillInfo.respec();
                    MinecraftForge.EVENT_BUS.post(new LevelUpEvent.Post(player, skillInfo.skill, 1, oldLevel));
                    //ToastHelper.sendSkillToast(player, skillInfo.skill, 1);
                } else {
                    failedSkills.append(skillInfo.skill.getName()).append(", ");
                }
            }
            data.saveAndSync();

            if (Loader.isModLoaded("dynamicswordskills")) {
                DSSPlayerInfo playerSkills = DSSPlayerInfo.get(player);
                for (SkillBase skill : SkillRegistry.getValues()) {
                    playerSkills.removeSkill(skill.getRegistryName().toString());
                }
            }

            if (failedSkills.length() == 0) {
                player.sendMessage(new TextComponentTranslation("reskillable.misc.scroll.message.success"));
            } else {
                player.sendMessage(new TextComponentTranslation("reskillable.misc.scroll.message.fail"));
            }
        }

        SoundEvent sfx = ReskillableSounds.getSound("reskillable:use_scroll_respec", ReskillableSounds.respec);
        player.playSound(sfx, 1F, (float) (0.7 + Math.random() * 0.4));

        stack.shrink(1);
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }

}
