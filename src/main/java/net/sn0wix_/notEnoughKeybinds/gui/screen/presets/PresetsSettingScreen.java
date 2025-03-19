package net.sn0wix_.notEnoughKeybinds.gui.screen.presets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.sn0wix_.notEnoughKeybinds.gui.ParentScreenBlConsumer;
import net.sn0wix_.notEnoughKeybinds.gui.SettingsScreen;
import net.sn0wix_.notEnoughKeybinds.keybinds.presets.PresetLoader;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;
import net.sn0wix_.notEnoughKeybinds.util.Utils;
import org.lwjgl.glfw.GLFW;

public class PresetsSettingScreen extends SettingsScreen {
    public ButtonWidget deleteButton;
    public ButtonWidget loadButton;
    public ButtonWidget editButton;
    public ButtonWidget writeButton;
    public ButtonWidget createNewButton;
    public ButtonWidget backButton;

    private PresetsListWidget presetsList;
    public Text currentPreset;

    public PresetsSettingScreen(Screen parent) {
        super(parent, Text.translatable(TextUtils.getSettingsTranslationKey("presets")),30);
    }

    @Override
    protected void init() {
        initButtons();
        super.init();
    }

    @Override
    public void initHeader() {
        super.initHeader();
    }

    @Override
    protected void initBody() {
        threePartsLayout.addBody(presetsList);
    }

    @Override
    public void initFooter() {
        DirectionalLayoutWidget footerBody = DirectionalLayoutWidget.horizontal().spacing(5);
        DirectionalLayoutWidget left = DirectionalLayoutWidget.vertical().spacing(5);
        DirectionalLayoutWidget right = DirectionalLayoutWidget.vertical().spacing(5);

        left.add(loadButton);
        right.add(createNewButton);

        DirectionalLayoutWidget leftBottom = DirectionalLayoutWidget.horizontal().spacing(5);
        DirectionalLayoutWidget rightBottom = DirectionalLayoutWidget.horizontal().spacing(5);
        leftBottom.add(editButton);
        leftBottom.add(deleteButton);

        rightBottom.add(writeButton);
        rightBottom.add(backButton);

        left.add(leftBottom);
        right.add(rightBottom);

        footerBody.add(left);
        footerBody.add(right);
        threePartsLayout.addFooter(footerBody);
    }

    public void initButtons() {
        this.presetsList = new PresetsListWidget(this, this.client, this.width, this.height - 112, 48, 36);

        this.loadButton = ButtonWidget.builder(TextUtils.getText("load_preset"), button -> {
            if (presetsList.getSelectedOrNull() != null) {
                PresetLoader.loadPreset(presetsList.getFocused().getPreset());
                presetsList.setSelected(null);
            }

            updateScreen();
        }).size(150, 20).build();

        this.createNewButton = ButtonWidget.builder(TextUtils.getText("create_preset"), button -> client.setScreen(new PresetEditScreen(this, new PresetLoader.KeybindPreset(), true))).dimensions(this.width / 2 + 4, this.height - 52, 150, 20).tooltip(TextUtils.getTooltip("create_preset")).build();
        this.editButton = ButtonWidget.builder(TextUtils.getText("edit"), button -> {
            if (presetsList.getSelectedOrNull() != null) {
                client.setScreen(new PresetEditScreen(this, presetsList.getSelectedOrNull().getPreset(), false));
            }
        }).size(72, 20).build();

        this.deleteButton = ButtonWidget.builder(TextUtils.getText("delete"), button -> client.setScreen(Utils.getModConfirmScreen(new ParentScreenBlConsumer(this, client1 -> {
            if (presetsList.getSelectedOrNull() != null) {
                PresetLoader.deletePreset(presetsList.getSelectedOrNull().getPreset());
                this.clearAndInit();
            }
        }, true), TextUtils.getText("preset.delete.confirm", presetsList.getSelectedOrNull().getPreset().getName())))).size(72, 20).build();

        this.writeButton = ButtonWidget.builder(TextUtils.getText("write"), button -> {
            client.setScreen(Utils.getModConfirmScreen(new ParentScreenBlConsumer(this, client1 -> {
                if (presetsList.getSelectedOrNull() != null) {
                    PresetLoader.writePreset(presetsList.getSelectedOrNull().getPreset(), Utils.bindingsToList(false));
                    Utils.showToastNotification(TextUtils.getText("preset.write", presetsList.getSelectedOrNull().getPreset().getName()));
                }
            }, true), TextUtils.getText("preset.write.confirm", presetsList.getSelectedOrNull().getPreset().getName())));
            updateScreen();
        }).size(72, 20).tooltip(TextUtils.getTooltip("write_preset")).build();

        this.backButton = ButtonWidget.builder(ScreenTexts.BACK, button -> this.client.setScreen(this.parent)).size( 72, 20).build();

        updateScreen();
    }

    public void updateScreen() {
        writeButton.active = presetsList.getSelectedOrNull() != null;
        deleteButton.active = presetsList.getSelectedOrNull() != null;
        editButton.active = presetsList.getSelectedOrNull() != null;
        loadButton.active = presetsList.getSelectedOrNull() != null;

        currentPreset = Text.literal(TextUtils.getText("current_preset").getString() + PresetLoader.getCurrentPresetName());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        //TODO add text to header
        context.drawCenteredTextWithShadow(textRenderer, currentPreset, this.width / 2, 32, Colors.GRAY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_F5) {
            PresetLoader.reload(true);
            this.clearAndInit();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}