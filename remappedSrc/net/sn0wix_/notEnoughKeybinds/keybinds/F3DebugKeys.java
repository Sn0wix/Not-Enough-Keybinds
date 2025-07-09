package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.F3DebugKeybinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;


//TODO fix profiling message
public class F3DebugKeys extends NotEKKeyBindings {
    public static final String F3_DEBUG_KEYS_CATEGORY_STRING = "key.category." + NotEnoughKeybinds.MOD_ID + ".f3_debug_keys";

    public static final F3DebugKeybinding RELOAD_CHUNKS = new F3DebugKeybinding("reload_chunks", GLFW.GLFW_KEY_A);
    public static final F3DebugKeybinding SHOW_HITBOXES = new F3DebugKeybinding("show_hitboxes", GLFW.GLFW_KEY_B);
    public static final F3DebugKeybinding COPY_LOCATION = new F3DebugKeybinding("copy_location", GLFW.GLFW_KEY_C);
    public static final F3DebugKeybinding CLEAR_CHAT = new F3DebugKeybinding("clear_chat", GLFW.GLFW_KEY_D);
    public static final F3DebugKeybinding CHUNK_BOUNDARIES = new F3DebugKeybinding("chunk_boundaries", GLFW.GLFW_KEY_G);
    public static final F3DebugKeybinding ADVANCED_TOOLTIPS = new F3DebugKeybinding("advanced_tooltips", GLFW.GLFW_KEY_H);
    public static final F3DebugKeybinding INSPECT = new F3DebugKeybinding("inspect", GLFW.GLFW_KEY_I);
    public static final F3DebugKeybinding PROFILING = new F3DebugKeybinding("profiling", GLFW.GLFW_KEY_L);
    public static final F3DebugKeybinding CREATIVE_SPECTATOR = new F3DebugKeybinding("creative_spectator", GLFW.GLFW_KEY_N);
    public static final F3DebugKeybinding PAUSE_FOCUS = new F3DebugKeybinding("pause_focus", GLFW.GLFW_KEY_P);
    public static final F3DebugKeybinding HELP = new F3DebugKeybinding("help", GLFW.GLFW_KEY_Q);
    public static final F3DebugKeybinding DUMP_DYNAMIC_TEXTURES = new F3DebugKeybinding("dump_dynamic_textures", GLFW.GLFW_KEY_S);
    public static final F3DebugKeybinding RELOAD_RESOURCEPACKS = new F3DebugKeybinding("reload_resourcepacks", GLFW.GLFW_KEY_T);
    public static final F3DebugKeybinding GAMEMODES = new F3DebugKeybinding("gamemodes", GLFW.GLFW_KEY_F4);


    public static final KeybindCategory F3_DEBUG_KEYS_CATEGORY = new KeybindCategory(F3_DEBUG_KEYS_CATEGORY_STRING, 69, RELOAD_CHUNKS, SHOW_HITBOXES, COPY_LOCATION, CLEAR_CHAT, CHUNK_BOUNDARIES, ADVANCED_TOOLTIPS,
            INSPECT, PROFILING, CREATIVE_SPECTATOR, PAUSE_FOCUS, HELP, DUMP_DYNAMIC_TEXTURES, RELOAD_RESOURCEPACKS, GAMEMODES);

    @Override
    public KeybindCategory getCategory() {
        return F3_DEBUG_KEYS_CATEGORY;
    }

    public static HashMap<String, String> getMap() {
        HashMap<String, String> map = new HashMap<>(14);
        for (int i = 0; i < F3_DEBUG_KEYS_CATEGORY.getKeyBindings().length; i++) {
            map.put(F3_DEBUG_KEYS_CATEGORY.getKeyBindings()[i].getTranslationKey(), F3_DEBUG_KEYS_CATEGORY.getKeyBindings()[i].getBoundKeyTranslation());
        }

        return map;
    }
}
