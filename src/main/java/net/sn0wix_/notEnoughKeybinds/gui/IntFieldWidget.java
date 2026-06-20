package net.sn0wix_.notEnoughKeybinds.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class IntFieldWidget extends EditBox {
    //TODO fix ctrl + v text bug
    public IntFieldWidget(Font textRenderer, int width, int height, Component text) {
        super(textRenderer, width, height, text);
    }

    public IntFieldWidget(Font textRenderer, int x, int y, int width, int height, Component text) {
        super(textRenderer, x, y, width, height, text);
    }

    public IntFieldWidget(Font textRenderer, int x, int y, int width, int height, @Nullable EditBox copyFrom, Component text) {
        super(textRenderer, x, y, width, height, copyFrom, text);
    }

    @Override
    public boolean charTyped(CharacterEvent input) {
        try {
            Integer.parseInt(input.codepointAsString());
        } catch (NumberFormatException e) {
            return false;
        }

        return super.charTyped(input);
    }

    @Override
    public void setValue(String text) {
        super.setValue(parseString(text));
    }

    public String parseString(String s) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            try {
                Integer.parseInt(String.valueOf(builder.append(s.charAt(i))));
            } catch (NumberFormatException ignored) {
            }
        }

        return builder.toString();
    }
}
