package net.sn0wix_.notEnoughKeybinds.gui.screen.keySettings;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.gui.SettingsScreen;

public class SwapTotemShieldSettings extends SettingsScreen {
    //what to swap first if holding none
    //choose best shield/first found one
    public SwapTotemShieldSettings(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, Text.translatable("settings." + NotEnoughKeybinds.MOD_ID + ".swap_totem_shield"));
    }

    @Override
    public void init(int x, int x2, int y, TextRenderer textRenderer) {

    }
}
