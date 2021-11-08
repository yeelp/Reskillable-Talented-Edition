package codersafterdark.reskillable.common.skill.traits.farming;

    import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.unlockable.Trait;
import net.minecraft.util.ResourceLocation;

public class TraitSeasons extends Trait {

    public TraitSeasons() {
        super(new ResourceLocation(MOD_ID, "seasonal_greetings"), 1, 2, new ResourceLocation(MOD_ID, "farming"),
                6, "reskillable:farming|12");
        setIcon(new ResourceLocation("sereneseasons:textures/items/ss_icon.png"));
    }

}
