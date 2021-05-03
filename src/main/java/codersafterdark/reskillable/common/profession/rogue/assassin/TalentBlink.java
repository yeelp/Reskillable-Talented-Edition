package codersafterdark.reskillable.common.profession.rogue.assassin;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.event.UnlockTalentEvent;
import codersafterdark.reskillable.api.talent.TalentActive;
import codersafterdark.reskillable.client.gui.handler.KeyBindings;
import codersafterdark.reskillable.common.network.BlinkPacket;
import codersafterdark.reskillable.common.network.PacketHandler;
import codersafterdark.reskillable.common.util.Utils;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.util.RayTracer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import javax.vecmath.Vector3d;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentBlink extends TalentActive {

    public TalentBlink() {
        super(new ResourceLocation(MOD_ID, "blink"), 0, 2, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "assassin"),
                3, 40, "profession|reskillable:rogue|6", "reskillable:agility|8");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onUnlock(UnlockTalentEvent event) {
        if (event.getTalent() instanceof TalentBlink) {
            setCooldown(0);
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;

        if (PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)) {
            activateTalent(KeyBindings.keyClassSpell);
        }
    }

    public void activateTalent(KeyBinding binding) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (binding.isPressed() && getCooldown() == 0) {
            blink(player);
            triggerCooldown();
        }
    }

    public void triggerCooldown() {
        this.setCooldown(getDefaultCooldown());
    }

    public boolean blink(EntityPlayer player) {
        World world = player.world;
        //if (world.isRemote) return false;

        if (cast(world, player)) {
            PacketHandler.INSTANCE.sendToServer(new BlinkPacket());
            return true;
        }

        return false;
    }

    public boolean cast(World world, EntityPlayer player) {
        RayTraceResult rayTrace = RayTracer.standardBlockRayTrace(world, player, 16.0D, false);
        if (world.isRemote) {
            for(int i = 0; i < 10; ++i) {
                double dx = player.posX;
                double dy = player.getEntityBoundingBox().minY + (double)(2.0F * world.rand.nextFloat());
                double dz = player.posZ;
                world.spawnParticle(EnumParticleTypes.PORTAL, dx, dy, dz, world.rand.nextDouble() - 0.5D, world.rand.nextDouble() - 0.5D, world.rand.nextDouble() - 0.5D, new int[0]);
            }
            Wizardry.proxy.playBlinkEffect(player);
        }

        Vector3d eye = Utils.getEyePosition(player);
        Vector3d look = Utils.getLookVec(player);

        Vector3d sample = new Vector3d(look);

        sample.scale(16D);
        sample.add(eye);
        Vec3d eye3 = new Vec3d(eye.x, eye.y, eye.z);
        Vec3d end = new Vec3d(sample.x, sample.y, sample.z);

        RayTraceResult p = player.world.rayTraceBlocks(eye3, end, true);
        if (p == null) {

            player.playSound(codersafterdark.reskillable.common.registry.ReskillableSounds.TALENT_BLINK, 10, 1);
            player.setPositionAndUpdate(end.x, end.y, end.z);
        }

        if (p != null && p.typeOfHit == RayTraceResult.Type.ENTITY) {
                        EntityLivingBase entity = (EntityLivingBase) p.entityHit;
            BlockPos pos = entity.getPosition();
            player.sendMessage(new TextComponentString("Entity struck!"));

            if (!world.isRemote) {
                player.setPositionAndUpdate(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
            }
            player.playSound(codersafterdark.reskillable.common.registry.ReskillableSounds.TALENT_BLINK, 10, 1);
            return true;
        }

        if (rayTrace != null && rayTrace.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos pos = rayTrace.getBlockPos();
            if (rayTrace.sideHit == EnumFacing.DOWN) {
                pos = pos.down();
            }
            player.sendMessage(new TextComponentString("reskillable:textures/talents/" + getSubProfession().getUnformattedName() + "/"));

            pos = pos.offset(rayTrace.sideHit);
            if (!world.getBlockState(pos).getMaterial().blocksMovement() && !world.getBlockState(pos.up()).getMaterial().blocksMovement()) {
                player.playSound(codersafterdark.reskillable.common.registry.ReskillableSounds.TALENT_BLINK, 10, 1);
                if (!world.isRemote) {
                    player.setPositionAndUpdate((double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D);
                }

                player.playSound(codersafterdark.reskillable.common.registry.ReskillableSounds.TALENT_BLINK, 10, 1);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}

