package codersafterdark.reskillable.item;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ReskillableItems {

    @GameRegistry.ObjectHolder("reskillable:scroll_respec")
    public static ItemScrollRespec scrollRespec;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        scrollRespec.initModel();
    }

    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(
                new ItemScrollRespec()
        );
    }
}
