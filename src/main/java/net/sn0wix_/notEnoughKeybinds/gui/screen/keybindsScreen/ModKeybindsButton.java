package net.sn0wix_.notEnoughKeybinds.gui.screen.keybindsScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.mixin.ControlsListWidgetAccessor;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ModKeybindsButton extends ControlsListWidget.CategoryEntry {
    public final ButtonWidget button;

    public ModKeybindsButton(ControlsListWidget widget) {
        widget.super(Text.empty());

        button = ButtonWidget.builder(Text.translatable("settings." + NotEnoughKeybinds.MOD_ID), button1 ->
                MinecraftClient.getInstance().setScreen(new NotEKSettingsScreen(((ControlsListWidgetAccessor) widget).getParent()))
        ).size(340, 20).build();
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        button.setPosition(x, y);
        button.render(context, mouseX, mouseY, tickDelta);

        context.drawTexture(NotEnoughKeybinds.ICON, x + 340 / 2 - MinecraftClient.getInstance().textRenderer.getWidth(button.getMessage()) / 2 - 20, y, 0, 0, 0, 18, 18, 18, 18);
    }

    @Override
    public List<? extends Element> children() {
        return List.of(button);
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return List.of(new Selectable() {
            @Override
            public SelectionType getType() {
                return SelectionType.HOVERED;
            }

            @Override
            public void appendNarrations(NarrationMessageBuilder builder) {
                builder.put(NarrationPart.TITLE, Text.of("not enough keybinds settings"));
            }
        });
    }
}
