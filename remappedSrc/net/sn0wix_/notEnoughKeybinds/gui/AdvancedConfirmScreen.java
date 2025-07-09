package net.sn0wix_.notEnoughKeybinds.gui;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class AdvancedConfirmScreen extends ConfirmScreen {
    public AdvancedConfirmScreen(BooleanConsumer callback, Text title, Text message) {
        super(callback, title, message);
    }

    public AdvancedConfirmScreen(BooleanConsumer callback, Text title, Text message, Text yesText, Text noText) {
        super(callback, title, message, yesText, noText);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (getFocused() == null && keyCode == GLFW.GLFW_KEY_ENTER) {
            this.callback.accept(true);
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            this.callback.accept(false);
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_DELETE) {
            this.callback.accept(true);
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
