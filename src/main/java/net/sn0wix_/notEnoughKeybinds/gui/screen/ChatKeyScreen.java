package net.sn0wix_.notEnoughKeybinds.gui.screen;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.sn0wix_.notEnoughKeybinds.gui.ParentScreenBlConsumer;
import net.sn0wix_.notEnoughKeybinds.gui.SettingsScreen;
import net.sn0wix_.notEnoughKeybinds.keybinds.ChatKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.ChatKeyBinding;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;
import net.sn0wix_.notEnoughKeybinds.util.Utils;

public class ChatKeyScreen extends SettingsScreen {
    public static final Text KEYBIND_NAME_TEXT = Text.translatable(TextUtils.getTranslationKey("keybind_name")).formatted(Formatting.GRAY);
    public static final Text MESSAGE_TEXT_FORMATTED = Text.translatable(TextUtils.getTranslationKey("text")).formatted(Formatting.GRAY);
    public static final Text MESSAGE_TEXT = Text.translatable(TextUtils.getTranslationKey("set_to_message"));
    public static final Text COMMAND_TEXT = Text.translatable(TextUtils.getTranslationKey("set_to_command"));


    public final ChatKeyBinding binding;
    public TextFieldWidget nameWidget;
    public TextFieldWidget messageWidget;
    public ButtonWidget commandMessageWidget;
    public ButtonWidget trashButton;

    public String name;
    public String message;


    public ChatKeyScreen(Screen parent, ChatKeyBinding binding) {
        super(parent, Text.translatable(TextUtils.getSettingsTranslationKey("chat_keys")));
        this.binding = binding;
        this.name = binding.getSettingsDisplayName().getString();
        this.message = binding.getChatMessage();
    }

    @Override
    public void init(int x, int x2, int y, TextRenderer textRenderer) {
        trashButton = ButtonWidget.builder(TextUtils.getText("delete"), button ->
                {
                    assert client != null;
                    client.setScreen(Utils.getModConfirmScreen(new ParentScreenBlConsumer(this, client1 -> {
                                ChatKeys.CHAT_KEYS_CATEGORY.removeKey(binding);
                                client.setScreen(parent);
                            }, false),
                            Text.translatable(TextUtils.getTranslationKey("delete_keybind.confirm"), nameWidget.getText())));
                }
        ).dimensions(x + 240, this.height - 29, 70, 20).build();
        addDrawableChild(trashButton);

        addDoneButton(button -> {
            binding.setChatMessage(messageWidget.getText());
            binding.setSettingDisplayName(nameWidget.getText());
            ChatKeys.CHAT_KEYS_CATEGORY.addKeyIf(binding);
            assert client != null;
            client.setScreen(parent);
        }, 0, 0, 230, 20);

        nameWidget = new TextFieldWidget(textRenderer, x, y, 150, 20, KEYBIND_NAME_TEXT);
        nameWidget.setChangedListener(s -> updateChildren());
        nameWidget.setText(name);
        addDrawableChild(nameWidget);


        commandMessageWidget = ButtonWidget.builder(
                COMMAND_TEXT,
                button -> {
                    if (messageWidget.getText().startsWith("/")) {
                        messageWidget.setText(messageWidget.getText().replaceFirst("/", ""));
                        button.setMessage(COMMAND_TEXT);
                    } else {
                        messageWidget.setText("/" + messageWidget.getText());
                        button.setMessage(MESSAGE_TEXT);
                    }

                    setFocused(true);
                    setFocused(messageWidget);
                }).dimensions(x2, y, 150, 20).build();
        addDrawableChild(commandMessageWidget);


        addDrawableChild(new TextWidget(x, y - 20, 200, 20, KEYBIND_NAME_TEXT, textRenderer).alignLeft());
        y += 40;
        addDrawableChild(new TextWidget(x, y, 200, 20, MESSAGE_TEXT_FORMATTED, textRenderer).alignLeft());


        messageWidget = new TextFieldWidget(textRenderer, x, y + 20, 310, 20, MESSAGE_TEXT_FORMATTED);
        messageWidget.setMaxLength(Integer.MAX_VALUE);
        messageWidget.setChangedListener(s -> {
            if (s.startsWith("/")) {
                commandMessageWidget.setMessage(MESSAGE_TEXT);
            } else {
                commandMessageWidget.setMessage(COMMAND_TEXT);
            }

            updateChildren();
        });
        messageWidget.setText(message);

        addDrawableChild(messageWidget);
        setInitialFocus(messageWidget);
    }

    public void updateChildren() {
        try {
            message = messageWidget.getText();
            name = nameWidget.getText();
            doneButton.active = !messageWidget.getText().isEmpty() && !nameWidget.getText().isEmpty();
        } catch (NullPointerException ignored) {
        }
    }
}
