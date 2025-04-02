package net.sn0wix_.notEnoughKeybinds.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TexturedButtonWidget extends ButtonWidget {
    public Identifier TEXTURE;
    public int spriteWidth;
    public int spriteHeight;
    public int textureWidth;
    public int textureHeight;

    public TexturedButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, NarrationSupplier narrationSupplier, Identifier texture, int spriteWidth, int spriteHeight, int textureWidth, int textureHeight) {
        super(x, y, width, height, message, onPress, narrationSupplier);
        TEXTURE = texture;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }


    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
        if (TEXTURE != null) {
            context.drawTexture(RenderLayer::getGuiTextured,TEXTURE, this.getX() + (this.width - spriteWidth) / 2, this.getY() + (this.height - spriteHeight) / 2, 0, 0, 0, spriteWidth, spriteHeight, textureWidth, textureHeight);
        }
    }
}
