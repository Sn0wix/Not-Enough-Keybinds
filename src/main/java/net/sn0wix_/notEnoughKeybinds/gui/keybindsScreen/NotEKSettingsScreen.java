package net.sn0wix_.notEnoughKeybinds.gui.keybindsScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3DebugKeys;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class NotEKSettingsScreen extends GameOptionsScreen {
    @Nullable
    public KeyBinding selectedKeyBinding;
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
        if (this.selectedKeyBinding != null) {
            this.gameOptions.setKeyCode(this.selectedKeyBinding, InputUtil.Type.MOUSE.createFromCode(button));
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
            if (selectedKeyBinding instanceof F3DebugKeys.F3DebugKeybinding) {
                NotEnoughKeybinds.LOGGER.info("DEBUG KEY");
            } else {
                if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                    this.gameOptions.setKeyCode(this.selectedKeyBinding, InputUtil.UNKNOWN_KEY);
                } else {
                    this.gameOptions.setKeyCode(this.selectedKeyBinding, InputUtil.fromKeyCode(keyCode, scanCode));
                }
            }

            this.selectedKeyBinding = null;
            this.lastKeyCodeUpdateTime = Util.getMeasuringTimeMs();
            this.controlsList.update();
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 8, 16777215);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
    }
}
