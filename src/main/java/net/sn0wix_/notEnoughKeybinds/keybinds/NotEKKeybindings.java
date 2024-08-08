package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindingCategory;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.ModKeyBinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.NotEKKeybinding;

import java.util.*;

public abstract class NotEKKeybindings {
    private static final ArrayList<KeybindingCategory> KEYBINDING_CATEGORIES = new ArrayList<>();


    public static final String KEY_BINDING_PREFIX = "key." + NotEnoughKeybinds.MOD_ID + ".";


    //why this isn't a thing in vanilla
    public static final KeyBinding TOGGLE_HIDE_HUD = registerKeyBinding(new KeyBinding(KEY_BINDING_PREFIX + "toggle_hide_hud", InputUtil.GLFW_KEY_F1, KeyBinding.MISC_CATEGORY));


    public static void registerModKeybinds() {
        registerKeyCategory(new BuildingKeys().getCategory());
        registerKeyCategory(new F3ShortcutsKeys().getCategory());
        registerKeyCategory(new InventoryKeys().getCategory());
        registerKeyCategory(new SkinLayersKeys().getCategory());
        registerKeyCategory(new F3DebugKeys().getCategory());
    }


    //registering stuff
    public abstract KeybindingCategory getCategory();

    public static void registerKeyCategory(KeybindingCategory category) {
        KEYBINDING_CATEGORIES.add(category);
        KEYBINDING_CATEGORIES.sort(Comparator.comparingInt(KeybindingCategory::getPriority));
    }

    public static void unregisterKeyCategory(KeybindingCategory category) {
        KEYBINDING_CATEGORIES.remove(category);
    }


    public static KeyBinding registerModKeyBinding(ModKeyBinding keyBinding) {
        return KeyBindingHelper.registerKeyBinding(keyBinding);
    }

    public static KeyBinding registerKeyBinding(KeyBinding keyBinding) {
        return KeyBindingHelper.registerKeyBinding(keyBinding);
    }


    //Helper methods
    public static ArrayList<KeybindingCategory> getCategories() {
        return KEYBINDING_CATEGORIES;
    }

    public static List<NotEKKeybinding> getModKeybindsAsList() {
        ArrayList<NotEKKeybinding> keyBindings = new ArrayList<>();
        KEYBINDING_CATEGORIES.forEach(category -> keyBindings.addAll(Arrays.asList(category.getKeyBindings())));
        return keyBindings.stream().toList();
    }

    public static NotEKKeybinding[] getModKeybinds() {
        List<NotEKKeybinding> bindings = getModKeybindsAsList();

        NotEKKeybinding[] list = new NotEKKeybinding[bindings.size()];

        for (int i = 0; i < bindings.size(); i++) {
            list[i] = bindings.get(i);
        }

        return list;
    }
}
