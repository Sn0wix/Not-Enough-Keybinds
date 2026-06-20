package net.sn0wix_.notEnoughKeybinds.gui.screen.keybindsScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.gui.SettingsScreen;
import net.sn0wix_.notEnoughKeybinds.gui.screen.BasicLayoutWidget;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.INotEKKeybinding;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class NotEKSettingsScreen extends SettingsScreen {
    @Nullable
    public INotEKKeybinding selectedKeyBinding;
    public long lastKeyCodeUpdateTime;
    private ControlsListWidget controlsList;
    public double scrollAmount = 0;


    public NotEKSettingsScreen(Screen parent) {
        super(parent, Text.translatable("settings." + NotEnoughKeybinds.MOD_ID));
        threePartsLayout = new BasicLayoutWidget(33, this);
    }

    @Override
    protected void init() {
        controlsList = new ControlsListWidget(this, this.client);
        controlsList.setScrollY(scrollAmount);
        super.init();
    }

    @Override
    public void initBody() {
        this.threePartsLayout.addBody(controlsList);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        scrollAmount = controlsList.getScrollY();
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (this.selectedKeyBinding != null && selectedKeyBinding.getBinding() != null) {
            assert this.client != null;
            selectedKeyBinding.getBinding().setBoundKey(InputUtil.Type.MOUSE.createFromCode(click.button()));
            this.selectedKeyBinding = null;
            this.controlsList.update();
            return true;
        } else {
            return super.mouseClicked(click, doubled);
        }
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (this.selectedKeyBinding != null) {
            if (input.key() == GLFW.GLFW_KEY_ESCAPE) {
                selectedKeyBinding.setAndSaveKeyBinding(InputUtil.UNKNOWN_KEY);
            } else {
                selectedKeyBinding.setAndSaveKeyBinding(InputUtil.fromKeyCode(input));
            }

            this.selectedKeyBinding = null;
            this.lastKeyCodeUpdateTime = Util.getMeasuringTimeMs();
            this.controlsList.update();
            return true;
        } else {
            return super.keyPressed(input);
        }
    }

    @Override
    public void refreshWidgetPositions() {
        this.controlsList.position(this.width, this.threePartsLayout);
        super.refreshWidgetPositions();
    }

    @Override
    public void onDisplayed() {
        if (controlsList != null) {
            controlsList.initEntries();
            controlsList.update();
        }
    }
}
