package codersafterdark.reskillable.common.profession;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.profession.Profession;
import net.minecraft.util.ResourceLocation;

public class ProfessionMage extends Profession {
    public ProfessionMage() {
        super(new ResourceLocation(MOD_ID, "mage"), new ResourceLocation("textures/blocks/bookshelf.png"));
        this.setGuiIndex(4);
        setColor(1143510);
        addSubProfession("wizard", 0);
        addSubProfession("monk", 1);
        addSubProfession("alchemist", 2);
    }
}
