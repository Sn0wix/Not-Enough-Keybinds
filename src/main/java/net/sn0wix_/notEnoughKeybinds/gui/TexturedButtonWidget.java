package net.sn0wix_.notEnoughKeybinds.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class TexturedButtonWidget extends Button {
    public Identifier TEXTURE;
    public int spriteWidth;
    public int spriteHeight;
    public int textureWidth;
    public int textureHeight;

    public TexturedButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress, CreateNarration narrationSupplier, Identifier texture, int spriteWidth, int spriteHeight, int textureWidth, int textureHeight) {
        super(x, y, width, height, message, onPress, narrationSupplier);
        TEXTURE = texture;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }


    @Override
    public void extractContents(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        if (TEXTURE != null) {
            context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.getX() + (this.width - spriteWidth) / 2,
                    this.getY() + (this.height - spriteHeight) / 2, 0, 0,
                    spriteWidth, spriteHeight, textureWidth, textureHeight);
        }
    }
}
