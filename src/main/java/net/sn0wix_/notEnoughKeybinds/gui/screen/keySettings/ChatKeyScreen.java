package net.sn0wix_.notEnoughKeybinds.gui.screen.keySettings;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.sn0wix_.notEnoughKeybinds.gui.ParentScreenBlConsumer;
import net.sn0wix_.notEnoughKeybinds.gui.SettingsScreen;
import net.sn0wix_.notEnoughKeybinds.keybinds.ChatKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.ChatKeyBinding;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;
import net.sn0wix_.notEnoughKeybinds.util.Utils;
import org.lwjgl.glfw.GLFW;

public class ChatKeyScreen extends SettingsScreen {
    public static final Text KEYBIND_NAME_TEXT = Text.translatable(TextUtils.getTranslationKey("keybind_name")).formatted(Formatting.GRAY);
    public static final Text MESSAGE_TEXT_FORMATTED = Text.translatable(TextUtils.getTranslationKey("text")).formatted(Formatting.GRAY);
    public static final Text MESSAGE_TEXT = Text.translatable(TextUtils.getTranslationKey("set_to_message"));
    public static final Text COMMAND_TEXT = Text.translatable(TextUtils.getTranslationKey("set_to_command"));


    public final ChatKeyBinding binding;
    public TextFieldWidget nameWidget;
    public TextFieldWidget messageWidget;
    public ButtonWidget commandMessageWidget;
    public ButtonWidget doneButton;
    public ButtonWidget deleteButton;

    public String name;
    public String message;

    public DirectionalLayoutWidget body = DirectionalLayoutWidget.vertical().spacing(20);

    public ChatKeyScreen(Screen parent, ChatKeyBinding binding) {
        super(parent, Text.translatable(TextUtils.getSettingsTranslationKey("chat_keys")));
        this.binding = binding;
        this.name = binding.getSettingsDisplayName().getString();
        this.message = binding.getChatMessage();
    }

    @Override
    protected void initBody() {
        initButtons();
        threePartsLayout.addBody(body);
    }

    @Override
    public void initFooter() {
        DirectionalLayoutWidget directionalLayoutWidget = this.threePartsLayout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
        directionalLayoutWidget.add(deleteButton);
        directionalLayoutWidget.add(doneButton);
    }

    public void initButtons() {
        DirectionalLayoutWidget top = DirectionalLayoutWidget.vertical().spacing(2);
        DirectionalLayoutWidget bottom = DirectionalLayoutWidget.vertical().spacing(2);

        top.add(new TextWidget(200, 20, KEYBIND_NAME_TEXT, textRenderer).alignLeft());
        bottom.add(new TextWidget(200, 20, MESSAGE_TEXT_FORMATTED, textRenderer).alignLeft());

        doneButton = ButtonWidget.builder(ScreenTexts.DONE, button -> {
            binding.setChatMessage(messageWidget.getText());
            binding.setSettingDisplayName(nameWidget.getText());

            if (!ChatKeys.CHAT_KEYS_CATEGORY.addKeyIf(binding)) {
                Utils.showToastNotification(TextUtils.getText("chat_binding.create", binding.getSettingsDisplayName()));
            }
            assert client != null;
            client.setScreen(parent);
        }).build();

        deleteButton = ButtonWidget.builder(TextUtils.getText("delete"), button -> {
            assert client != null;
            client.setScreen(Utils.getModConfirmScreen(new ParentScreenBlConsumer(this, client1 -> {
                        ChatKeys.CHAT_KEYS_CATEGORY.removeKey(binding);
                        client.setScreen(parent);
                        Utils.showToastNotification(TextUtils.getText("chat_binding.delete", binding.getSettingsDisplayName()));
                    }, false),
                    Text.translatable(TextUtils.getTranslationKey("delete_keybind.confirm"), nameWidget.getText())));
        }).build();

        nameWidget = new TextFieldWidget(textRenderer, 150, 20, KEYBIND_NAME_TEXT);
        nameWidget.setChangedListener(s -> updateChildren());
        nameWidget.setText(name);


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
                }).size(150, 20).build();


        messageWidget = new TextFieldWidget(textRenderer, 310, 20, MESSAGE_TEXT_FORMATTED);
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

        DirectionalLayoutWidget nameLayout = DirectionalLayoutWidget.horizontal().spacing(10);
        nameLayout.add(nameWidget);
        nameLayout.add(commandMessageWidget);

        top.add(nameLayout);
        bottom.add(messageWidget);

        body.add(top);
        body.add(bottom);

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

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (getFocused() == null || getFocused() == nameWidget || getFocused() == messageWidget) {
            if (keyCode == GLFW.GLFW_KEY_DELETE) {
                deleteButton.onPress();
                return true;
            }

            if (keyCode == GLFW.GLFW_KEY_ENTER) {
                doneButton.onPress();
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
