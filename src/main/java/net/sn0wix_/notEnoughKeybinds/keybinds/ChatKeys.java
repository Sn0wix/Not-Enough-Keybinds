package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.gui.screen.keySettings.ChatKeyScreen;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.ChatKeyBinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.INotEKKeybinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class ChatKeys extends NotEKKeyBindings {
    public static final String CHAT_KEYS_CATEGORY_KEY = "key.category." + NotEnoughKeybinds.MOD_ID + ".chat_keys";
    public static final ChatKeysCategory CHAT_KEYS_MOD_CATEGORY = new ChatKeysCategory(CHAT_KEYS_CATEGORY_KEY, 1);
    public static final KeyMapping.Category CHAT_KEYS_CATEGORY = KeyMapping.Category.register(NotEnoughKeybinds.getIdentifier(CHAT_KEYS_CATEGORY_KEY));



    @Override
    public KeybindCategory getModCategory() {
        return CHAT_KEYS_MOD_CATEGORY;
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
            return TextUtils.getTranslationKey("add_new_keybind");
        }

        @Override
        public @Nullable Screen getAddNewButtonScreen(Screen parent) {
            return new ChatKeyScreen(parent, new ChatKeyBinding(
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

        public boolean addKeyIf(ChatKeyBinding keyBinding) {
            boolean bl = chatKeys.removeIf((binding) -> binding.getName().equals(keyBinding.getName()));
            chatKeys.add(keyBinding);
            NotEnoughKeybinds.CHAT_KEYS_CONFIG.addKeyIf(keyBinding);
            update();

            return bl;
        }

        public void update() {
            NotEKKeyBindings.updateCategory(this);
            KeyMapping.resetMapping();
        }

        public void initializeKeys() {
            chatKeys.addAll(NotEnoughKeybinds.CHAT_KEYS_CONFIG.loadKeys());
            KeyMapping.resetMapping();
        }
    }
}
