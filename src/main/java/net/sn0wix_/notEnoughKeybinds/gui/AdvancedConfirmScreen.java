package net.sn0wix_.notEnoughKeybinds.gui;

import com.mojang.blaze3d.platform.InputConstants;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

public class AdvancedConfirmScreen extends ConfirmScreen {
    public AdvancedConfirmScreen(BooleanConsumer callback, Component title, Component message) {
        super(callback, title, message);
    }

    public AdvancedConfirmScreen(BooleanConsumer callback, Component title, Component message, Component yesText, Component noText) {
        super(callback, title, message, yesText, noText);
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        if (getFocused() == null && input.input() == InputConstants.KEY_RETURN) {
            this.callback.accept(true);
            return true;
        }

        if (input.input() == InputConstants.KEY_BACKSPACE) {
            this.callback.accept(false);
            return true;
        } else if (input.input() == InputConstants.KEY_DELETE) {
            this.callback.accept(true);
            return true;
        }

        return super.keyPressed(input);
    }
}
