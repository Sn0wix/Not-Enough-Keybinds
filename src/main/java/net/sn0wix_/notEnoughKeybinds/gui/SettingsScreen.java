package net.sn0wix_.notEnoughKeybinds.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public abstract class SettingsScreen extends GameOptionsScreen {
    public ButtonWidget doneButton;

    public SettingsScreen(Screen parent, Text title) {
        super(parent, MinecraftClient.getInstance().options, title);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 8, 16777215);
    }

    /*UPDATE @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
    }*/

    @Override
    public void init() {
        assert client != null;
        init(this.width / 2 - 155, (this.width / 2 - 155) + 160, this.height / 6, client.textRenderer);
    }

    public abstract void init(int x, int x2, int y, TextRenderer textRenderer);



    public void addDoneButton() {
        addDoneButton(0, 0, 310, 20);
    }

    public void addDoneButton(int xModifier, int yModifier, int width, int height) {
        addDoneButton(button -> close(), xModifier, yModifier, width, height);
    }

    public void addDoneButton(ButtonWidget.PressAction action, int xModifier, int yModifier, int width, int height) {
        doneButton = ButtonWidget.builder(ScreenTexts.DONE, action)
                .dimensions(this.width / 2 - 155 + xModifier, this.height - 29 + yModifier, width, height)
                .build();
        addDrawableChild(doneButton);
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
