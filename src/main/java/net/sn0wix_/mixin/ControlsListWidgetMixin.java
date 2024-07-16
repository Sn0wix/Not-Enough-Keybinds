package net.sn0wix_.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.sn0wix_.NotEnoughKeybinds;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ControlsListWidget.KeyBindingEntry.class)
public abstract class ControlsListWidgetMixin {
    @Shadow @Final private KeyBinding binding;
    @Unique
    private ButtonWidget notBoundButton;

    @Unique
    private int maxKeyNameLength = -1;


    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/option/ControlsListWidget$KeyBindingEntry;update()V", shift = At.Shift.BEFORE))
    private void init(ControlsListWidget controlsListWidget, KeyBinding binding, Text bindingName, CallbackInfo ci) {
        notBoundButton = TextIconButtonWidget.builder(Text.empty(), button -> {
                    NotEnoughKeybinds.LOGGER.info("PRESS");
                    //MinecraftClient.getInstance().options.setKeyCode(binding, InputUtil.UNKNOWN_KEY);
                    //((ControlsListWidget) (Object) this).update();
                }, true)
                .dimension(20, 20)
                .texture(new Identifier(NotEnoughKeybinds.MOD_ID, "icon/cross_button1"), 14, 14)
                .build();

        notBoundButton.setTooltip(Tooltip.of(Text.translatable("controls." + NotEnoughKeybinds.MOD_ID + ".notBound.tooltip")));
    }


    @Inject(method = "render", at = @At("TAIL"))
    private void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        int xOffset = maxKeyNameLength;
        if (xOffset <= 0) {
            xOffset = 25;
        }

        MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier(NotEnoughKeybinds.MOD_ID, "icon/cross_button1"));

        this.notBoundButton.setX(xOffset - 25);
        this.notBoundButton.setY(y);
        this.notBoundButton.render(context, mouseX, mouseY, tickDelta);
    }

    @Inject(method = "update", at = @At("TAIL"))
    private void update(CallbackInfo ci) {
        notBoundButton.active = !this.binding.isUnbound();
    }


    @Inject(method = "children", at = @At("RETURN"))
    private void children(CallbackInfoReturnable<List<? extends Element>> cir) {
        ImmutableList.builder().addAll(cir.getReturnValue()).add(notBoundButton);
    }

    @Inject(method = "selectableChildren", at = @At("RETURN"))
    private void selectableChildren(CallbackInfoReturnable<List<? extends Selectable>> cir) {
        ImmutableList.builder().addAll(cir.getReturnValue()).add(notBoundButton);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I"), index = 2)
    private int getMaxKeyNameLength(int x) {
        if (maxKeyNameLength == -1) {
            maxKeyNameLength = x;
        }

        return x;
    }
}
