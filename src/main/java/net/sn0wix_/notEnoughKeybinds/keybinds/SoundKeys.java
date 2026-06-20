package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.INotEKKeybinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.NotEKKeyBinding;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

public class SoundKeys extends NotEKKeyBindings {
    public static final String SOUND_KEYS_CATEGORY_KEY = "key.category." + NotEnoughKeybinds.MOD_ID + ".sound";
    public static final KeyBinding.Category SOUND_KEYS_CATEGORY = KeyBinding.Category.create(NotEnoughKeybinds.getIdentifier(SOUND_KEYS_CATEGORY_KEY));


    public static final NotEKKeyBinding TOGGLE_SUBTITLES = registerModKeyBinding(new NotEKKeyBinding("toggle_subtitles", SOUND_KEYS_CATEGORY, (client, keyBinding) -> {
        client.options.getShowSubtitles().setValue(!client.options.getShowSubtitles().getValue());
    }));

    public static final NotEKKeyBinding INCREASE_MASTER_VOLUME = registerModKeyBinding(new NotEKKeyBinding("increase_master_volume", SOUND_KEYS_CATEGORY, new VolumeKeybindingTicker(SoundCategory.MASTER, 0.1f)));
    public static final NotEKKeyBinding DECREASE_MASTER_VOLUME = registerModKeyBinding(new NotEKKeyBinding("decrease_master_volume", SOUND_KEYS_CATEGORY, new VolumeKeybindingTicker(SoundCategory.MASTER, -0.1f)));


    @Override
    public KeybindCategory getModCategory() {
        INotEKKeybinding[] bindings = new INotEKKeybinding[SoundCategory.values().length + 3];
        bindings[0] = TOGGLE_SUBTITLES;
        bindings[1] = INCREASE_MASTER_VOLUME;
        bindings[2] = DECREASE_MASTER_VOLUME;

        int i = bindings.length - SoundCategory.values().length;

        for (SoundCategory category : SoundCategory.values()) {
            bindings[i] = registerModKeyBinding(new SoundKeybinding("soundCategory." + category.getName(), SOUND_KEYS_CATEGORY, category));
            i++;
        }

        return new KeybindCategory(SOUND_KEYS_CATEGORY_KEY, 10, bindings);
    }


    public static class SoundKeybinding extends NotEKKeyBinding {
        public SoundCategory category;

        public SoundKeybinding(String translationKey, Category category, SoundCategory soundCategory) {
            super(translationKey, category, new ToggleCategoryBindingTicker(soundCategory));
            this.category = soundCategory;
        }

        @Override
        public Text getSettingsDisplayName() {
            return TextUtils.getCombinedTranslation(Text.translatable(TextUtils.getTranslationKey("toggle")),
                    Text.translatable("soundCategory." + category.getName()));
        }
    }


    public static class ToggleCategoryBindingTicker implements INotEKKeybinding.KeybindingTicker {
        private double previousVolume = 1;

        private final SoundCategory category;

        public ToggleCategoryBindingTicker(SoundCategory category) {
            this.category = category;
        }

        @Override
        public void onWasPressed(MinecraftClient client, NotEKKeyBinding keyBinding) {
            if (client.options.getSoundVolumeOption(category).getValue() > 0) {
                previousVolume = client.options.getSoundVolumeOption(category).getValue() > 0 ? client.options.getSoundVolumeOption(category).getValue() : 1;
                client.options.getSoundVolumeOption(category).setValue(0.0);
            } else {
                client.options.getSoundVolumeOption(category).setValue(previousVolume);
            }
        }
    }

    public record VolumeKeybindingTicker(SoundCategory category, float scaling) implements INotEKKeybinding.KeybindingTicker {
        @Override
        public void onTick(MinecraftClient client, NotEKKeyBinding keyBinding) {
            while (keyBinding.wasPressed()) {
                onWasPressed(client, keyBinding);
            }
        }

        @Override
        public void onWasPressed(MinecraftClient client, NotEKKeyBinding keyBinding) {
            double value = client.options.getSoundVolumeOption(category).getValue();
            value += scaling;

            if (value < 0) {
                value = 0;
            } else if (value > 1) {
                value = 1;
            }

            client.options.getSoundVolumeOption(category).setValue(value);
        }
    }
}
