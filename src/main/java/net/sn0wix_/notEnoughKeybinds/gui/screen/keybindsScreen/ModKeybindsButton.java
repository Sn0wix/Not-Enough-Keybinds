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
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.gui.TexturedButtonWidget;
import net.sn0wix_.notEnoughKeybinds.mixin.ControlsListWidgetAccessor;

import java.util.List;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class ModKeybindsButton extends ControlsListWidget.CategoryEntry {
    public final ButtonWidget button;

    public ModKeybindsButton(ControlsListWidget widget) {
        widget.super(new KeyBinding.Category(Identifier.of("")));

        button = new TexturedButtonWidget(0, 0, 340, 20, Text.translatable("settings." + NotEnoughKeybinds.MOD_ID), button1 ->
                MinecraftClient.getInstance().setScreen(new NotEKSettingsScreen(((ControlsListWidgetAccessor) widget).getParent()))
        , Supplier::get, NotEnoughKeybinds.ICON, 18, 18, 18, 18);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        button.setWidth(getContentWidth() + 2);
        button.setPosition(getContentX(), getContentY());
        button.render(context, mouseX, mouseY, deltaTicks);
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
