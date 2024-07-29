package net.sn0wix_.keybinds;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.sn0wix_.NotEnoughKeybinds;

import java.util.ArrayList;
import java.util.List;

public class ModKeybinds {
    private static final ArrayList<F3ShortcutKeybinding> F3_SHORTCUT_KEYS = new ArrayList<>(8);

    public static final String NOT_ENOUGH_KEYBINDS_CATEGORY = "key.category." + NotEnoughKeybinds.MOD_ID + ".general";
    public static final String F3_SHORTCUTS_CATEGORY = "key.category." + NotEnoughKeybinds.MOD_ID + ".f3_shortcuts";
    public static final String KEY_BINDING_PREFIX = "key." + NotEnoughKeybinds.MOD_ID + ".";


    //custom
    public static final KeyBinding ALWAYS_PLACE_ITEM = registerKeyBinding(new KeyBinding(KEY_BINDING_PREFIX + "always_place_item", InputUtil.UNKNOWN_KEY.getCode(), NOT_ENOUGH_KEYBINDS_CATEGORY));
    public static final KeyBinding THROW_ENDER_PEARL = registerKeyBinding(new KeyBinding(KEY_BINDING_PREFIX + "throw_ender_pearl", InputUtil.UNKNOWN_KEY.getCode(), NOT_ENOUGH_KEYBINDS_CATEGORY));



    //why this isn't a thing in vanilla
    public static final KeyBinding TOGGLE_HIDE_HUD = registerKeyBinding(new KeyBinding(KEY_BINDING_PREFIX + "toggle_hide_hud", InputUtil.GLFW_KEY_F1, KeyBinding.MISC_CATEGORY));


    //f3 shortcuts
    public static final KeyBinding TOGGLE_HITBOXES = registerKeyBinding(new F3ShortcutKeybinding(KEY_BINDING_PREFIX + "toggle_hitboxes", InputUtil.UNKNOWN_KEY.getCode(), F3_SHORTCUTS_CATEGORY, 66));
    public static final KeyBinding TOGGLE_CHUNK_GRID = registerKeyBinding(new F3ShortcutKeybinding(KEY_BINDING_PREFIX + "toggle_chunk_grid", InputUtil.UNKNOWN_KEY.getCode(), F3_SHORTCUTS_CATEGORY, 71));
    public static final KeyBinding TOGGLE_ADVANCED_TOOLTIPS = registerKeyBinding(new F3ShortcutKeybinding(KEY_BINDING_PREFIX + "toggle_advanced_tooltips", InputUtil.UNKNOWN_KEY.getCode(), F3_SHORTCUTS_CATEGORY, 72));
    public static final KeyBinding CLEAR_CHAT_HISTORY = registerKeyBinding(new F3ShortcutKeybinding(KEY_BINDING_PREFIX + "clear_chat_history", InputUtil.UNKNOWN_KEY.getCode(), F3_SHORTCUTS_CATEGORY, 68));
    public static final KeyBinding TOGGLE_SPECTATOR_MODE = registerKeyBinding(new F3ShortcutKeybinding(KEY_BINDING_PREFIX + "toggle_spectator_mode", InputUtil.UNKNOWN_KEY.getCode(), F3_SHORTCUTS_CATEGORY, 78));
    public static final KeyBinding TOGGLE_AUTO_PAUSE = registerKeyBinding(new F3ShortcutKeybinding(KEY_BINDING_PREFIX + "toggle_auto_pause", InputUtil.UNKNOWN_KEY.getCode(), F3_SHORTCUTS_CATEGORY, 80));
    public static final KeyBinding RELOAD_ALL_CHUNKS = registerKeyBinding(new F3ShortcutKeybinding(KEY_BINDING_PREFIX + "reload_all_chunks", InputUtil.UNKNOWN_KEY.getCode(), F3_SHORTCUTS_CATEGORY, 65));
    public static final KeyBinding RELOAD_RESOURCE_PACKS = registerKeyBinding(new F3ShortcutKeybinding(KEY_BINDING_PREFIX + "reload_resource_packs", InputUtil.UNKNOWN_KEY.getCode(), F3_SHORTCUTS_CATEGORY, 84));



    public static void registerModKeybinds() {
        NotEnoughKeybinds.LOGGER.info("Registering key bindings for " + NotEnoughKeybinds.MOD_ID);
    }

    public static KeyBinding registerKeyBinding(KeyBinding keyBinding) {
        if (keyBinding instanceof F3ShortcutKeybinding f3ShortcutKeybinding) {
            F3_SHORTCUT_KEYS.add(f3ShortcutKeybinding);
        }
        return KeyBindingHelper.registerKeyBinding(keyBinding);
    }

    public static List<Integer> checkF3Shortcuts(int key, int scanCode) {
        ArrayList<Integer> codes = new ArrayList<>();

        F3_SHORTCUT_KEYS.forEach(f3ShortcutKeybinding -> {
            if (f3ShortcutKeybinding.matchesKey(key, scanCode)) {
                codes.add(f3ShortcutKeybinding.getCodeToEmulate());
            }
        });

        return codes;
    }
}
