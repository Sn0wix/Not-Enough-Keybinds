package net.sn0wix_.notEnoughKeybinds.gui;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public record ParentScreenBlConsumer(Screen parent, Consumer<Minecraft> consumer, boolean setParentIf) implements BooleanConsumer {
    @Override
    public void accept(boolean t) {
        if (t) {
            consumer.accept(Minecraft.getInstance());

            if (setParentIf) {
                Minecraft.getInstance().setScreen(parent);
            }
        } else {
            Minecraft.getInstance().setScreen(parent);
        }
    }
}
