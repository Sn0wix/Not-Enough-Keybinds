package net.sn0wix_.notEnoughKeybinds.config;

import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.platform.InputConstants;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.resources.Identifier;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.ChatKeyBinding;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatKeysConfig {
    public static ConfigClassHandler<ChatKeysConfig> HANDLER = ConfigClassHandler.createBuilder(ChatKeysConfig.class)
            .id(Identifier.fromNamespaceAndPath(NotEnoughKeybinds.MOD_ID, "chat_keys"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve(NotEnoughKeybinds.MOD_ID).resolve("chat_keys.json"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(false)
                    .build())
            .build();


    @SerialEntry()
    public HashMap<String, ChatKeyValues> chatKeys = new HashMap<>();


    public void addKey(ChatKeyBinding binding) {
        chatKeys.put(binding.getName(), new ChatKeyValues(binding.getChatMessage(), binding.getSettingsDisplayName().getString(), binding.getDefaultKey()));
        saveConfig();
    }

    public void removeKey(ChatKeyBinding binding) {
        chatKeys.remove(binding.getName());
        saveConfig();
    }

    public void addKeyIf(ChatKeyBinding binding) {
        chatKeys.remove(binding.getName());
        chatKeys.put(binding.getName(), new ChatKeyValues(binding.getChatMessage(), binding.getSettingsDisplayName().getString(), InputConstants.getKey(binding.getBoundKeyTranslation())));
        saveConfig();
    }

    public void updateKey(String translationKey, InputConstants.Key key) {
        chatKeys.forEach((s, chatKeyValues) -> {
            if (s.equals(translationKey)) {
                chatKeyValues.setKey(key.getValue());
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

        public ChatKeyValues(String message, String name, InputConstants.Key key) {
            this.message = message;
            this.name = name;
            this.keyCode = key.getValue();
        }

        public InputConstants.Key getKey() {
            return keyCode == -1 ? InputConstants.UNKNOWN : InputConstants.getKey(new KeyEvent(keyCode, GLFW.glfwGetKeyScancode(keyCode), 0));
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
