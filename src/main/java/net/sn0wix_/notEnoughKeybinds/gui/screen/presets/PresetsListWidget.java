package net.sn0wix_.notEnoughKeybinds.gui.screen.presets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.sn0wix_.notEnoughKeybinds.keybinds.presets.PresetLoader;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;
import org.jetbrains.annotations.Nullable;

public class PresetsListWidget extends AlwaysSelectedEntryListWidget<PresetsListWidget.PresetEntry> {
    public final PresetsSettingScreen parent;

    public PresetsListWidget(PresetsSettingScreen parent, MinecraftClient client, int width, int height, int y, int itemHeight) {
        super(client, width, height, y, itemHeight);
        this.parent = parent;
        initEntries();
    }

    public void initEntries() {
        clearEntries();
        PresetLoader.getPresets().forEach(keybindPreset -> addEntry(new PresetEntry(keybindPreset, client.textRenderer)));
    }

    @Override
    public void setSelected(@Nullable PresetsListWidget.PresetEntry entry) {
        super.setSelected(entry);
        parent.updateScreen();
    }

    @Environment(EnvType.CLIENT)
    public class PresetEntry extends AlwaysSelectedEntryListWidget.Entry<PresetEntry> implements AutoCloseable {
        public PresetLoader.KeybindPreset preset;
        public TextRenderer textRenderer;

        public PresetEntry(PresetLoader.KeybindPreset preset, TextRenderer textRenderer) {
            this.preset = preset;
            this.textRenderer = textRenderer;
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            context.drawText(textRenderer, TextUtils.trimText(preset.getName(), textRenderer, getContentWidth() - 6), getContentX() + 3, getContentY() + 1, Colors.WHITE, false);
            context.drawText(textRenderer, TextUtils.trimText(preset.getDescription(), textRenderer, getContentWidth()), getContentX() + 3, getContentY() + 9 + 3, Colors.LIGHT_GRAY, false);
            context.drawText(textRenderer, TextUtils.trimText(preset.getFileName(), textRenderer, getContentWidth()), getContentX() + 3, getContentY() + 9 + 9 + 3, Formatting.DARK_GRAY.getColorValue(), false);

        }

        public PresetLoader.KeybindPreset getPreset() {
            return preset;
        }

        @Override
        public boolean mouseClicked(Click click, boolean doubled) {
            PresetsListWidget.this.setSelected(this);
            return true;
        }

        @Override
        public Text getNarration() {
            return Text.literal(preset.getName());
        }

        public void close() {
        }
    }
}
