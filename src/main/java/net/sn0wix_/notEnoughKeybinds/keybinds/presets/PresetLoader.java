package net.sn0wix_.notEnoughKeybinds.keybinds.presets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.config.NotEKSettings;
import net.sn0wix_.notEnoughKeybinds.keybinds.ChatKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3DebugKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.PresetKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.INotEKKeybinding;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;
import net.sn0wix_.notEnoughKeybinds.util.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class PresetLoader {
    public static final Float CURRENT_VERSION = 1f;
    public static final File PRESETS_DIR = new File("config" + File.separator + NotEnoughKeybinds.MOD_ID + File.separator + "presets");
    private static final ArrayList<KeybindPreset> presets = new ArrayList<>();
    private static KeybindPreset currentPreset = null;


    public static void init() {
        PRESETS_DIR.mkdirs();
        List<File> presetFiles = List.of(Objects.requireNonNull(PRESETS_DIR.listFiles()));

        presetFiles.forEach(file -> {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                float version;
                try {
                    version = Float.parseFloat(Utils.nextValue("version", reader));
                } catch (NumberFormatException e) {
                    NotEnoughKeybinds.LOGGER.error("The version string is not a number!");
                    throw new RuntimeException(e);
                }

                if (version == 0) {
                    NotEnoughKeybinds.LOGGER.error("Incorrect version number in " + file.getAbsolutePath());
                    throw new Exception();
                }

                String name = Utils.nextValue("preset_name", reader);
                String description = Utils.nextValue("description", reader);
                List<String> content = new ArrayList<>();

                reader.lines().forEach(content::add);
                reader.close();

                presets.add(new KeybindPreset(file.getName(), version, name, description, content));
            } catch (Exception e) {
                NotEnoughKeybinds.LOGGER.info("Could not read from file " + file.getAbsolutePath(), e);
            }
        });

        AtomicBoolean bl = new AtomicBoolean(false);

        for (KeybindPreset preset : presets) {
            if (preset.getName().equals(getCurrentPresetName())) {
                bl.set(true);
                break;
            }
        }

        if (!bl.get()) {
            NotEnoughKeybinds.GENERAL_CONFIG.currentPreset = "none";
            NotEKSettings.saveConfig();
        }

        NotEnoughKeybinds.LOGGER.info("Presets initialized");
    }

    public static void reload(boolean showToastNotification) {
        NotEnoughKeybinds.LOGGER.info("Reloading presets...");
        if (showToastNotification)
            Utils.showToastNotification(TextUtils.getText("preset.reload"));
        currentPreset = null;
        presets.clear();
        init();
    }

    /**
     * Load presents only after options were initialized
     */
    public static void loadPreset(KeybindPreset preset) {
        if (preset == null) {
            NotEnoughKeybinds.LOGGER.warn("Preset can not be null!");
            return;
        }

        preset.content.forEach(line -> {
            String translation = line.split(":")[0];
            String value = Utils.getValue(line);

            if (translation.isEmpty() || value.isEmpty()) {
                NotEnoughKeybinds.LOGGER.error("Incorrect keybinding entry in preset" + preset.name + ": " + translation + ":" + value);
            } else {
                GameOptions options = MinecraftClient.getInstance().options;
                boolean found = false;

                for (int i = 0; i < options.allKeys.length; i++) {
                    KeyBinding binding = options.allKeys[i];

                    if (binding.getTranslationKey().equals(translation) && !binding.getTranslationKey().equals(PresetKeys.NEXT_PRESET_GLOBAL.getTranslationKey()) && !binding.getTranslationKey().equals(PresetKeys.PREVIOUS_PRESET_GLOBAL.getTranslationKey())) { //check, if the keys are not global preset keys
                        binding.setBoundKey(InputUtil.fromTranslationKey(value));
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    //check if the keys is saved in another file than options.txt
                    if (translation.contains("key." + NotEnoughKeybinds.MOD_ID)) {
                        Stream.of(ChatKeys.CHAT_KEYS_CATEGORY.getKeyBindings(), F3DebugKeys.F3_DEBUG_KEYS_CATEGORY.getKeyBindings()).toList().forEach(iNotEKKeybindings -> {
                            for (INotEKKeybinding keybinding : iNotEKKeybindings) {
                                if (keybinding.getTranslationKey().equals(translation)) {
                                    keybinding.setBoundKey(InputUtil.fromTranslationKey(value));
                                    break;
                                }
                            }
                        });
                    }
                }
            }
        });

        KeyBinding.updateKeysByCode();
        KeyBinding.unpressAll();
        KeyBinding.updatePressedStates();

        setCurrentPreset(preset);
        Utils.showToastNotification(Text.translatable(TextUtils.getTranslationKey("preset.load"), preset.getName()));
        NotEnoughKeybinds.LOGGER.info("Preset " + preset.getName() + " was loaded!");
    }

    public static void deletePreset(KeybindPreset preset) {
        File file = new File(PRESETS_DIR.getPath(), preset.getFileName());
        if (!file.delete()) {
            NotEnoughKeybinds.LOGGER.error("Could not delete preset file " + preset.getFileName());
        } else {
            NotEnoughKeybinds.LOGGER.info("Deleted preset " + preset.getFileName());
        }

        reload(false);
    }

    public static void writePreset(KeybindPreset preset, File file) {
        try {
            PRESETS_DIR.mkdirs();

            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            writer.write("version:" + CURRENT_VERSION);
            writer.newLine();
            writer.write("name:" + preset.name);
            writer.newLine();
            writer.write("description:" + preset.description);
            writer.newLine();

            preset.content.forEach(line -> {
                try {
                    writer.write(line);
                    writer.newLine();
                } catch (IOException e) {
                    NotEnoughKeybinds.LOGGER.error("Could not write " + line + " into " + file.getAbsolutePath());
                }
            });

            writer.close();
        } catch (IOException e) {
            NotEnoughKeybinds.LOGGER.error("Could not create file writer of file " + file.getAbsolutePath());
        }
    }

    public static void writePreset(KeybindPreset preset) {
        writePreset(addFileNameIf(preset), new File(PRESETS_DIR, preset.getFileName()));
    }

    public static void writePreset(KeybindPreset preset, List<String> content) {
        preset.setContent(content);
        writePreset(addFileNameIf(preset), new File(PRESETS_DIR, preset.getFileName()));
    }

    public static List<KeybindPreset> getPresets() {
        return presets.stream().toList();
    }

    public static String getCurrentPresetName() {
        return currentPreset == null ? NotEnoughKeybinds.GENERAL_CONFIG.currentPreset : currentPreset.getName();
    }

    public static KeybindPreset getCurrentPreset() {
        return currentPreset;
    }

    public static KeybindPreset addFileNameIf(KeybindPreset preset) {
        if (preset.getFileName().isEmpty()) {
            int index = (Objects.requireNonNull(PRESETS_DIR.listFiles()).length);

            while (true) {
                index++;

                boolean found = false;
                for (File file : Objects.requireNonNull(PRESETS_DIR.listFiles())) {
                    if (file.getName().equals("preset" + index + ".txt")) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    break;
                }
            }

            preset.setFileName("preset" + index + ".txt");
        }

        return preset;
    }

    public static void setCurrentPreset(KeybindPreset preset) {
        currentPreset = preset;
        NotEnoughKeybinds.GENERAL_CONFIG.currentPreset = preset.getName();
        NotEKSettings.saveConfig();
    }


    public static class KeybindPreset {
        private String fileName = "";
        private float version;
        private String name;
        private String description;
        private List<String> content;

        public KeybindPreset(String fileName, float version, String name, String description, List<String> content) {
            this.fileName = fileName;
            this.version = version;
            this.name = name;
            this.description = description;
            this.content = content;
        }

        public KeybindPreset() {
            this.fileName = "";
            this.version = PresetLoader.CURRENT_VERSION;
            this.name = "";
            this.description = "";
            this.content = List.of();
        }

        public KeybindPreset(float version, String name, String description, List<String> content) {
            this.version = version;
            this.name = name;
            this.description = description;
            this.content = content;
        }


        public String getFileName() {
            return fileName;
        }

        public float getVersion() {
            return version;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public List<String> getContent() {
            return content;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public void setVersion(float version) {
            this.version = version;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setContent(List<String> content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "KeybindPreset{" +
                    "fileName='" + fileName + '\'' +
                    ", version=" + version +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", content=" + content +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            KeybindPreset that = (KeybindPreset) o;
            return Float.compare(that.version, version) == 0 &&
                    Objects.equals(fileName, that.fileName) &&
                    Objects.equals(name, that.name) &&
                    Objects.equals(description, that.description) &&
                    Objects.equals(content, that.content);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fileName, version, name, description, content);
        }
    }
}
