package codersafterdark.reskillable.client.gui.button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ToolTip {
    private final List<String> lines = new ArrayList<>();
    private final long delay;
    private long mouseOverStart;

    public ToolTip() {
        this.delay = 0;
    }

    public ToolTip(int delay) {
        this.delay = delay;
    }

    public void clear() {
        this.lines.clear();
    }

    public boolean add(String line) {
        return add(line, null);
    }

    public boolean add(String line, @Nullable TextFormatting formatting) {
        return this.lines.add(formatting != null ? formatting + line : line);
    }

    public boolean add(List<Object> lines) {
        boolean changed = false;
        for (Object line : lines) {
            if (line instanceof String) {
                changed |= add((String) line);
            }
        }
        return changed;
    }

    public List<String> getLines() {
        return Collections.unmodifiableList(this.lines);
    }

    public void onTick(boolean mouseOver) {
        if (this.delay == 0) {
            return;
        }
        if (mouseOver) {
            if (this.mouseOverStart == 0) {
                this.mouseOverStart = System.currentTimeMillis();
            }
        } else {
            this.mouseOverStart = 0;
        }
    }

    public boolean isReady() {
        return this.delay == 0 || this.mouseOverStart != 0 && System.currentTimeMillis() - this.mouseOverStart >= this.delay;
    }

    public void refresh() {
    	//no-op
    }
}