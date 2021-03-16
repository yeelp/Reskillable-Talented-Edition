package codersafterdark.reskillable;

import codersafterdark.reskillable.advancement.ReskillableAdvancements;
import codersafterdark.reskillable.api.event.LevelUpEvent;
import codersafterdark.reskillable.api.event.LevelUpProfessionEvent;
import codersafterdark.reskillable.api.event.UnlockUnlockableEvent;
import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import codersafterdark.reskillable.profession.*;
import codersafterdark.reskillable.profession.warrior.TalentFrenzy;
import codersafterdark.reskillable.profession.warrior.TalentRally;
import codersafterdark.reskillable.skill.*;
import codersafterdark.reskillable.skill.agility.TraitHillWalker;
import codersafterdark.reskillable.skill.agility.TraitRoadWalk;
import codersafterdark.reskillable.skill.agility.TraitSidestep;
import codersafterdark.reskillable.skill.attack.TraitBattleSpirit;
import codersafterdark.reskillable.skill.attack.TraitNeutralissse;
import codersafterdark.reskillable.skill.building.TraitPerfectRecover;
import codersafterdark.reskillable.skill.building.TraitTransmutation;
import codersafterdark.reskillable.skill.defense.TraitEffectTwist;
import codersafterdark.reskillable.skill.defense.TraitUndershirt;
import codersafterdark.reskillable.skill.farming.TraitGreenThumb;
import codersafterdark.reskillable.skill.farming.TraitHungryFarmer;
import codersafterdark.reskillable.skill.farming.TraitMoreWheat;
import codersafterdark.reskillable.skill.farming.TraitSeasons;
import codersafterdark.reskillable.skill.gathering.TraitDropGuarantee;
import codersafterdark.reskillable.skill.gathering.TraitLuckyFisherman;
import codersafterdark.reskillable.skill.magic.TraitGoldenOsmosis;
import codersafterdark.reskillable.skill.magic.TraitSafePort;
import codersafterdark.reskillable.skill.mining.TraitFossilDigger;
import codersafterdark.reskillable.skill.mining.TraitObsidianSmasher;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.RegistryBuilder;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class ReskillableRegistryHandler {
    @SubscribeEvent
    public static void buildRegistry(RegistryEvent.NewRegistry newRegistryEvent) {
        new RegistryBuilder<Profession>().setName(new ResourceLocation(MOD_ID, "profession")).setType(Profession.class).create();
        new RegistryBuilder<Skill>().setName(new ResourceLocation(MOD_ID, "skill")).setType(Skill.class).create();
        new RegistryBuilder<Unlockable>().setName(new ResourceLocation(MOD_ID, "unlockable")).setType(Unlockable.class).create();
        new RegistryBuilder<Talent>().setName(new ResourceLocation(MOD_ID, "talent")).setType(Talent.class).create();
    }

    @SubscribeEvent
    public static void registerProfessions(RegistryEvent.Register<Profession> professionRegister) {
        professionRegister.getRegistry().registerAll(
                new ProfessionWarrior(),
                new ProfessionRogue(),
                new ProfessionMage(),
                new ProfessionFarmer(),
                new ProfessionGatherer(),
                new ProfessionTinkerer()
                );
    }

    @SubscribeEvent
    public static void registerSkills(RegistryEvent.Register<Skill> skillRegister) {
        skillRegister.getRegistry().registerAll(
                new SkillMining(),
                new SkillGathering(),
                new SkillAttack(),
                new SkillDefense(),
                new SkillBuilding(),
                new SkillFarming(),
                new SkillAgility(),
                new SkillMagic()
        );
    }

    @SubscribeEvent
    public static void registerTraits(RegistryEvent.Register<Unlockable> unlockablesRegister) {
        unlockablesRegister.getRegistry().registerAll(
                new TraitHillWalker(),
                new TraitRoadWalk(),
                new TraitSidestep(),
                new TraitBattleSpirit(),
                new TraitNeutralissse(),
                new TraitPerfectRecover(),
                new TraitTransmutation(),
                new TraitEffectTwist(),
                new TraitUndershirt(),
                new TraitGreenThumb(),
                new TraitHungryFarmer(),
                new TraitMoreWheat(),
                new TraitDropGuarantee(),
                new TraitLuckyFisherman(),
                new TraitGoldenOsmosis(),
                new TraitSafePort(),
                new TraitFossilDigger(),
                new TraitObsidianSmasher()
                // NEW TRAITS
        );

        if (Loader.isModLoaded("sereneseasons")) {
            unlockablesRegister.getRegistry().register(new TraitSeasons());
        }
    }

    @SubscribeEvent
    public static void registerTalents(RegistryEvent.Register<Talent> talentRegister) {
        talentRegister.getRegistry().registerAll(
                new TalentRally(),
                new TalentFrenzy()
        );
    }

    @SubscribeEvent
    public static void professionAdvancementHandling(LevelUpProfessionEvent.Post postEvent) {
        if (postEvent.getEntityPlayer() instanceof EntityPlayerMP) {
            ReskillableAdvancements.PROFESSION_LEVEL.trigger((EntityPlayerMP) postEvent.getEntityPlayer(),
                postEvent.getProfession(), postEvent.getLevel());
        }
    }

    @SubscribeEvent
    public static void skillAdvancementHandling(LevelUpEvent.Post postEvent) {
        if (postEvent.getEntityPlayer() instanceof EntityPlayerMP) {
            ReskillableAdvancements.SKILL_LEVEL.trigger((EntityPlayerMP) postEvent.getEntityPlayer(),
                    postEvent.getSkill(), postEvent.getLevel());
        }
    }

    @SubscribeEvent
    public static void unlockableAdvancementHandling(UnlockUnlockableEvent.Post event) {
        if (event.getEntityPlayer() instanceof EntityPlayerMP) {
            ReskillableAdvancements.UNLOCK_UNLOCKABLE.trigger((EntityPlayerMP) event.getEntityPlayer(), event.getUnlockable());
        }
    }
}