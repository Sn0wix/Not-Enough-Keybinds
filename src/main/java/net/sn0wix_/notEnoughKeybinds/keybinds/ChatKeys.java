package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.gui.screen.ChatKeyScreen;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.ChatKeyBinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.INotEKKeybinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ChatKeys extends NotEKKeyBindings {
    public static final String CHAT_KEYS_CATEGORY_STRING = "key.category." + NotEnoughKeybinds.MOD_ID + ".chat_keys";

    @Override
    public KeybindCategory getCategory() {
        return new ChatKeysCategory(CHAT_KEYS_CATEGORY_STRING, 10);
    }

    public static class ChatKeysCategory extends KeybindCategory {
        public ChatKeysCategory(String translationKey, int priority, INotEKKeybinding... keyBindings) {
            super(translationKey, priority, keyBindings);
        }

        @Override
        public String getAddNewButtonTranslation() {
            return "text." + NotEnoughKeybinds.MOD_ID + ".add_new_keybind";
        }

        @Override
        public @Nullable Screen getAddNewButtonScreen(Screen parent) {
            return new ChatKeyScreen(parent, MinecraftClient.getInstance().options, new ChatKeyBinding(
                    KEY_BINDING_PREFIX + "chat." + NotEnoughKeybinds.CHAT_KEYS_CONFIG.getNewKeyIndex(),
                    "Chat Key " + NotEnoughKeybinds.CHAT_KEYS_CONFIG.getNewKeyIndex(), new Random().nextInt(16) == 0 ? "Hello World!" : ""));
        }
    }
}
