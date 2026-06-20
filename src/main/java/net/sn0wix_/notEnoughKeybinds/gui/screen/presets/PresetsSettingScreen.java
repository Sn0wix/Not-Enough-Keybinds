package net.sn0wix_.notEnoughKeybinds.gui.screen.presets;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.sn0wix_.notEnoughKeybinds.gui.ParentScreenBlConsumer;
import net.sn0wix_.notEnoughKeybinds.gui.SettingsScreen;
import net.sn0wix_.notEnoughKeybinds.gui.screen.BasicLayoutWidget;
import net.sn0wix_.notEnoughKeybinds.keybinds.presets.PresetLoader;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;
import net.sn0wix_.notEnoughKeybinds.util.Utils;
import org.lwjgl.glfw.GLFW;

public class PresetsSettingScreen extends SettingsScreen {
    public Button deleteButton;
    public Button loadButton;
    public Button editButton;
    public Button writeButton;
    public Button createNewButton;
    public Button backButton;
    public StringWidget currentPresetText;

    private PresetsListWidget presetsList;

    public PresetsSettingScreen(Screen parent) {
        super(parent, Component.translatable(TextUtils.getSettingsTranslationKey("presets")),
                BasicLayoutWidget.DEFAULT_HEADER_AND_FOOTER_HEIGHT + 8,
                BasicLayoutWidget.DEFAULT_HEADER_AND_FOOTER_HEIGHT + 25);
    }

    @Override
    protected void init() {
        initButtons();
        super.init();
    }

    @Override
    public void initHeader() {
        LinearLayout header = LinearLayout.vertical().spacing(7);
        StringWidget headerText = new StringWidget(title, font);
        headerText.setWidth(200);

        currentPresetText = new StringWidget(Component.nullToEmpty(PresetLoader.getCurrentPresetName()), font);
        currentPresetText.setWidth(200);

        header.addChild(headerText);
        header.addChild(currentPresetText);
        threePartsLayout.addToHeader(header);
    }

    @Override
    protected void initBody() {
        threePartsLayout.addToContents(presetsList);
    }

    @Override
    public void initFooter() {
        LinearLayout footerBody = LinearLayout.horizontal().spacing(5);
        LinearLayout left = LinearLayout.vertical().spacing(5);
        LinearLayout right = LinearLayout.vertical().spacing(5);

        left.addChild(loadButton);
        right.addChild(createNewButton);

        LinearLayout leftBottom = LinearLayout.horizontal().spacing(5);
        LinearLayout rightBottom = LinearLayout.horizontal().spacing(5);
        leftBottom.addChild(editButton);
        leftBottom.addChild(deleteButton);

        rightBottom.addChild(writeButton);
        rightBottom.addChild(backButton);

        left.addChild(leftBottom);
        right.addChild(rightBottom);

        footerBody.addChild(left);
        footerBody.addChild(right);
        threePartsLayout.addToFooter(footerBody);
    }

    @Override
    public void repositionElements() {
        super.repositionElements();

        if (this.presetsList != null) {
            this.presetsList.updateSize(this.width, this.threePartsLayout);
        }
    }

    public void initButtons() {
        this.presetsList = new PresetsListWidget(this, this.minecraft, this.width, threePartsLayout.getContentHeight(), threePartsLayout.getHeaderHeight(), 36);

        this.loadButton = Button.builder(TextUtils.getText("load_preset"), button -> {
            if (presetsList.getSelected() != null) {
                PresetLoader.loadPreset(presetsList.getSelected().getPreset());
            }

            updateScreen();
        }).size(150, 20).build();

        this.createNewButton = Button.builder(TextUtils.getText("create_preset"), button -> minecraft.setScreen(new PresetEditScreen(this, new PresetLoader.KeybindPreset(), true))).bounds(this.width / 2 + 4, this.height - 52, 150, 20).tooltip(TextUtils.getTooltip("create_preset")).build();
        this.editButton = Button.builder(TextUtils.getText("edit"), button -> {
            if (presetsList.getSelected() != null) {
                minecraft.setScreen(new PresetEditScreen(this, presetsList.getSelected().getPreset(), false));
            }
        }).size(72, 20).build();

        this.deleteButton = Button.builder(TextUtils.getText("delete"), button -> minecraft.setScreen(Utils.getModConfirmScreen(new ParentScreenBlConsumer(this, client1 -> {
            if (presetsList.getSelected() != null) {
                PresetLoader.KeybindPreset p = presetsList.getSelected().getPreset();
                PresetLoader.deletePreset(p);
                Utils.showToastNotification(TextUtils.getText("preset.delete.toast", p.getName()));
                this.rebuildWidgets();
            }
        }, true), TextUtils.getText("preset.delete.confirm", presetsList.getSelected().getPreset().getName())))).size(72, 20).build();

        this.writeButton = Button.builder(TextUtils.getText("write"), button -> {
            minecraft.setScreen(Utils.getModConfirmScreen(new ParentScreenBlConsumer(this, client1 -> {
                if (presetsList.getSelected() != null) {
                    PresetLoader.writePreset(presetsList.getSelected().getPreset(), Utils.bindingsToList(false));
                    Utils.showToastNotification(TextUtils.getText("preset.write", presetsList.getSelected().getPreset().getName()));
                }
            }, true), TextUtils.getText("preset.write.confirm", presetsList.getSelected().getPreset().getName())));
            updateScreen();
        }).size(72, 20).tooltip(TextUtils.getTooltip("write_preset")).build();

        this.backButton = Button.builder(CommonComponents.GUI_BACK, button -> {
            //update the parent gui
            if (parent instanceof KeyBindsScreen keybindsScreen) {
                keybindsScreen.children().forEach(child -> {
                    if (child instanceof KeyBindsList widget) {
                        widget.resetMappingAndUpdateButtons();
                    }
                });
            }

            minecraft.setScreen(parent);
        }).size(72, 20).build();

        updateScreen();
    }

    //overwriting protected to public
    @Override
    public void rebuildWidgets() {
        super.rebuildWidgets();
    }

    public void updateScreen() {
        writeButton.active = presetsList.getSelected() != null;
        deleteButton.active = presetsList.getSelected() != null;
        editButton.active = presetsList.getSelected() != null;
        loadButton.active = presetsList.getSelected() != null;

        if (currentPresetText != null) {
            currentPresetText.setMessage(Component.nullToEmpty(PresetLoader.getCurrentPresetName()));
        }
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        if (input.input() == GLFW.GLFW_KEY_F5) {
            PresetLoader.reload(true);
            this.rebuildWidgets();
            return true;
        }

        if (getFocused() != null && getFocused() == presetsList) {
            if (input.input() == GLFW.GLFW_KEY_DELETE && presetsList.getSelected() != null) {
                deleteButton.onPress(input);
                return true;
            } else if (input.input() == GLFW.GLFW_KEY_ENTER && presetsList.getSelected() != null) {
                loadButton.onPress(input);
                return true;
            } else if (input.input() == GLFW.GLFW_KEY_KP_ADD) {
                createNewButton.onPress(input);
                return true;
            }
        } else if (input.input() == GLFW.GLFW_KEY_ENTER && getFocused() instanceof Button buttonWidget) {
            buttonWidget.onPress(input);
        }

        return super.keyPressed(input);
    }
}
