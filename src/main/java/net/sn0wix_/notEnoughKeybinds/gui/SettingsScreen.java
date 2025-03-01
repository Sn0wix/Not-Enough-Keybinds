package net.sn0wix_.notEnoughKeybinds.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.text.Text;

public abstract class SettingsScreen extends GameOptionsScreen {

    public SettingsScreen(Screen parent, Text title) {
        super(parent, MinecraftClient.getInstance().options, title);
    }

    public void saveOptions() {
    }

    @Override
    public void close() {
        saveOptions();
        super.close();
    }

    //UPDATE
    @Override
    protected void addOptions() {

    }
}
