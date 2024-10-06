package net.sn0wix_.notEnoughKeybinds.gui.screen.presets;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.sn0wix_.notEnoughKeybinds.gui.SettingsScreen;
import net.sn0wix_.notEnoughKeybinds.keybinds.presets.PresetLoader;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;
import net.sn0wix_.notEnoughKeybinds.util.Utils;

public class PresetEditScreen extends SettingsScreen {
    public static final Text NAME_TEXT = Text.translatable(TextUtils.getTranslationKey("name")).formatted(Formatting.GRAY);
    public static final Text DESCRIPTION_TEXT = Text.translatable(TextUtils.getTranslationKey("description")).formatted(Formatting.GRAY);


    public final PresetLoader.KeybindPreset preset;
    public TextFieldWidget nameWidget;
    public TextFieldWidget descriptionWidget;
    public ButtonWidget backButton;

    public String name;
    public String description;
    public boolean newPreset;


    public PresetEditScreen(Screen parent, PresetLoader.KeybindPreset preset, boolean newPreset) {
        super(parent, Text.translatable(TextUtils.getSettingsTranslationKey(newPreset ? "preset.new" : "preset")));
        this.preset = preset;
        this.name = preset.getName();
        this.description = preset.getDescription();
        this.newPreset = newPreset;
    }

    @Override
    public void init(int x, int x2, int y, TextRenderer textRenderer) {
        backButton = ButtonWidget.builder(ScreenTexts.BACK, button -> client.setScreen(parent)
        ).dimensions(x + 240, this.height - 29, 70, 20).build();
        addDrawableChild(backButton);

        addDoneButton(button -> {
            preset.setName(nameWidget.getText());
            preset.setDescription(descriptionWidget.getText());
            PresetLoader.writePreset(preset, preset.getContent().isEmpty() ? Utils.bindingsToList(true) : preset.getContent());
            PresetLoader.reload(false);

            assert client != null;
            client.setScreen(parent);
        }, 0, 0, 230, 20);

        nameWidget = new TextFieldWidget(textRenderer, x, y, 310, 20, NAME_TEXT);
        nameWidget.setChangedListener(s -> updateChildren());
        nameWidget.setMaxLength(Integer.MAX_VALUE);
        nameWidget.setText(name);
        addDrawableChild(nameWidget);


        addDrawableChild(new TextWidget(x, y - 20, 200, 20, NAME_TEXT, textRenderer).alignLeft());
        y += 30;
        addDrawableChild(new TextWidget(x, y, 200, 20, DESCRIPTION_TEXT, textRenderer).alignLeft());


        descriptionWidget = new TextFieldWidget(textRenderer, x, y + 20, 310, 20, DESCRIPTION_TEXT);
        descriptionWidget.setMaxLength(Integer.MAX_VALUE);
        descriptionWidget.setText(description);

        addDrawableChild(descriptionWidget);

        setInitialFocus(nameWidget);
        updateChildren();
    }

    public void updateChildren() {
        try {
            description = descriptionWidget.getText();
            name = nameWidget.getText();
            doneButton.active = !nameWidget.getText().isEmpty();
        } catch (NullPointerException ignored) {
        }
    }
}
