package codersafterdark.reskillable.advancement;

import codersafterdark.reskillable.advancement.professionlevel.ProfessionLevelTrigger;
import codersafterdark.reskillable.advancement.skilllevel.SkillLevelTrigger;
import codersafterdark.reskillable.advancement.trait.UnlockUnlockableTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class ReskillableAdvancements {
    public static final ProfessionLevelTrigger PROFESSION_LEVEL = new ProfessionLevelTrigger();
    public static final SkillLevelTrigger SKILL_LEVEL = new SkillLevelTrigger();
    public static final UnlockUnlockableTrigger UNLOCK_UNLOCKABLE = new UnlockUnlockableTrigger();

    public static void preInit() {
        CriteriaTriggers.register(PROFESSION_LEVEL);
        CriteriaTriggers.register(SKILL_LEVEL);
        CriteriaTriggers.register(UNLOCK_UNLOCKABLE);
    }
}
