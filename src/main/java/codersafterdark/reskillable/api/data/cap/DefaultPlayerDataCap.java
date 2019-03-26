package codersafterdark.reskillable.api.data.cap;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementCache;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Ability;
import codersafterdark.reskillable.api.unlockable.IAbilityEventHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Consumer;

public class DefaultPlayerDataCap implements IPlayerData {
    private static final String TAG_SKILLS_CMP = "SkillLevels";
    private boolean client;
    private RequirementCache requirementCache;
    public WeakReference<EntityPlayer> playerWR;
    private Map<Skill, PlayerSkillInfo> skillInfo = new HashMap<>();

    public DefaultPlayerDataCap(){}

    public DefaultPlayerDataCap(EntityPlayer player) {
        playerWR = new WeakReference<>(player);
        client = player.getEntityWorld().isRemote;
        requirementCache = RequirementCache.getCache(player);
        ReskillableRegistries.SKILLS.getValuesCollection().forEach(s -> skillInfo.put(s, new PlayerSkillInfo(s)));
    }

    @Override
    public PlayerSkillInfo getSkillInfo(Skill s) {
        return skillInfo.get(s);
    }

    @Override
    public Collection<PlayerSkillInfo> getAllSkillInfo() {
        return skillInfo.values();
    }

    @Override
    public boolean hasAnyAbilities() {
        return !getAllAbilities().isEmpty();
    }

    @Override
    public Set<Ability> getAllAbilities() {
        Set<Ability> set = new TreeSet<>();
        skillInfo.values().forEach(info -> info.addAbilities(set));
        return set;
    }

    @Override
    public boolean matchStats(RequirementHolder holder) {
        return playerWR.get() == null || holder.getRequirements().stream().allMatch(this::requirementAchieved);
    }

    @Override
    public boolean requirementAchieved(Requirement req) {
        return getRequirementCache().requirementAchieved(req);
    }

    @Override
    public RequirementCache getRequirementCache() {
        return requirementCache;
    }

    @Override
    public void tickPlayer(TickEvent.PlayerTickEvent event) {
        forEachEventHandler(h -> h.onPlayerTick(event));
    }

    @Override
    public void blockDrops(BlockEvent.HarvestDropsEvent event) {
        forEachEventHandler(h -> h.onBlockDrops(event));
    }

    @Override
    public void mobDrops(LivingDropsEvent event) {
        forEachEventHandler(h -> h.onMobDrops(event));
    }

    @Override
    public void breakSpeed(PlayerEvent.BreakSpeed event) {
        forEachEventHandler(h -> h.getBreakSpeed(event));
    }

    @Override
    public void attackMob(LivingHurtEvent event) {
        forEachEventHandler(h -> h.onAttackMob(event));
    }

    @Override
    public void hurt(LivingHurtEvent event) {
        forEachEventHandler(h -> h.onHurt(event));
    }

    @Override
    public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        forEachEventHandler(h -> h.onRightClickBlock(event));
    }

    @Override
    public void enderTeleport(EnderTeleportEvent event) {
        forEachEventHandler(h -> h.onEnderTeleport(event));
    }

    @Override
    public void killMob(LivingDeathEvent event) {
        forEachEventHandler(h -> h.onKillMob(event));
    }

    @Override
    public void forEachEventHandler(Consumer<IAbilityEventHandler> consumer) {
        skillInfo.values().forEach(info -> info.forEachEventHandler(consumer));
    }
}
