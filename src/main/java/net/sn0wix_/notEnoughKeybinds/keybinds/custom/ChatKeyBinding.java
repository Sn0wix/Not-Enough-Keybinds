package net.sn0wix_.notEnoughKeybinds.keybinds.custom;


import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.gui.screen.keySettings.ChatKeyScreen;
import net.sn0wix_.notEnoughKeybinds.keybinds.ChatKeys;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;
import org.apache.commons.lang3.StringUtils;

public class ChatKeyBinding extends NotEKKeyBinding {
    private String chatMessage;
    private String displayName;

    public ChatKeyBinding(String translationKey, String displayName, String chatMessage) {
        super(translationKey, ChatKeys.CHAT_KEYS_CATEGORY, new ChatKeysTicker());
        this.chatMessage = chatMessage;
        this.displayName = displayName;
    }

    public ChatKeyBinding(String translationKey, String displayName, String chatMessage, InputConstants.Key boundKey) {
        //this keybinding was loaded from the config file with the prefix, so we don't add the prefix for the second time
        super(translationKey, ChatKeys.CHAT_KEYS_CATEGORY, new ChatKeysTicker(), false);
        this.chatMessage = chatMessage;
        this.displayName = displayName;
        setKey(boundKey);
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public void setSettingDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public Component getSettingsDisplayName() {
        return Component.literal(displayName);
    }

    @Override
    public void setAndSaveKeyBinding(InputConstants.Key key) {
        super.setAndSaveKeyBinding(key);
        NotEnoughKeybinds.CHAT_KEYS_CONFIG.updateKey(this.getName(), key);
    }

    @Override
    public Screen getSettingsScreen(Screen parent) {
        return new ChatKeyScreen(parent, this);
    }

    @Override
    public Component getTooltip() {
        return Component.translatable(TextUtils.getTranslationKey(chatMessage.startsWith("/") ? "executes_command" : "sends_message", true), chatMessage);
    }

    public static class ChatKeysTicker implements KeybindingTicker {
        @Override
        public void onWasPressed(Minecraft client, NotEKKeyBinding keyBinding) {
            if (keyBinding instanceof ChatKeyBinding chatKeyBinding) {
                sendMessage(chatKeyBinding.getChatMessage(), client.gui.getChat() != null, client);
            }
        }

        public static void sendMessage(String chatText, boolean addToHistory, Minecraft client) {
            chatText = normalize(chatText);
            if (!chatText.isEmpty()) {
                if (addToHistory) {
                    client.gui.getChat().addRecentChat(chatText);
                }
                assert client.player != null;
                if (chatText.startsWith("/")) {
                    client.player.connection.sendCommand(chatText.substring(1));
                } else {
                    client.player.connection.sendChat(chatText);
                }
            }
        }

        /**
         * {@return the {@code message} normalized by trimming it and then normalizing spaces}
         */
        public static String normalize(String chatText) {
            return StringUtil.trimChatMessage(StringUtils.normalizeSpace(chatText.trim()));
        }
    }
}
