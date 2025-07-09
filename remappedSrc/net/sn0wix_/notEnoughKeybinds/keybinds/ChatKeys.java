package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
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
    public static final String CHAT_KEYS_CATEGORY_STRING = "key.category." + NotEnoughKeybinds.MOD_ID + ".chat_keys";
    public static final ChatKeysCategory CHAT_KEYS_CATEGORY = new ChatKeysCategory(CHAT_KEYS_CATEGORY_STRING, 1);


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
            boolean bl = chatKeys.removeIf((binding) -> binding.getTranslationKey().equals(keyBinding.getTranslationKey()));
            chatKeys.add(keyBinding);
            NotEnoughKeybinds.CHAT_KEYS_CONFIG.addKeyIf(keyBinding);
            update();

            return bl;
        }

        public void update() {
            NotEKKeyBindings.updateCategory(this);
            KeyBinding.updateKeysByCode();
        }

        public void initializeKeys() {
            chatKeys.addAll(NotEnoughKeybinds.CHAT_KEYS_CONFIG.loadKeys());
            KeyBinding.updateKeysByCode();
        }
    }
}
