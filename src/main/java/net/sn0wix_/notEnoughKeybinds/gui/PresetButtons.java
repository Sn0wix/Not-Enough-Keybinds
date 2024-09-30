package net.sn0wix_.notEnoughKeybinds.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.mixin.ControlsListWidgetAccessor;

import java.util.List;

public class PresetButtons extends ControlsListWidget.CategoryEntry {
    public final ButtonWidget currentPreset;
    public final ButtonWidget presetSettings;
    int maxKeyNameLength;

    public PresetButtons(ControlsListWidget widget) {
        widget.super(Text.empty());
        maxKeyNameLength = ((ControlsListWidgetAccessor) widget).getMaxKeyNameLength();
        int size = (maxKeyNameLength + 150) / 2;

        currentPreset = ButtonWidget.builder(Text.literal("Current Preset: none"), button1 ->{}).size(size, 20).build();
        presetSettings = ButtonWidget.builder(Text.literal("Preset Settings..."), button1 ->{}).size(size, 20).build();
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        currentPreset.setPosition(x + 90 - maxKeyNameLength, y);
        currentPreset.render(context, mouseX, mouseY, tickDelta);

        presetSettings.setPosition(x + (maxKeyNameLength % 2) + 90 - maxKeyNameLength + (maxKeyNameLength + 150) / 2, y);
        presetSettings.render(context, mouseX, mouseY, tickDelta);
    }

    @Override
    public List<? extends Element> children() {
        return List.of(currentPreset, presetSettings);
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return List.of(currentPreset, presetSettings);
    }
}
