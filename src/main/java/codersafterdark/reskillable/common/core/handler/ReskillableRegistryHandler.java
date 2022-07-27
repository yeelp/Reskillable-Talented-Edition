package codersafterdark.reskillable.common.core.handler;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.event.LevelUpEvent;
import codersafterdark.reskillable.api.event.LevelUpProfessionEvent;
import codersafterdark.reskillable.api.event.UnlockUnlockableEvent;
import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import codersafterdark.reskillable.common.advancement.ReskillableAdvancements;
import codersafterdark.reskillable.common.profession.ProfessionFarmer;
import codersafterdark.reskillable.common.profession.ProfessionGatherer;
import codersafterdark.reskillable.common.profession.ProfessionMage;
import codersafterdark.reskillable.common.profession.ProfessionRogue;
import codersafterdark.reskillable.common.profession.ProfessionTinkerer;
import codersafterdark.reskillable.common.profession.ProfessionWarrior;
import codersafterdark.reskillable.common.profession.gatherer.seafarer.TalentNeptunium;
import codersafterdark.reskillable.common.profession.mage.alchemist.TalentAlchemicDrive;
import codersafterdark.reskillable.common.profession.mage.alchemist.TalentAlchemistTraining;
import codersafterdark.reskillable.common.profession.mage.alchemist.TalentEquivalentExchange;
import codersafterdark.reskillable.common.profession.mage.alchemist.TalentIndecisiveDrinker;
import codersafterdark.reskillable.common.profession.mage.alchemist.TalentIronHearted;
import codersafterdark.reskillable.common.profession.mage.alchemist.TalentQuickDrinker;
import codersafterdark.reskillable.common.profession.mage.alchemist.TalentSoftHearted;
import codersafterdark.reskillable.common.profession.mage.alchemist.TalentTrueExtension;
import codersafterdark.reskillable.common.profession.mage.monk.TalentMonk;
import codersafterdark.reskillable.common.profession.rogue.archer.TalentArcherAdvanced;
import codersafterdark.reskillable.common.profession.rogue.archer.TalentArcherMaster;
import codersafterdark.reskillable.common.profession.rogue.archer.TalentArcherNovice;
import codersafterdark.reskillable.common.profession.rogue.archer.TalentPenetrate;
import codersafterdark.reskillable.common.profession.rogue.archer.TalentResourceful;
import codersafterdark.reskillable.common.profession.rogue.archer.TalentSniper;
import codersafterdark.reskillable.common.profession.rogue.archer.TalentTrueShot;
import codersafterdark.reskillable.common.profession.rogue.archer.TalentTruthSeeker;
import codersafterdark.reskillable.common.profession.rogue.archer.TalentVantage;
import codersafterdark.reskillable.common.profession.rogue.assassin.TalentAssassinAdvanced;
import codersafterdark.reskillable.common.profession.rogue.assassin.TalentAssassinMaster;
import codersafterdark.reskillable.common.profession.rogue.assassin.TalentAssassinNovice;
import codersafterdark.reskillable.common.profession.rogue.assassin.TalentBlink;
import codersafterdark.reskillable.common.profession.rogue.assassin.TalentCritSlayer;
import codersafterdark.reskillable.common.profession.rogue.assassin.TalentDarkness;
import codersafterdark.reskillable.common.profession.rogue.assassin.TalentHemorrhage;
import codersafterdark.reskillable.common.profession.rogue.assassin.TalentPlungingSilence;
import codersafterdark.reskillable.common.profession.rogue.assassin.TalentShadow;
import codersafterdark.reskillable.common.profession.rogue.assassin.TalentSpringHeel;
import codersafterdark.reskillable.common.profession.rogue.trickster.TalentTrickster;
import codersafterdark.reskillable.common.profession.warrior.berserker.TalentAbsorption;
import codersafterdark.reskillable.common.profession.warrior.berserker.TalentAdrenaline;
import codersafterdark.reskillable.common.profession.warrior.berserker.TalentBerserkAdvanced;
import codersafterdark.reskillable.common.profession.warrior.berserker.TalentBerserkMaster;
import codersafterdark.reskillable.common.profession.warrior.berserker.TalentBerserkNovice;
import codersafterdark.reskillable.common.profession.warrior.berserker.TalentEdge;
import codersafterdark.reskillable.common.profession.warrior.berserker.TalentFear;
import codersafterdark.reskillable.common.profession.warrior.berserker.TalentFrenzy;
import codersafterdark.reskillable.common.profession.warrior.berserker.TalentJump;
import codersafterdark.reskillable.common.profession.warrior.gladiator.TalentGladiator;
import codersafterdark.reskillable.common.profession.warrior.warden.TalentAura;
import codersafterdark.reskillable.common.profession.warrior.warden.TalentBlocking;
import codersafterdark.reskillable.common.profession.warrior.warden.TalentMantra;
import codersafterdark.reskillable.common.profession.warrior.warden.TalentRally;
import codersafterdark.reskillable.common.profession.warrior.warden.TalentSacrifice;
import codersafterdark.reskillable.common.profession.warrior.warden.TalentVigilance;
import codersafterdark.reskillable.common.profession.warrior.warden.TalentWardenAdvanced;
import codersafterdark.reskillable.common.profession.warrior.warden.TalentWardenMaster;
import codersafterdark.reskillable.common.profession.warrior.warden.TalentWardenNovice;
import codersafterdark.reskillable.common.skill.SkillAgility;
import codersafterdark.reskillable.common.skill.SkillAttack;
import codersafterdark.reskillable.common.skill.SkillBuilding;
import codersafterdark.reskillable.common.skill.SkillDefense;
import codersafterdark.reskillable.common.skill.SkillFarming;
import codersafterdark.reskillable.common.skill.SkillGathering;
import codersafterdark.reskillable.common.skill.SkillMagic;
import codersafterdark.reskillable.common.skill.SkillMining;
import codersafterdark.reskillable.common.skill.traits.agility.TraitHillWalker;
import codersafterdark.reskillable.common.skill.traits.agility.TraitLuckyStrikes;
import codersafterdark.reskillable.common.skill.traits.agility.TraitMightyStrikes;
import codersafterdark.reskillable.common.skill.traits.agility.TraitPirouette;
import codersafterdark.reskillable.common.skill.traits.agility.TraitRisingCut;
import codersafterdark.reskillable.common.skill.traits.agility.TraitRoadWalk;
import codersafterdark.reskillable.common.skill.traits.agility.TraitShadowrush;
import codersafterdark.reskillable.common.skill.traits.agility.TraitSidestep;
import codersafterdark.reskillable.common.skill.traits.agility.TraitWhirlwind;
import codersafterdark.reskillable.common.skill.traits.attack.TraitBattleSpirit;
import codersafterdark.reskillable.common.skill.traits.attack.TraitCharge;
import codersafterdark.reskillable.common.skill.traits.attack.TraitEviscerate;
import codersafterdark.reskillable.common.skill.traits.attack.TraitFocus;
import codersafterdark.reskillable.common.skill.traits.attack.TraitLeapingStrike;
import codersafterdark.reskillable.common.skill.traits.attack.TraitNeutralissse;
import codersafterdark.reskillable.common.skill.traits.attack.TraitSunder;
import codersafterdark.reskillable.common.skill.traits.building.TraitPerfectRecover;
import codersafterdark.reskillable.common.skill.traits.building.TraitTransmutation;
import codersafterdark.reskillable.common.skill.traits.defense.TraitDeflect;
import codersafterdark.reskillable.common.skill.traits.defense.TraitEffectTwist;
import codersafterdark.reskillable.common.skill.traits.defense.TraitKamaitachi;
import codersafterdark.reskillable.common.skill.traits.defense.TraitMortalDraw;
import codersafterdark.reskillable.common.skill.traits.defense.TraitParry;
import codersafterdark.reskillable.common.skill.traits.defense.TraitUndershirt;
import codersafterdark.reskillable.common.skill.traits.farming.TraitGreenThumb;
import codersafterdark.reskillable.common.skill.traits.farming.TraitHungryFarmer;
import codersafterdark.reskillable.common.skill.traits.farming.TraitMoreWheat;
import codersafterdark.reskillable.common.skill.traits.farming.TraitSeasons;
import codersafterdark.reskillable.common.skill.traits.gathering.TraitDropGuarantee;
import codersafterdark.reskillable.common.skill.traits.gathering.TraitLuckyFisherman;
import codersafterdark.reskillable.common.skill.traits.magic.TraitGoldenOsmosis;
import codersafterdark.reskillable.common.skill.traits.magic.TraitSafePort;
import codersafterdark.reskillable.common.skill.traits.mining.TraitFossilDigger;
import codersafterdark.reskillable.common.skill.traits.mining.TraitObsidianSmasher;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class ReskillableRegistryHandler {
    @SubscribeEvent
    public static void buildRegistry(@SuppressWarnings("unused") RegistryEvent.NewRegistry newRegistryEvent) {
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
                new TraitObsidianSmasher(),

                // NEW TRAITS
                new TraitLuckyStrikes(),
                new TraitMightyStrikes()
        );

        if (Loader.isModLoaded("sereneseasons")) {
            unlockablesRegister.getRegistry().register(new TraitSeasons());
        }

        if (Loader.isModLoaded("dynamicswordskills")) {
            unlockablesRegister.getRegistry().registerAll(
                    new TraitCharge(),
                    new TraitEviscerate(),
                    new TraitFocus(),
                    new TraitLeapingStrike(),
                    new TraitSunder(),
                    new TraitPirouette(),
                    new TraitRisingCut(),
                    new TraitWhirlwind(),
                    new TraitShadowrush(),
                    new TraitDeflect(),
                    new TraitKamaitachi(),
                    new TraitMortalDraw(),
                    new TraitParry()
            );
        }
    }

    @SubscribeEvent
    public static void registerTalents(RegistryEvent.Register<Talent> talentRegister) {
        talentRegister.getRegistry().registerAll(
                new TalentWardenAdvanced(),
                new TalentBlocking(),
                new TalentMantra(),
                new TalentWardenMaster(),
                new TalentWardenNovice(),
                new TalentRally(),
                new TalentSacrifice(),
                new TalentVigilance(),

                new TalentBerserkAdvanced(),
                new TalentBerserkMaster(),
                new TalentBerserkNovice(),
                new TalentFear(),
                new TalentEdge(),
                new TalentFrenzy(),
                new TalentJump(),
                new TalentAbsorption(),

                new TalentGladiator(),

                new TalentHemorrhage(),
                new TalentPlungingSilence(),
                new TalentSpringHeel(),
                new TalentCritSlayer(),
                new TalentBlink(),

                new TalentTrickster(),

                new TalentArcherNovice(),
                new TalentArcherAdvanced(),
                new TalentArcherMaster(),
                new TalentPenetrate(),
                new TalentResourceful(),
                new TalentSniper(),
                new TalentTruthSeeker(),
                new TalentTrueShot(),
                new TalentVantage(),

                new TalentMonk(),
                
                new TalentTrueExtension(),
                new TalentIndecisiveDrinker(),
                new TalentAlchemicDrive(),
                new TalentEquivalentExchange(),
                new TalentIronHearted(),
                new TalentSoftHearted(),
                new TalentQuickDrinker(),
                new TalentAlchemistTraining.AlchemistNovice(),
                new TalentAlchemistTraining.AlchemistAdvanced(),
                new TalentAlchemistTraining.AlchemistMaster(),

                new TalentNeptunium()
        );

        if (Loader.isModLoaded("dynamicstealth")) {
            talentRegister.getRegistry().registerAll(
                    new TalentAura(),
                    new TalentDarkness(),
                    new TalentShadow(),
                    new TalentAssassinNovice(),
                    new TalentAssassinAdvanced(),
                    new TalentAssassinMaster()
            );
        }

        if (Loader.isModLoaded("extraalchemy")) {
            talentRegister.getRegistry().registerAll(
                    new TalentAdrenaline()
            );
        }
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