package net.sn0wix_.notEnoughKeybinds.gui.screen.keySettings;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.gui.SettingsScreen;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

import java.awt.*;

public class SwapTotemShieldSettings extends SettingsScreen {
    //what to swap first if holding none
    //meding slider
    //swap second
    //choose best shield/first found one

    public ButtonWidget swapFirstButton;
    public ButtonWidget shieldAlgorithmButton;
    public ButtonWidget swapSecondButton;
    public TextFieldWidget mendingValue;

    public SwapTotemShieldSettings(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, Text.translatable(TextUtils.getSettingsTranslation("swap_totem_shield")));
    }

    @Override
    public void init(int x, int x2, int y, TextRenderer textRenderer) {
        addDoneButton();

        swapFirstButton = ButtonWidget.builder(Text.empty(), button -> {

        }).dimensions(x, y, 150, 20).build();
        addDrawableChild(swapFirstButton);

        shieldAlgorithmButton = ButtonWidget.builder(Text.empty(), button -> {})
                .dimensions(x2, y, 150, 20).build();
        addDrawableChild(shieldAlgorithmButton);

        y += 30;

        swapSecondButton = ButtonWidget.builder(Text.empty(), button -> {

        }).dimensions(x, y, 150, 20).build();
        addDrawableChild(swapSecondButton);

        mendingValue = new TextFieldWidget(textRenderer, x2, y, 150, 20, Text.literal("SDFSDF"));
        addDrawableChild(mendingValue);
    }
}
