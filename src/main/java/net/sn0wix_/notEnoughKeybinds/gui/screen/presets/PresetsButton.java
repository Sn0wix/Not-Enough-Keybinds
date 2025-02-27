package net.sn0wix_.notEnoughKeybinds.gui.screen.presets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.sn0wix_.notEnoughKeybinds.keybinds.presets.PresetLoader;
import net.sn0wix_.notEnoughKeybinds.mixin.ControlsListWidgetAccessor;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

import java.util.List;

public class PresetsButton extends ControlsListWidget.CategoryEntry {
    public final Text currentPreset;
    public final ButtonWidget presetSettings;
    public final TextRenderer textRenderer;

    public PresetsButton(ControlsListWidget widget, TextRenderer renderer) {
        widget.super(Text.empty());
        this.textRenderer = renderer;

        currentPreset = Text.literal(TextUtils.getText("current_preset").getString() + PresetLoader.getCurrentPresetName());
        presetSettings = ButtonWidget.builder(TextUtils.getText("presets"), button1 ->
                        MinecraftClient.getInstance().setScreen(new PresetsSettingScreen(((ControlsListWidgetAccessor) widget).getParent())))
                .size(132, 20).build();
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        String textToRender = currentPreset.getString();

        //location of the presets button
        int buttonPos = x + entryWidth / 2 - 340 / 2 + 340 - presetSettings.getWidth();
        int textLength = textRenderer.getWidth(textToRender) + 20;
        int padding = 5;

        try {
            if (buttonPos - x - padding <= textLength) {
                //the text is so large, it doesn't fit on the screen
                StringBuilder builder = new StringBuilder();

                for (int i = 0; textRenderer.getWidth(builder + "...") <= buttonPos - x - padding; i++) {
                    builder.append(textToRender.charAt(i));
                }

                textToRender = builder + "...";
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

        context.drawText(textRenderer, textToRender, x, y + entryHeight / 2 - 9 / 2, Colors.GRAY, true);

        presetSettings.setPosition(buttonPos, y);
        presetSettings.render(context, mouseX, mouseY, tickDelta);
    }

    @Override
    public List<? extends Element> children() {
        return List.of(presetSettings);
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return List.of(presetSettings);
    }
}
