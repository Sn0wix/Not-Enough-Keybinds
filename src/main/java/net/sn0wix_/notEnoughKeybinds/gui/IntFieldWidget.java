package net.sn0wix_.notEnoughKeybinds.gui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.CharInput;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class IntFieldWidget extends TextFieldWidget {
    //TODO fix ctrl + v text bug
    public IntFieldWidget(TextRenderer textRenderer, int width, int height, Text text) {
        super(textRenderer, width, height, text);
    }

    public IntFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
        super(textRenderer, x, y, width, height, text);
    }

    public IntFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, @Nullable TextFieldWidget copyFrom, Text text) {
        super(textRenderer, x, y, width, height, copyFrom, text);
    }

    @Override
    public boolean charTyped(CharInput input) {
        try {
            Integer.parseInt(input.asString());
        } catch (NumberFormatException e) {
            return false;
        }

        return super.charTyped(input);
    }

    @Override
    public void setText(String text) {
        super.setText(parseString(text));
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
