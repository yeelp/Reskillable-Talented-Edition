package codersafterdark.reskillable.client.gui.button;

import javax.annotation.Nullable;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IToolTipProvider {
    @Nullable
    @SideOnly(Side.CLIENT)
    ToolTip getToolTip(int mouseX, int mouseY);

    @SideOnly(Side.CLIENT)
    boolean isToolTipVisible();

    @SideOnly(Side.CLIENT)
    boolean isMouseOver(int mouseX, int mouseY);
}