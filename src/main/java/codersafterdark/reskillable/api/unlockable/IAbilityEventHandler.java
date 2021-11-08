package codersafterdark.reskillable.api.unlockable;

import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public interface IAbilityEventHandler {
    default void onPlayerTick(@SuppressWarnings("unused") PlayerTickEvent event) {
    	//no-op
    }

    default void onBlockDrops(@SuppressWarnings("unused") HarvestDropsEvent event) {
    	//no-op
    }

    default void getBreakSpeed(@SuppressWarnings("unused") BreakSpeed event) {
    	//no-op
    }

    default void onMobDrops(@SuppressWarnings("unused") LivingDropsEvent event) {
    	//no-op
    }

    default void onAttackMob(@SuppressWarnings("unused") LivingHurtEvent event) {
    	//no-op
    }

    default void onHurt(@SuppressWarnings("unused") LivingHurtEvent event) {
    	//no-op
    }

    default void onRightClickBlock(@SuppressWarnings("unused") RightClickBlock event) {
    	//no-op
    }

    default void onEnderTeleport(@SuppressWarnings("unused") EnderTeleportEvent event) {
    	//no-op
    }

    default void onKillMob(@SuppressWarnings("unused") LivingDeathEvent event) {
    	//no-op
    }
}