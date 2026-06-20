package net.sn0wix_.notEnoughKeybinds.gui.screen.keybindsScreen;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Util;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
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
        super(parent, Component.translatable("settings." + NotEnoughKeybinds.MOD_ID));
        threePartsLayout = new BasicLayoutWidget(33, this);
    }

    @Override
    protected void init() {
        controlsList = new ControlsListWidget(this, this.minecraft);
        controlsList.setScrollAmount(scrollAmount);
        super.init();
    }

    @Override
    public void initBody() {
        this.threePartsLayout.addToContents(controlsList);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        super.extractRenderState(context, mouseX, mouseY, delta);
        scrollAmount = controlsList.scrollAmount();
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        if (this.selectedKeyBinding != null && selectedKeyBinding.getBinding() != null) {
            assert this.minecraft != null;
            selectedKeyBinding.getBinding().setKey(InputConstants.Type.MOUSE.getOrCreate(click.button()));
            this.selectedKeyBinding = null;
            this.controlsList.update();
            return true;
        } else {
            return super.mouseClicked(click, doubled);
        }
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        if (this.selectedKeyBinding != null) {
            if (input.key() == GLFW.GLFW_KEY_ESCAPE) {
                selectedKeyBinding.setAndSaveKeyBinding(InputConstants.UNKNOWN);
            } else {
                selectedKeyBinding.setAndSaveKeyBinding(InputConstants.getKey(input));
            }

            this.selectedKeyBinding = null;
            this.lastKeyCodeUpdateTime = Util.getMillis();
            this.controlsList.update();
            return true;
        } else {
            return super.keyPressed(input);
        }
    }

    @Override
    public void repositionElements() {
        this.controlsList.updateSize(this.width, this.threePartsLayout);
        super.repositionElements();
    }

    @Override
    public void added() {
        if (controlsList != null) {
            controlsList.initEntries();
            controlsList.update();
        }
    }
}
