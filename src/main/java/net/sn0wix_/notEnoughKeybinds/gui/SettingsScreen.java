package net.sn0wix_.notEnoughKeybinds.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public abstract class SettingsScreen extends GameOptionsScreen {
    public ButtonWidget doneButton;

    public SettingsScreen(Screen parent, Text title) {
        super(parent, MinecraftClient.getInstance().options, title);
    }

    public void addDoneButtonFooter() {
        addDoneButtonFooter(0, 0, 310, 20);
    }

    public void addDoneButtonFooter(int xModifier, int yModifier, int width, int height) {
        addDoneButtonFooter(button -> close(), xModifier, yModifier, width, height);
    }

    public void addDoneButtonFooter(ButtonWidget.PressAction action, int xModifier, int yModifier, int width, int height) {
        doneButton = ButtonWidget.builder(ScreenTexts.DONE, action)
                .dimensions(this.width / 2 - 155 + xModifier, this.height - 29 + yModifier, width, height)
                .build();
        this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8)).add(doneButton);
    }

    public void saveOptions() {}

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
