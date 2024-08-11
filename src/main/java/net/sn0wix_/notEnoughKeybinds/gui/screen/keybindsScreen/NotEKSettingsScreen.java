package net.sn0wix_.notEnoughKeybinds.gui.screen.keybindsScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.gui.SettingsScreen;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.INotEKKeybinding;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class NotEKSettingsScreen extends SettingsScreen {
    @Nullable
    public INotEKKeybinding selectedKeyBinding;
    public long lastKeyCodeUpdateTime;
    private ControlsListWidget controlsList;


    public NotEKSettingsScreen(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, Text.translatable("settings." + NotEnoughKeybinds.MOD_ID));
    }

    @Override
    protected void init() {
        this.controlsList = this.addDrawableChild(new ControlsListWidget(this, this.client));
        this.addDrawableChild(
                ButtonWidget.builder(ScreenTexts.DONE, button -> {
                            assert this.client != null;
                            this.client.setScreen(this.parent);
                        })
                        .dimensions(this.width / 2 - 155, this.height - 29, 310, 20)
                        .build()
        );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.selectedKeyBinding != null && selectedKeyBinding.getBinding() != null) {
            this.gameOptions.setKeyCode(selectedKeyBinding.getBinding(), InputUtil.Type.MOUSE.createFromCode(button));
            this.selectedKeyBinding = null;
            this.controlsList.update();
            return true;
        } else {
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.selectedKeyBinding != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                selectedKeyBinding.setAndSaveKeyBinding(InputUtil.UNKNOWN_KEY);
            } else {
                selectedKeyBinding.setAndSaveKeyBinding(InputUtil.fromKeyCode(keyCode, scanCode));
            }

            this.selectedKeyBinding = null;
            this.lastKeyCodeUpdateTime = Util.getMeasuringTimeMs();
            this.controlsList.update();
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }
}
