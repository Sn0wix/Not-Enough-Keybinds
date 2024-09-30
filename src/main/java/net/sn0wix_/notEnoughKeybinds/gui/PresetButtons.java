package net.sn0wix_.notEnoughKeybinds.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.keybinds.presets.PresetLoader;
import net.sn0wix_.notEnoughKeybinds.mixin.ControlsListWidgetAccessor;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

import java.util.List;

public class PresetButtons extends ControlsListWidget.CategoryEntry {
    public final TextWidget currentPreset;
    public final ButtonWidget presetSettings;
    int maxKeyNameLength;

    public PresetButtons(ControlsListWidget widget) {
        widget.super(Text.empty());
        maxKeyNameLength = ((ControlsListWidgetAccessor) widget).getMaxKeyNameLength();
        int size = (maxKeyNameLength + 150) / 2;

        currentPreset = new TextWidget(size, 20, Text.literal(TextUtils.getText("current_preset").getString() +
                (PresetLoader.getCurrentPresetName().length() > 10 ? PresetLoader.getCurrentPresetName().substring(0, 10) + "..." : PresetLoader.getCurrentPresetName())), MinecraftClient.getInstance().textRenderer);
        presetSettings = ButtonWidget.builder(TextUtils.getText("presets"), button1 -> {
        }).size(size - 15, 20).build();
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        currentPreset.setPosition(x + 90 - maxKeyNameLength, y);
        currentPreset.render(context, mouseX, mouseY, tickDelta);

        presetSettings.setPosition(x + (maxKeyNameLength % 2) + 105 - maxKeyNameLength + (maxKeyNameLength + 150) / 2, y);
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
