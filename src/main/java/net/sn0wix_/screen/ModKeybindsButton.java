package net.sn0wix_.screen;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.sn0wix_.NotEnoughKeybinds;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ModKeybindsButton extends ControlsListWidget.CategoryEntry {
    public final ButtonWidget button;

    public ModKeybindsButton(ControlsListWidget widget) {
        widget.super(Text.literal("TEST"));

        button = ButtonWidget.builder(Text.literal("NotEnoughKeybinds"), button1 -> {
            NotEnoughKeybinds.LOGGER.info("PRESSED");
        }).dimensions(0,0,20,20).build();
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        super.render(context, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
        button.render(context, mouseX, mouseY, tickDelta);
        this.drawBorder(context, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
    }

    @Override
    public List<? extends Element> children() {
        return ImmutableList.of(button);
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return ImmutableList.of(button);
    }
}
