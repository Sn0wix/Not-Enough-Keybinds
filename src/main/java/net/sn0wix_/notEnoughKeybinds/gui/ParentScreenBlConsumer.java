package net.sn0wix_.notEnoughKeybinds.gui;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Consumer;

public record ParentScreenBlConsumer(Screen parent, Consumer<MinecraftClient> consumer, boolean setParentIf) implements BooleanConsumer {
    @Override
    public void accept(boolean t) {
        if (t) {
            consumer.accept(MinecraftClient.getInstance());

            if (setParentIf) {
                MinecraftClient.getInstance().setScreen(parent);
            }
        } else {
            MinecraftClient.getInstance().setScreen(parent);
        }
    }
}
