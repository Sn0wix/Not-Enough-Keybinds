package net.sn0wix_.keybinds;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.sn0wix_.NotEnoughKeybinds;

public class ModKeybinds {
    public static final String KEY_CATEGORY = "key.category." + NotEnoughKeybinds.MOD_ID + ".general";
    public static final String KEY_BINDING_PREFIX = "key." + NotEnoughKeybinds.MOD_ID + ".";

    public static final KeyBinding ALWAYS_USE_ITEM = KeyBindingHelper.registerKeyBinding(new KeyBinding(KEY_BINDING_PREFIX + "always_use_item", InputUtil.UNKNOWN_KEY.getCode(), KEY_CATEGORY));



    public static void registerModKeybinds() {
        NotEnoughKeybinds.LOGGER.info("Registering key bindings for " + NotEnoughKeybinds.MOD_ID);
    }
}
