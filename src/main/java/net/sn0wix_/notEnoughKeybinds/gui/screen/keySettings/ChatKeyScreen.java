package net.sn0wix_.notEnoughKeybinds.gui.screen.keySettings;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.sn0wix_.notEnoughKeybinds.gui.ParentScreenBlConsumer;
import net.sn0wix_.notEnoughKeybinds.gui.SettingsScreen;
import net.sn0wix_.notEnoughKeybinds.keybinds.ChatKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.ChatKeyBinding;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;
import net.sn0wix_.notEnoughKeybinds.util.Utils;
import org.lwjgl.glfw.GLFW;

public class ChatKeyScreen extends SettingsScreen {
    public static final Component KEYBIND_NAME_TEXT = Component.translatable(TextUtils.getTranslationKey("keybind_name")).withStyle(ChatFormatting.GRAY);
    public static final Component MESSAGE_TEXT_FORMATTED = Component.translatable(TextUtils.getTranslationKey("text")).withStyle(ChatFormatting.GRAY);
    public static final Component MESSAGE_TEXT = Component.translatable(TextUtils.getTranslationKey("set_to_message"));
    public static final Component COMMAND_TEXT = Component.translatable(TextUtils.getTranslationKey("set_to_command"));


    public final ChatKeyBinding binding;
    public EditBox nameWidget;
    public EditBox messageWidget;
    public Button commandMessageWidget;
    public Button doneButton;
    public Button deleteButton;

    public String name;
    public String message;

    public LinearLayout body = LinearLayout.vertical().spacing(20);

    public ChatKeyScreen(Screen parent, ChatKeyBinding binding) {
        super(parent, Component.translatable(TextUtils.getSettingsTranslationKey("chat_keys")));
        this.binding = binding;
        this.name = binding.getSettingsDisplayName().getString();
        this.message = binding.getChatMessage();
    }

    @Override
    protected void initBody() {
        initButtons();
        threePartsLayout.addToContents(body);
    }

    @Override
    public void initFooter() {
        LinearLayout directionalLayoutWidget = this.threePartsLayout.addToFooter(LinearLayout.horizontal().spacing(8));
        directionalLayoutWidget.addChild(deleteButton);
        directionalLayoutWidget.addChild(doneButton);
    }

    public void initButtons() {
        LinearLayout top = LinearLayout.vertical().spacing(2);
        LinearLayout bottom = LinearLayout.vertical().spacing(2);

        top.addChild(new StringWidget(200, 20, KEYBIND_NAME_TEXT, font));
        bottom.addChild(new StringWidget(200, 20, MESSAGE_TEXT_FORMATTED, font));

        doneButton = Button.builder(CommonComponents.GUI_DONE, button -> {
            binding.setChatMessage(messageWidget.getValue());
            binding.setSettingDisplayName(nameWidget.getValue());

            if (!ChatKeys.CHAT_KEYS_MOD_CATEGORY.addKeyIf(binding)) {
                Utils.showToastNotification(TextUtils.getText("chat_binding.create", binding.getSettingsDisplayName()));
            }
            assert minecraft != null;
            minecraft.setScreen(parent);
        }).build();

        deleteButton = Button.builder(TextUtils.getText("delete"), button -> {
            assert minecraft != null;
            minecraft.setScreen(Utils.getModConfirmScreen(new ParentScreenBlConsumer(this, client1 -> {
                        ChatKeys.CHAT_KEYS_MOD_CATEGORY.removeKey(binding);
                        minecraft.setScreen(parent);
                        Utils.showToastNotification(TextUtils.getText("chat_binding.delete", binding.getSettingsDisplayName()));
                    }, false),
                    Component.translatable(TextUtils.getTranslationKey("delete_keybind.confirm"), nameWidget.getValue())));
        }).build();

        nameWidget = new EditBox(font, 150, 20, KEYBIND_NAME_TEXT);
        nameWidget.setResponder(s -> updateChildren());
        nameWidget.setValue(name);


        commandMessageWidget = Button.builder(
                COMMAND_TEXT,
                button -> {
                    if (messageWidget.getValue().startsWith("/")) {
                        messageWidget.setValue(messageWidget.getValue().replaceFirst("/", ""));
                        button.setMessage(COMMAND_TEXT);
                    } else {
                        messageWidget.setValue("/" + messageWidget.getValue());
                        button.setMessage(MESSAGE_TEXT);
                    }

                    setFocused(true);
                    setFocused(messageWidget);
                }).size(150, 20).build();


        messageWidget = new EditBox(font, 310, 20, MESSAGE_TEXT_FORMATTED);
        messageWidget.setMaxLength(Integer.MAX_VALUE);
        messageWidget.setResponder(s -> {
            if (s.startsWith("/")) {
                commandMessageWidget.setMessage(MESSAGE_TEXT);
            } else {
                commandMessageWidget.setMessage(COMMAND_TEXT);
            }

            updateChildren();
        });
        messageWidget.setValue(message);

        LinearLayout nameLayout = LinearLayout.horizontal().spacing(10);
        nameLayout.addChild(nameWidget);
        nameLayout.addChild(commandMessageWidget);

        top.addChild(nameLayout);
        bottom.addChild(messageWidget);

        body.addChild(top);
        body.addChild(bottom);

        setInitialFocus(messageWidget);
    }

    public void updateChildren() {
        try {
            message = messageWidget.getValue();
            name = nameWidget.getValue();
            doneButton.active = !messageWidget.getValue().isEmpty() && !nameWidget.getValue().isEmpty();
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        if (getFocused() == null || getFocused() == nameWidget || getFocused() == messageWidget) {
            if (input.input() == GLFW.GLFW_KEY_DELETE) {
                deleteButton.onPress(input);
                return true;
            }

            if (input.input() == GLFW.GLFW_KEY_ENTER) {
                doneButton.onPress(input);
                return true;
            }
        }

        return super.keyPressed(input);
    }
}
