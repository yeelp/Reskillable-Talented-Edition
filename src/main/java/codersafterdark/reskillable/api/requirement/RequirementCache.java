package codersafterdark.reskillable.api.requirement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import codersafterdark.reskillable.api.event.CacheInvalidatedEvent;
import codersafterdark.reskillable.api.event.LevelUpEvent;
import codersafterdark.reskillable.api.event.LevelUpProfessionEvent;
import codersafterdark.reskillable.api.event.LockTalentEvent;
import codersafterdark.reskillable.api.event.LockUnlockableEvent;
import codersafterdark.reskillable.api.event.UnlockTalentEvent;
import codersafterdark.reskillable.api.event.UnlockUnlockableEvent;
import codersafterdark.reskillable.api.requirement.logic.DoubleRequirement;
import codersafterdark.reskillable.api.requirement.logic.OuterRequirement;
import codersafterdark.reskillable.api.requirement.logic.impl.NOTRequirement;
import codersafterdark.reskillable.common.network.InvalidateRequirementPacket;
import codersafterdark.reskillable.common.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class RequirementCache {
    private static Set<Class<? extends Requirement>> dirtyCacheTypes = new HashSet<>();
    private static Map<SidedUUID, RequirementCache> cacheMap = new HashMap<>();

    private Map<Class<? extends Requirement>, Map<Requirement, Boolean>> requirementCache = new HashMap<>();
    private Set<Class<? extends Requirement>> recentlyInvalidated = new HashSet<>();
    private boolean valid = true;
    private boolean isRemote;//true if client
    private boolean dirtyCache;
    private UUID uuid;

    /**
     * @deprecated I don't believe this is used by any external addons, but just in case only deprecating it for now.
     * Switch to using RequirementCache#getCache
     */
    @Deprecated
    public RequirementCache(@Nonnull EntityPlayer player) {
        this(player.getUniqueID(), player.getEntityWorld().isRemote);
        cacheMap.put(new SidedUUID(this.uuid, this.isRemote), this);
    }

    private RequirementCache(UUID uuid, boolean isClientPlayer) {
        this.uuid = uuid;
        this.isRemote = isClientPlayer;
    }

    public static RequirementCache getCache(@Nonnull EntityPlayer player) {
        return getCache(player.getUniqueID(), player.getEntityWorld().isRemote);
    }

    public static RequirementCache getCache(UUID uuid, boolean isRemote) {
        SidedUUID sidedUUID = new SidedUUID(uuid, isRemote);
        if (cacheMap.containsKey(sidedUUID)) {
            return cacheMap.get(sidedUUID);
        }
        RequirementCache cache = new RequirementCache(uuid, isRemote);
        cacheMap.put(sidedUUID, cache);
        return cache;
    }

    public static boolean hasCache(@Nonnull EntityPlayer player) {
        return hasCache(player.getUniqueID(), player.getEntityWorld().isRemote);
    }

    public static boolean hasCache(UUID uuid, boolean isRemote) {
        return cacheMap.containsKey(new SidedUUID(uuid, isRemote));
    }

    public static void registerDirtyTypes() {
        //Register logic requirements and any other implementations of DoubleRequirement to be invalidated
        registerRequirementType(NOTRequirement.class, DoubleRequirement.class);
    }

    //A method to allow adding of requirements that should always be invalidated if other requirements get invalidated
    @SafeVarargs
	public static void registerRequirementType(Class<? extends Requirement>... requirementClasses) {
        dirtyCacheTypes.addAll(Arrays.asList(requirementClasses));
    }

    @SafeVarargs
	public static void invalidateCache(EntityPlayer player, Class<? extends Requirement>... cacheTypes) {
        if (player != null) {
            invalidateCache(player.getUniqueID(), cacheTypes);
        }
    }

    //This method signature cannot be changed or it would cause crashes with some addons
    //TODO: Maybe make a method that allows for the sidedness to be stated that you want the cache invalidated for?
    //TODO Cont: invalidateCacheNoPacket may cover that already
    @SafeVarargs
	public static void invalidateCache(UUID uuid, Class<? extends Requirement>... cacheTypes) {
        boolean hasServer = hasCache(uuid, false);
        boolean hasClient = hasCache(uuid, true);
        if (hasServer) {
            invalidateCacheNoPacket(uuid, false, cacheTypes);
            if (!hasClient) {
                //Send packet to client
                RequirementCache cache = getCache(uuid, false);
                if (cache != null) {
                    EntityPlayer player = cache.getPlayer();
                    if (player != null) {
                        PacketHandler.INSTANCE.sendTo(new InvalidateRequirementPacket(uuid, cacheTypes), (EntityPlayerMP) player);
                    }
                }
            }
        }
        if (hasClient) {
            invalidateCacheNoPacket(uuid, true, cacheTypes);
            if (!hasServer) {
                //Send packet to server
                PacketHandler.INSTANCE.sendToServer(new InvalidateRequirementPacket(uuid, cacheTypes));
            }
        }
    }

    /**
     * @deprecated I don't believe this is used by any external addons, but just in case only deprecating it for now.
     */
    @SafeVarargs
	@Deprecated
    public static void invalidateCacheNoPacket(UUID uuid, Class<? extends Requirement>... cacheTypes) {
        invalidateCacheNoPacket(uuid, true, cacheTypes);
        invalidateCacheNoPacket(uuid, false, cacheTypes);
    }

    @SafeVarargs
	public static void invalidateCacheNoPacket(UUID uuid, boolean isRemote, Class<? extends Requirement>... cacheTypes) {
        getCache(uuid, isRemote).invalidateCache(cacheTypes);
    }

    public static boolean requirementAchieved(EntityPlayer player, Requirement requirement) {
        return player != null && requirementAchieved(player.getUniqueID(), player.getEntityWorld().isRemote, requirement);
    }

    /**
     * @deprecated I don't believe this is used by any external addons, but just in case only deprecating it for now.
     */
    @Deprecated
    public static boolean requirementAchieved(UUID uuid, Requirement requirement) {
        return requirementAchieved(uuid, true, requirement) || requirementAchieved(uuid, false, requirement);
    }

    public static boolean requirementAchieved(UUID uuid, boolean isRemote, Requirement requirement) {
        return getCache(uuid, isRemote).requirementAchieved(requirement);
    }

    @SubscribeEvent
    public static void onProfessionLevelChange(LevelUpProfessionEvent.Post event) {
        invalidateCache(event.getEntityPlayer().getUniqueID(), ProfessionRequirement.class);
    }

    @SubscribeEvent
    public static void onLevelChange(LevelUpEvent.Post event) {
        //Just invalidate all skills because it is easier than checking each requirement they have to see if the skill matches
        invalidateCache(event.getEntityPlayer().getUniqueID(), SkillRequirement.class);
    }

    @SubscribeEvent
    public static void onTalentLocked(LockTalentEvent.Post event) {
        invalidateCache(event.getEntityPlayer().getUniqueID(), TalentRequirement.class);
    }

    @SubscribeEvent
    public static void onUnlockableLocked(LockUnlockableEvent.Post event) {
        invalidateCache(event.getEntityPlayer().getUniqueID(), TraitRequirement.class);
    }

    @SubscribeEvent
    public static void onTalentUnlocked(UnlockTalentEvent.Post event) {
        invalidateCache(event.getEntityPlayer().getUniqueID(), TalentRequirement.class);
    }

    @SubscribeEvent
    public static void onUnlockableUnlocked(UnlockUnlockableEvent.Post event) {
        invalidateCache(event.getEntityPlayer().getUniqueID(), TraitRequirement.class);
    }

    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        invalidateCache(event.getEntityPlayer().getUniqueID(), AdvancementRequirement.class);
    }

    @SubscribeEvent
    public static void onDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
        removeCache(event.player.getUniqueID(), true);
        removeCache(event.player.getUniqueID(), false);
    }

    public static void removeCache(EntityPlayer player) {
        removeCache(player.getUniqueID(), player.getEntityWorld().isRemote);
    }

    public static void removeCache(UUID uuid, boolean isRemote) {
        SidedUUID sidedUUID = new SidedUUID(uuid, isRemote);
        if (cacheMap.containsKey(sidedUUID)) {
            cacheMap.get(sidedUUID).valid = false;
            cacheMap.remove(sidedUUID);
        }
    }

    @Nullable
    private EntityPlayer getPlayer() {
        if (this.isRemote) {
            WorldClient world = Minecraft.getMinecraft().world;
            return world == null ? null : world.getPlayerEntityByUUID(this.uuid);
        }
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        //Server should only be able to be null when isRemote is true, but just in case have this statement
        if (server != null) {
            for (WorldServer world : server.worlds) {
                EntityPlayer player = world.getPlayerEntityByUUID(this.uuid);
                if (player != null) {
                    return player;
                }
            }
        }
        return null;
    }

    //TODO: Use this in places to ensure that a new RequirementCache is not required.
    //TODO Cont: In places that use this they should probably throw an exception that says the state is invalid
    //TODO Cont: For example requirementAchieved
    public boolean isValid() {
        return this.valid;
    }

    /**
     * Force clears a cache, should not be used in most situations.
     *
     * Currently is only used when a client joins as the cache does not realize it is dirty.
     */
    public void forceClear() {
        if (isValid()) {
            EntityPlayer player = getPlayer();
            if (player != null) {
                this.requirementCache.clear();
                this.recentlyInvalidated.clear();
                this.dirtyCache = false;
                MinecraftForge.EVENT_BUS.post(new CacheInvalidatedEvent(player, true));
                if (!this.isRemote) {
                    PacketHandler.INSTANCE.sendTo(new InvalidateRequirementPacket(this.uuid), (EntityPlayerMP) player);
                }
            }
        }
    }

    public boolean requirementAchieved(Requirement requirement) {
        if (requirement == null || !isValid()) {
            return false;
        }
        EntityPlayer player = getPlayer();
        if (player == null) {
            return false;
        }

        if (!requirement.isCacheable()) {
            return requirement.achievedByPlayer(player);
        }

        Class<? extends Requirement> clazz = requirement.getClass();
        Map<Requirement, Boolean> cache;
        if (this.requirementCache.containsKey(clazz)) {
            cache = this.requirementCache.get(clazz);
            if (cache.containsKey(requirement)) {
                return cache.get(requirement);
            }
        } else {
            this.requirementCache.put(clazz, cache = new HashMap<>());
        }
        boolean achieved = requirement.achievedByPlayer(player);
        cache.put(requirement, achieved);
        if (!this.dirtyCache && dirtyCacheTypes.stream().anyMatch(dirtyType -> dirtyType.isInstance(requirement))) {
            this.dirtyCache = true;
        }
        //Remove the cached already invalidated types
        this.recentlyInvalidated.removeAll(this.recentlyInvalidated.stream().filter(type -> type.isInstance(requirement)).collect(Collectors.toList()));

        if (requirement instanceof OuterRequirement) {
            this.recentlyInvalidated.removeAll(((OuterRequirement) requirement).getInternalTypes());
        }

        return achieved;
    }

    public void invalidateCache(@SuppressWarnings("unchecked") Class<? extends Requirement>... cacheType) {
        EntityPlayer player = getPlayer();
        if (player == null) {
            //Do not fire off a cache invalidated event, as we do not know what player to fire it with
            //TODO: At some point maybe make it so that the CacheInvalidatedEvent can be fired via uuid instead of with a player
            return;
        }

        List<Class<? extends Requirement>> dirtyTypes = this.dirtyCache ? new ArrayList<>(dirtyCacheTypes) : new ArrayList<>();
        //Clear all types that are supposed to be invalidated each time if dirtyCache is true

        if (cacheType != null) {
            //If no classes of that type have been added do not bother invalidating it again.
            for (Class<? extends Requirement> type : cacheType) {
                if (!this.recentlyInvalidated.contains(type)) {
                    dirtyTypes.add(type);
                    this.recentlyInvalidated.add(type);
                }
            }
            if (dirtyTypes.size() == dirtyCacheTypes.size()) {
                //Nothing changed so the dirty types are not actually dirty and they aren't being directly invalidated because cacheType is not null
                MinecraftForge.EVENT_BUS.post(new CacheInvalidatedEvent(player, false));
                return;
            }
        }

        if (dirtyTypes.isEmpty()) {
            MinecraftForge.EVENT_BUS.post(new CacheInvalidatedEvent(player, false));
            return;
        }

        Set<Class<? extends Requirement>> requirements = this.requirementCache.keySet();
        List<Class<? extends Requirement>> toRemove = new ArrayList<>();

        for (Class<? extends Requirement> requirement : requirements) {
            for (Class<? extends Requirement> dirtyType : dirtyTypes) {
                if (dirtyType.isAssignableFrom(requirement)) {
                    toRemove.add(requirement);
                }
            }
        }
        toRemove.forEach(requirement -> this.requirementCache.remove(requirement));

        MinecraftForge.EVENT_BUS.post(new CacheInvalidatedEvent(player, !toRemove.isEmpty()));
    }

    private static class SidedUUID {
        private final UUID uuid;
        private final boolean isRemote; //true if client

        SidedUUID(UUID uuid, boolean isRemote) {
            this.uuid = uuid;
            this.isRemote = isRemote;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof SidedUUID) {
                SidedUUID other = (SidedUUID) obj;
                return this.isRemote == other.isRemote && this.uuid.equals(other.uuid);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.isRemote, this.uuid);
        }
    }
}