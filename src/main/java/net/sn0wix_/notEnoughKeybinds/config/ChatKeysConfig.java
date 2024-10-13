package net.sn0wix_.notEnoughKeybinds.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.ChatKeyBinding;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatKeysConfig {
    public static ConfigClassHandler<ChatKeysConfig> HANDLER = ConfigClassHandler.createBuilder(ChatKeysConfig.class)
            .id(Identifier.of(NotEnoughKeybinds.MOD_ID, "chat_keys"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve(NotEnoughKeybinds.MOD_ID).resolve("chat_keys.json"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(false)
                    .build())
            .build();


    @SerialEntry()
    public HashMap<String, ChatKeyValues> chatKeys = new HashMap<>();


    public void addKey(ChatKeyBinding binding) {
        chatKeys.put(binding.getTranslationKey(), new ChatKeyValues(binding.getChatMessage(), binding.getSettingsDisplayName().getString(), binding.getDefaultKey()));
        saveConfig();
    }

    public void removeKey(ChatKeyBinding binding) {
        chatKeys.remove(binding.getTranslationKey());
        saveConfig();
    }

    public void addKeyIf(ChatKeyBinding binding) {
        chatKeys.remove(binding.getTranslationKey());
        chatKeys.put(binding.getTranslationKey(), new ChatKeyValues(binding.getChatMessage(), binding.getSettingsDisplayName().getString(), InputUtil.fromTranslationKey(binding.getBoundKeyTranslationKey())));
        saveConfig();
    }

    public void updateKey(String translationKey, InputUtil.Key key) {
        chatKeys.forEach((s, chatKeyValues) -> {
            if (s.equals(translationKey)) {
                chatKeyValues.setKey(key.getCode());
            }
        });
        saveConfig();
    }

    public ArrayList<ChatKeyBinding> loadKeys() {
        ArrayList<ChatKeyBinding> bindings = new ArrayList<>();
        chatKeys.forEach((translation, chatKeyValues) -> bindings.add(new ChatKeyBinding(translation, chatKeyValues.getName(), chatKeyValues.getMessage(), chatKeyValues.getKey())));
        return bindings;
    }


    public static ChatKeysConfig getConfig() {
        HANDLER.load();
        return HANDLER.instance();
    }

    public static void saveConfig() {
        HANDLER.save();
    }


    public static class ChatKeyValues {
        private String message;
        private String name;
        private int keyCode;

        public ChatKeyValues(String message, String name, InputUtil.Key key) {
            this.message = message;
            this.name = name;
            this.keyCode = key.getCode();
        }

        public InputUtil.Key getKey() {
            return keyCode == -1 ? InputUtil.UNKNOWN_KEY : InputUtil.fromKeyCode(keyCode, GLFW.glfwGetKeyScancode(keyCode));
        }

        public String getMessage() {
            return message;
        }

        public String getName() {
            return name;
        }

        public void setKey(int key) {
            this.keyCode = key;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
