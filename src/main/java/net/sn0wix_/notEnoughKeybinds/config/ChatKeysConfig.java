package net.sn0wix_.notEnoughKeybinds.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.ChatKeyBinding;

import java.util.ArrayList;

public class ChatKeysConfig {
    public static ConfigClassHandler<ChatKeysConfig> HANDLER = ConfigClassHandler.createBuilder(ChatKeysConfig.class)
            .id(new Identifier(NotEnoughKeybinds.MOD_ID, "chat_keys"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve(NotEnoughKeybinds.MOD_ID).resolve("chat_keys.json"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(false)
                    .build())
            .build();

    @SerialEntry()
    public ArrayList<ChatKeyBinding> chatKeys = new ArrayList<>();

    public ChatKeyBinding[] getKeysArray() {
        return chatKeys.toArray(new ChatKeyBinding[0]);
    }

    public int getNewKeyIndex() {
        return chatKeys.size();
    }

    public static ChatKeysConfig getConfig() {
        HANDLER.load();
        return HANDLER.instance();
    }

    public static void saveConfig() {
        HANDLER.save();
    }
}
