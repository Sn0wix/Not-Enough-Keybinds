package net.sn0wix_.notEnoughKeybinds.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3DebugKeys;

import java.util.HashMap;

public class DebugKeysConfig {
    public static ConfigClassHandler<DebugKeysConfig> HANDLER = ConfigClassHandler.createBuilder(DebugKeysConfig.class)
            .id(Identifier.of(NotEnoughKeybinds.MOD_ID, "debug_keys"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve(NotEnoughKeybinds.MOD_ID).resolve("debug_keys.json"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(false)
                    .build())
            .build();

    @SerialEntry()
    public HashMap<String, String> debugKeybindings = F3DebugKeys.getMap();

    public static DebugKeysConfig getConfig() {
        HANDLER.load();
        DebugKeysConfig config = HANDLER.instance();

        for (int i = 0; i < F3DebugKeys.F3_DEBUG_KEYS_CATEGORY.getKeyBindings().length; i++) {
            F3DebugKeys.F3_DEBUG_KEYS_CATEGORY.getKeyBindings()[i].setBoundKey(
                    config.getKey(F3DebugKeys.F3_DEBUG_KEYS_CATEGORY.getKeyBindings()[i].getTranslationKey()));
        }

        return config;
    }

    public static void saveConfig() {
        HANDLER.save();
    }

    public void saveBoundKey(String keybindingTranslation, String newKey) {
        debugKeybindings.replace(keybindingTranslation, newKey);
        saveConfig();
    }

    public InputUtil.Key getKey(String keybinding) {
        return InputUtil.fromTranslationKey(debugKeybindings.get(keybinding));
    }
}
