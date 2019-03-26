package codersafterdark.reskillable.api.data.cap;

import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementCache;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Ability;
import codersafterdark.reskillable.api.unlockable.IAbilityEventHandler;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public interface IPlayerData {
    PlayerSkillInfo getSkillInfo(Skill s);
    Collection<PlayerSkillInfo> getAllSkillInfo();
    boolean hasAnyAbilities();
    Set<Ability> getAllAbilities();
    boolean matchStats(RequirementHolder holder);
    boolean requirementAchieved(Requirement req);
    RequirementCache getRequirementCache();

    void tickPlayer(TickEvent.PlayerTickEvent event);
    void blockDrops(BlockEvent.HarvestDropsEvent event);
    void mobDrops(LivingDropsEvent event);
    void breakSpeed(PlayerEvent.BreakSpeed event);
    void attackMob(LivingHurtEvent event);
    void hurt(LivingHurtEvent event);
    void rightClickBlock(PlayerInteractEvent.RightClickBlock event);
    void enderTeleport(EnderTeleportEvent event);
    void killMob(LivingDeathEvent event);
    void forEachEventHandler(Consumer<IAbilityEventHandler> consumer);
}
