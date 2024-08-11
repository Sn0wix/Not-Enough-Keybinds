package net.sn0wix_.notEnoughKeybinds.gui.screen;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.gui.SettingsScreen;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.ChatKeyBinding;

public class ChatKeyScreen extends SettingsScreen {
    public static final Text KEYBIND_NAME_TEXT = Text.translatable("text." + NotEnoughKeybinds.MOD_ID + ".keybind_name").formatted(Formatting.GRAY);
    public static final Text MESSAGE_TEXT_FORMATTED = Text.translatable("text." + NotEnoughKeybinds.MOD_ID + ".message").formatted(Formatting.GRAY);
    public static final Text MESSAGE_TEXT = Text.translatable("text." + NotEnoughKeybinds.MOD_ID + ".message");
    public static final Text COMMAND_TEXT = Text.translatable("text." + NotEnoughKeybinds.MOD_ID + ".command");
    public static final Text TYPE_TEXT = Text.translatable("text." + NotEnoughKeybinds.MOD_ID + ".type");


    public final ChatKeyBinding binding;
    public TextFieldWidget nameWidget;
    public TextFieldWidget messageWidget;
    public ButtonWidget commandMessageWidget;


    public ChatKeyScreen(Screen parent, GameOptions gameOptions, ChatKeyBinding binding) {
        super(parent, gameOptions, Text.translatable("settings." + NotEnoughKeybinds.MOD_ID + ".chat_keys"));

        this.binding = binding;
    }

    @Override
    public void init() {
        assert client != null;

        TextRenderer textRenderer = client.textRenderer;
        int x = this.width / 2 - 155;
        int x2 = width + 160;
        int y = this.height / 6;

        this.addDrawableChild(
                ButtonWidget.builder(ScreenTexts.DONE, button -> {
                            this.client.setScreen(this.parent);
                        })
                        .dimensions(this.width / 2 - 155, this.height - 29, 310, 20)
                        .build()
        );

        //x and y are pixels, not coordinates when used with TextWidget
        addDrawableChild(new TextWidget(x, y, KEYBIND_NAME_TEXT, textRenderer));

        this.nameWidget = new TextFieldWidget(textRenderer, x, y, 150, 20, KEYBIND_NAME_TEXT);
        addDrawableChild(nameWidget);


        this.commandMessageWidget = ButtonWidget.builder(
                TYPE_TEXT,
                button -> {
                }).dimensions(x2, y, 200, 20).build();
        addDrawableChild(commandMessageWidget);


        y += 30;
        addDrawableChild(new TextWidget(x, y, MESSAGE_TEXT_FORMATTED, textRenderer));
        this.messageWidget = new TextFieldWidget(textRenderer, x, y, 300, 20, MESSAGE_TEXT_FORMATTED);
        this.messageWidget.setMaxLength(Integer.MAX_VALUE);
        setInitialFocus(messageWidget);
        addDrawableChild(messageWidget);
    }
}
