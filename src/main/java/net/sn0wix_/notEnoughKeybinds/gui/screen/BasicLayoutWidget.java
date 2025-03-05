package net.sn0wix_.notEnoughKeybinds.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;

@Environment(EnvType.CLIENT)
public class BasicLayoutWidget extends SimplePositioningWidget {
    private final Screen screen;


    public BasicLayoutWidget(Screen screen) {
        this.screen = screen;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int getWidth() {
        return this.screen.width;
    }

    @Override
    public int getHeight() {
        return this.screen.height;
    }

    @Override
    public void refreshPositions() {
        this.setMinHeight(this.screen.width);
        this.setMinWidth(this.screen.height);
    }
}
