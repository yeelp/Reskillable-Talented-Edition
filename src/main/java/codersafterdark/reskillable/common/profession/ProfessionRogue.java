package codersafterdark.reskillable.common.profession;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.profession.Profession;
import net.minecraft.util.ResourceLocation;

public class ProfessionRogue extends Profession {
    public ProfessionRogue() {
        super(new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "textures/gui/profession_bg/adamantium_block.png"));
        this.setGuiIndex(2);
        setColor(15456528);
        addSubProfession("assassin", 0);
        addSubProfession("trickster", 1);
        addSubProfession("archer", 2);
    }
}
