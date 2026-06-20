package net.sn0wix_.notEnoughKeybinds.gui.screen.presets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.sn0wix_.notEnoughKeybinds.keybinds.presets.PresetLoader;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;
import org.jetbrains.annotations.Nullable;

public class PresetsListWidget extends ObjectSelectionList<PresetsListWidget.PresetEntry> {
    public final PresetsSettingScreen parent;

    public PresetsListWidget(PresetsSettingScreen parent, Minecraft client, int width, int height, int y, int itemHeight) {
        super(client, width, height, y, itemHeight);
        this.parent = parent;
        initEntries();
    }

    public void initEntries() {
        clearEntries();
        PresetLoader.getPresets().forEach(keybindPreset -> addEntry(new PresetEntry(keybindPreset, minecraft.font)));
    }

    @Override
    public void setSelected(@Nullable PresetsListWidget.PresetEntry entry) {
        super.setSelected(entry);
        parent.updateScreen();
    }

    @Environment(EnvType.CLIENT)
    public class PresetEntry extends ObjectSelectionList.Entry<PresetEntry> implements AutoCloseable {
        public PresetLoader.KeybindPreset preset;
        public Font textRenderer;

        public PresetEntry(PresetLoader.KeybindPreset preset, Font textRenderer) {
            this.preset = preset;
            this.textRenderer = textRenderer;
        }

        @Override
        public void extractContent(GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            context.text(textRenderer, TextUtils.trimText(preset.getName(), textRenderer, getContentWidth() - 6), getContentX() + 3, getContentY() + 1, CommonColors.WHITE, false);
            context.text(textRenderer, TextUtils.trimText(preset.getDescription(), textRenderer, getContentWidth()), getContentX() + 3, getContentY() + 9 + 3, CommonColors.LIGHT_GRAY, false);
            context.text(textRenderer, TextUtils.trimText(preset.getFileName(), textRenderer, getContentWidth()), getContentX() + 3, getContentY() + 9 + 9 + 3, ChatFormatting.DARK_GRAY.getColor(), false);

        }

        public PresetLoader.KeybindPreset getPreset() {
            return preset;
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
            PresetsListWidget.this.setSelected(this);
            return true;
        }

        @Override
        public Component getNarration() {
            return Component.literal(preset.getName());
        }

        public void close() {
        }
    }
}
