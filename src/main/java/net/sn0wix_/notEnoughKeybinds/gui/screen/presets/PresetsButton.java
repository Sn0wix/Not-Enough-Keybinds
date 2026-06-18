package net.sn0wix_.notEnoughKeybinds.gui.screen.presets;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;
import net.sn0wix_.notEnoughKeybinds.keybinds.presets.PresetLoader;
import net.sn0wix_.notEnoughKeybinds.mixin.ControlsListWidgetAccessor;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

import java.util.List;

public class PresetsButton extends KeyBindsList.CategoryEntry {
    public final Button presetSettings;
    public final Font textRenderer;

    public PresetsButton(KeyBindsList widget, Font renderer) {
        widget.super(new KeyMapping.Category(Identifier.parse("")));
        this.textRenderer = renderer;

        presetSettings = Button.builder(TextUtils.getText("presets"), button1 ->
                        Minecraft.getInstance().setScreen(new PresetsSettingScreen(((ControlsListWidgetAccessor) widget).getParent())))
                .size(132, 20).build();
    }

    @Override
    public void extractContent(GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        String textToRender = TextUtils.getText("current_preset").getString() + PresetLoader.getCurrentPresetName();

        int x = getContentX();

        //location of the presets button
        int buttonPos = x + getContentWidth() / 2 - 340 / 2 + 340 - presetSettings.getWidth();
        int textLength = textRenderer.width(textToRender) + 20;
        int padding = 5;

        try {
            if (buttonPos - x - padding <= textLength) {
                //the text is larger than the available space
                x = buttonPos - textLength + padding * 3;

                if (textLength > buttonPos - padding) {
                    //the text is so large, it doesn't fit on the screen
                    StringBuilder builder = new StringBuilder();

                    for (int i = 0; textRenderer.width(builder + "...") <= buttonPos - padding * 2; i++) {
                        builder.append(textToRender.charAt(i));
                    }

                    textToRender = builder + "...";
                    x = padding;
                }
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

        context.text(textRenderer, textToRender, x, getContentY() + getContentHeight() / 2 - 9 / 2, CommonColors.GRAY, true);

        presetSettings.setPosition(buttonPos, getContentY());
        presetSettings.extractRenderState(context, mouseX, mouseY, deltaTicks);
    }


    @Override
    public List<? extends GuiEventListener> children() {
        return List.of(presetSettings);
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return List.of(presetSettings);
    }
}
