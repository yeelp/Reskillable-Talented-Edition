package codersafterdark.reskillable.profession;

import codersafterdark.reskillable.api.profession.Profession;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class ProfessionFarmer extends Profession {
    public ProfessionFarmer() {
        super(new ResourceLocation(MOD_ID, "farmer"), new ResourceLocation("textures/blocks/dirt.png"));
        this.setGuiIndex(1);
        setColor(12213007);
        addSubProfession("rancher", 0);
        addSubProfession("chef", 2);

    }
}
