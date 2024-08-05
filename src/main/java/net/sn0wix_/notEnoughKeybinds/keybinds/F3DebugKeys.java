package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindingCategory;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.ModKeyBinding;
import org.lwjgl.glfw.GLFW;


//TODO fix: keys binding to normal keys
//TODO fix: gamemode screen fixer
//TODO fix: help message
//TODO fix: F3 + holding c not binding
//TODO fix: add multiple keys support - test

public class F3DebugKeys extends ModKeybindings {
    public static final String F3_DEBUG_KEYS_CATEGORY_STRING = "key.category." + NotEnoughKeybinds.MOD_ID + ".f3_debug_keys";

    public static final ModKeyBinding RELOAD_CHUNKS = new F3DebugKeybinding("reload_chunks", GLFW.GLFW_KEY_A, F3_DEBUG_KEYS_CATEGORY_STRING);
    public static final ModKeyBinding SHOW_HITBOXES = new F3DebugKeybinding("show_hitboxes", GLFW.GLFW_KEY_B, F3_DEBUG_KEYS_CATEGORY_STRING);
    public static final ModKeyBinding COPY_LOCATION = new F3DebugKeybinding("copy_location", GLFW.GLFW_KEY_C, F3_DEBUG_KEYS_CATEGORY_STRING);
    public static final ModKeyBinding CLEAR_CHAT = new F3DebugKeybinding("clear_chat", GLFW.GLFW_KEY_D, F3_DEBUG_KEYS_CATEGORY_STRING);
    public static final ModKeyBinding CHUNK_BOUNDARIES = new F3DebugKeybinding("chunk_boundaries", GLFW.GLFW_KEY_G, F3_DEBUG_KEYS_CATEGORY_STRING);
    public static final ModKeyBinding ADVANCED_TOOLTIPS = new F3DebugKeybinding("advanced_tooltips", GLFW.GLFW_KEY_H, F3_DEBUG_KEYS_CATEGORY_STRING);
    public static final ModKeyBinding INSPECT = new F3DebugKeybinding("inspect", GLFW.GLFW_KEY_I, F3_DEBUG_KEYS_CATEGORY_STRING);
    public static final ModKeyBinding PROFILING = new F3DebugKeybinding("profiling", GLFW.GLFW_KEY_L, F3_DEBUG_KEYS_CATEGORY_STRING);
    public static final ModKeyBinding CREATIVE_SPECTATOR = new F3DebugKeybinding("creative_spectator", GLFW.GLFW_KEY_N, F3_DEBUG_KEYS_CATEGORY_STRING);
    public static final ModKeyBinding PAUSE_FOCUS = new F3DebugKeybinding("pause_focus", GLFW.GLFW_KEY_P, F3_DEBUG_KEYS_CATEGORY_STRING);
    public static final ModKeyBinding HELP = new F3DebugKeybinding("help", GLFW.GLFW_KEY_Q, F3_DEBUG_KEYS_CATEGORY_STRING);
    public static final ModKeyBinding DUMP_DYNAMIC_TEXTURES = new F3DebugKeybinding("dump_dynamic_textures", GLFW.GLFW_KEY_S, F3_DEBUG_KEYS_CATEGORY_STRING);
    public static final ModKeyBinding RELOAD_RESOURCEPACKS = new F3DebugKeybinding("reload_resourcepacks", GLFW.GLFW_KEY_T, F3_DEBUG_KEYS_CATEGORY_STRING);
    public static final ModKeyBinding GAMEMODES = new F3DebugKeybinding("gamemodes", GLFW.GLFW_KEY_F4, F3_DEBUG_KEYS_CATEGORY_STRING);


    public static final KeybindingCategory F3_DEBUG_KEYS_CATEGORY = new KeybindingCategory(F3_DEBUG_KEYS_CATEGORY_STRING, 3, RELOAD_CHUNKS, SHOW_HITBOXES, COPY_LOCATION, CLEAR_CHAT, CHUNK_BOUNDARIES, ADVANCED_TOOLTIPS,
            INSPECT, PROFILING, CREATIVE_SPECTATOR, PAUSE_FOCUS, HELP, DUMP_DYNAMIC_TEXTURES, RELOAD_RESOURCEPACKS, GAMEMODES);

    @Override
    public KeybindingCategory getCategory() {
        return F3_DEBUG_KEYS_CATEGORY;
    }

    public static class F3DebugKeybinding extends ModKeyBinding {
        public F3DebugKeybinding(String translationKey, int code, String category) {
            super(translationKey, code, category, null);
        }

        @Override
        public Text getBoundKeyLocalizedText() {
            return Text.literal("F3 + " + super.getBoundKeyLocalizedText().getString());
        }
    }
}
