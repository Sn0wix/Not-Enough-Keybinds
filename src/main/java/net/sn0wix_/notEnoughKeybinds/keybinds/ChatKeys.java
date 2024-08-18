package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.gui.screen.ChatKeyScreen;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.ChatKeyBinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.INotEKKeybinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class ChatKeys extends NotEKKeyBindings {
    public static final String CHAT_KEYS_CATEGORY_STRING = "key.category." + NotEnoughKeybinds.MOD_ID + ".chat_keys";
    public static final ChatKeysCategory CHAT_KEYS_CATEGORY = new ChatKeysCategory(CHAT_KEYS_CATEGORY_STRING, 10);


    @Override
    public KeybindCategory getCategory() {
        return CHAT_KEYS_CATEGORY;
    }

    public static class ChatKeysCategory extends KeybindCategory {
        private final ArrayList<ChatKeyBinding> chatKeys = new ArrayList<>(NotEnoughKeybinds.CHAT_KEYS_CONFIG.chatKeys.size());

        public ChatKeysCategory(String translationKey, int priority, INotEKKeybinding... keyBindings) {
            super(translationKey, priority, keyBindings);
        }

        @Override
        public INotEKKeybinding[] getKeyBindings() {
            return chatKeys.toArray(new ChatKeyBinding[0]);
        }

        @Override
        public String getAddNewButtonTranslation() {
            return "text." + NotEnoughKeybinds.MOD_ID + ".add_new_keybind";
        }

        @Override
        public @Nullable Screen getAddNewButtonScreen(Screen parent) {
            return new ChatKeyScreen(parent, MinecraftClient.getInstance().options, new ChatKeyBinding(
                    "chat." + getNewKeyIndex(),
                    "Chat Key " + getNewKeyIndex(), new Random().nextInt(16) == 0 ? "Hello World!" : ""));
        }

        public int getNewKeyIndex() {
            return chatKeys.size();
        }


        public void addKey(ChatKeyBinding keyBinding) {
            chatKeys.add(keyBinding);
            NotEnoughKeybinds.CHAT_KEYS_CONFIG.addKey(keyBinding);
            update();
        }

        public void removeKey(ChatKeyBinding keyBinding) {
            chatKeys.remove(keyBinding);
            NotEnoughKeybinds.CHAT_KEYS_CONFIG.removeKey(keyBinding);
            update();
        }

        public void addKeyIf(ChatKeyBinding keyBinding) {
            chatKeys.remove(keyBinding);
            chatKeys.add(keyBinding);
            NotEnoughKeybinds.CHAT_KEYS_CONFIG.addKeyIf(keyBinding);
            update();
        }

        public void update() {
            NotEKKeyBindings.updateCategory(this);
        }

        public void initializeKeys() {
            chatKeys.addAll(NotEnoughKeybinds.CHAT_KEYS_CONFIG.loadKeys());
        }
    }
}
