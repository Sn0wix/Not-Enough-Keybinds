package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.INotEKKeybinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.NotEKKeyBinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.presets.PresetLoader;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

import java.util.NoSuchElementException;

public class PresetKeys extends NotEKKeyBindings {
    public static final String PRESET_CATEGORY = "key.category." + NotEnoughKeybinds.MOD_ID + ".presets";

    public static final NotEKKeyBinding NEXT_PRESET = registerModKeyBinding(new NotEKKeyBinding("next_preset", PRESET_CATEGORY, new PresetBinding(true)));
    public static final NotEKKeyBinding PREVIOUS_PRESET = registerModKeyBinding(new NotEKKeyBinding("previous_preset", PRESET_CATEGORY, new PresetBinding(false)));

    public static final NotEKKeyBinding NEXT_PRESET_GLOBAL = registerModKeyBinding(new NotEKKeyBinding("next_preset_global", PRESET_CATEGORY, new PresetBinding(true)) {
        @Override
        public Text getTooltip() {
            return TextUtils.getText("key_global", true);
        }
    });
    public static final NotEKKeyBinding PREVIOUS_PRESET_GLOBAL = registerModKeyBinding(new NotEKKeyBinding("previous_preset_global", PRESET_CATEGORY, new PresetBinding(false)) {
        @Override
        public Text getTooltip() {
            return TextUtils.getText("key_global", true);
        }
    });

    @Override
    public KeybindCategory getCategory() {
        return new KeybindCategory(PRESET_CATEGORY, 3, false, NEXT_PRESET_GLOBAL, PREVIOUS_PRESET_GLOBAL, NEXT_PRESET, PREVIOUS_PRESET);
    }

    public static class PresetBinding implements INotEKKeybinding.KeybindingTicker {
        private final boolean next;

        public PresetBinding(boolean next) {
            this.next = next;
        }

        @Override
        public void onWasPressed(MinecraftClient client, NotEKKeyBinding keyBinding) {
            try {
                if (PresetLoader.getCurrentPreset() == null) {
                    try {
                        PresetLoader.loadPreset(PresetLoader.getPresets().getFirst());
                    } catch (NoSuchElementException e) {
                        NotEnoughKeybinds.LOGGER.info("Cannot load the next or previous preset because none exist!");
                    }

                    return;
                }

                int currentIndex = PresetLoader.getPresets().indexOf(PresetLoader.getCurrentPreset());

                if (next) {
                    currentIndex = (currentIndex + 1) % PresetLoader.getPresets().size();
                } else {
                    currentIndex = (currentIndex - 1 + PresetLoader.getPresets().size()) % PresetLoader.getPresets().size();
                }

                PresetLoader.loadPreset(PresetLoader.getPresets().get(currentIndex));
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
    }
}
