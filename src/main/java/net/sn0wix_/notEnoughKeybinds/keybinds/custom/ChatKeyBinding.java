package net.sn0wix_.notEnoughKeybinds.keybinds.custom;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.gui.screen.ChatKeyScreen;
import net.sn0wix_.notEnoughKeybinds.keybinds.ChatKeys;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;
import org.apache.commons.lang3.StringUtils;

public class ChatKeyBinding extends NotEKKeyBinding {
    private String chatMessage;
    private String displayName;

    public ChatKeyBinding(String translationKey, String displayName, String chatMessage) {
        super(translationKey, ChatKeys.CHAT_KEYS_CATEGORY_STRING, new ChatKeysTicker());
        this.chatMessage = chatMessage;
        this.displayName = displayName;
    }

    public ChatKeyBinding(String translationKey, String displayName, String chatMessage, InputUtil.Key key) {
        super(translationKey, ChatKeys.CHAT_KEYS_CATEGORY_STRING, new ChatKeysTicker());
        this.chatMessage = chatMessage;
        this.displayName = displayName;
        setBoundKey(key);
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
    public Text getSettingsDisplayName() {
        return Text.literal(displayName);
    }

    @Override
    public void setAndSaveKeyBinding(InputUtil.Key key) {
        super.setAndSaveKeyBinding(key);
        NotEnoughKeybinds.CHAT_KEYS_CONFIG.updateKey(getTranslationKey(), key);
    }

    @Override
    public Screen getSettingsScreen(Screen parent) {
        return new ChatKeyScreen(parent, this);
    }

    @Override
    public Text getTooltip() {
        return Text.translatable(TextUtils.getTranslationKey(chatMessage.startsWith("/") ? "executes_command" : "sends_message", true), chatMessage);
    }

    public static class ChatKeysTicker implements KeybindingTicker {
        @Override
        public void onWasPressed(MinecraftClient client, NotEKKeyBinding keyBinding) {
            if (keyBinding instanceof ChatKeyBinding chatKeyBinding) {
                sendMessage(chatKeyBinding.getChatMessage(), client.inGameHud.getChatHud() != null, client);
            }
        }

        public static void sendMessage(String chatText, boolean addToHistory, MinecraftClient client) {
            chatText = normalize(chatText);
            if (!chatText.isEmpty()) {
                if (addToHistory) {
                    client.inGameHud.getChatHud().addToMessageHistory(chatText);
                }
                assert client.player != null;
                if (chatText.startsWith("/")) {
                    client.player.networkHandler.sendChatCommand(chatText.substring(1));
                } else {
                    client.player.networkHandler.sendChatMessage(chatText);
                }
            }
        }

        /**
         * {@return the {@code message} normalized by trimming it and then normalizing spaces}
         */
        public static String normalize(String chatText) {
            return StringHelper.truncateChat(StringUtils.normalizeSpace(chatText.trim()));
        }
    }
}
