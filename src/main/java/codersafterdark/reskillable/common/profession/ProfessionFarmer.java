package codersafterdark.reskillable.common.profession;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import codersafterdark.reskillable.api.profession.Profession;
import net.minecraft.util.ResourceLocation;

public class ProfessionFarmer extends Profession {
    public ProfessionFarmer() {
        super(new ResourceLocation(MOD_ID, "farmer"), new ResourceLocation("textures/blocks/dirt.png"));
        this.setGuiIndex(1);
        setColor(12213007);
        addSubProfession("rancher", 0);
        addSubProfession("chef", 2);
        setOffense(false);
    }
}
