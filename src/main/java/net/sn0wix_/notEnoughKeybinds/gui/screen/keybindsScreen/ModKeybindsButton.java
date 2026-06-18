package net.sn0wix_.notEnoughKeybinds.gui.screen.keybindsScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.mixin.ControlsListWidgetAccessor;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ModKeybindsButton extends KeyBindsList.CategoryEntry {
    public final Button button;

    public ModKeybindsButton(KeyBindsList widget) {
        widget.super(new KeyMapping.Category(Identifier.parse("")));

        button = Button.builder(Component.translatable("settings." + NotEnoughKeybinds.MOD_ID), button1 ->
                Minecraft.getInstance().setScreen(new NotEKSettingsScreen(((ControlsListWidgetAccessor) widget).getParent()))
        ).size(340, 20).build();
    }

    @Override
    public void extractContent(GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        button.setWidth(getContentWidth() + 2);
        button.setPosition(getContentX(), getContentY());
        button.extractRenderState(context, mouseX, mouseY, deltaTicks);

        context.blit(RenderPipelines.GUI_TEXTURED, NotEnoughKeybinds.ICON, getContentX() + 340 / 2 - Minecraft.getInstance().font.width(button.getMessage()) / 2 - 20, getContentY(), 0, 0, 0, 18, 18, 18, 18);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return List.of(button);
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return List.of(new NarratableEntry() {
            @Override
            public NarrationPriority narrationPriority() {
                return NarrationPriority.HOVERED;
            }

            @Override
            public void updateNarration(NarrationElementOutput builder) {
                builder.add(NarratedElementType.TITLE, Component.nullToEmpty("not enough keybinds settings"));
            }
        });
    }
}
