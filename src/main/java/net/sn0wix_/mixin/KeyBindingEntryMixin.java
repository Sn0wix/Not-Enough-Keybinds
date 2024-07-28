package net.sn0wix_.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.sn0wix_.NotEnoughKeybinds;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ControlsListWidget.KeyBindingEntry.class)
public abstract class KeyBindingEntryMixin {
    @Unique
    private static final int NOT_BOUND_BUTTON_DIMENSIONS = 20;

    //private static final Identifier ICON = new Identifier(NotEnoughKeybinds.MOD_ID, "textures/gui/cross_button.png");
    //https://github.com/kawashirov/distant-horizons/blob/main-kawashirov/fabric/src/main/java/com/seibel/distanthorizons/fabric/mixins/client/MixinOptionsScreen.java
    @Shadow
    @Final
    private KeyBinding binding;
    @Unique
    private TextIconButtonWidget notBoundButton;

    @Unique
    private int maxKeyNameLength = -1;


    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/option/ControlsListWidget$KeyBindingEntry;update()V", shift = At.Shift.BEFORE))
    private void injectInit(ControlsListWidget controlsListWidget, KeyBinding binding, Text bindingName, CallbackInfo ci) {
        notBoundButton = TextIconButtonWidget.builder(Text.empty(), button -> NotEnoughKeybinds.LOGGER.info("PRESEDPOIAHEFOPIHSDPOIHSPDOFih"), true)
                .dimension(NOT_BOUND_BUTTON_DIMENSIONS, NOT_BOUND_BUTTON_DIMENSIONS)
                .texture(new Identifier("spectator/close"), 15, 15)
                .build();

        notBoundButton.setTooltip(Tooltip.of(Text.translatable("controls." + NotEnoughKeybinds.MOD_ID + ".notBound.tooltip")));
    }


    @Inject(method = "render", at = @At("TAIL"))
    private void injectRender(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        int xOffset = maxKeyNameLength;
        if (xOffset <= 0) {
            xOffset = 25;
        }

        this.notBoundButton.setX(xOffset - 25);
        this.notBoundButton.setY(y);
        this.notBoundButton.render(context, mouseX, mouseY, tickDelta);
    }

    @Inject(method = "update", at = @At("TAIL"))
    private void injectUpdate(CallbackInfo ci) {
        notBoundButton.active = !this.binding.isUnbound();
    }

    @Inject(method = "children", at = @At(value = "HEAD"), cancellable = true)
    private void injectChildren(CallbackInfoReturnable<List<? extends Element>> cir) {
        cir.setReturnValue(ImmutableList.<Element>builder().addAll(cir.getReturnValue()).add(notBoundButton).build());
        //cir.setReturnValue(ImmutableList.of(this.notBoundButton));
    }

    @Inject(method = "selectableChildren", at = @At(value = "HEAD"), cancellable = true)
    private void injectSelectableChildren(CallbackInfoReturnable<List<? extends Selectable>> cir) {
        cir.setReturnValue(ImmutableList.<Selectable>builder().addAll(cir.getReturnValue()).add(notBoundButton).build());
        //cir.setReturnValue(ImmutableList.of());
        //cir.setReturnValue(ImmutableList.of(this.notBoundButton));
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I"), index = 2)
    private int injectGetMaxKeyNameLength(int x) {
        if (maxKeyNameLength == -1) {
            maxKeyNameLength = x;
        }

        return x;
    }

    @Mixin(ControlsListWidget.class)
    public static abstract class ControlsListMixin {
        /*@Inject(method = "<init>", at = @At("HEAD"), cancellable = true)
        private static void modifyWidth(KeybindsScreen parent, MinecraftClient client, CallbackInfo ci) {
            ci.cancel();
            new ControlsListWidget(parent, client, parent.width + 100 , parent.height - 52, 20, 20);
        }*/
        /*@ModifyArg(method = "<init>", at = @At("HEAD"), index = 1)
        private static int modifyWidth(int width) {
            NotEnoughKeybinds.LOGGER.info("Width: " + width);
            return width; // replace 45 with the new value
        }*/

    /*int   halfRowWidth = this.getRowWidth() / 2;        126
            int centerX = this.getX() + this.width / 2;   266
            int leftBound = centerX - halfRowWidth;       136
            int rightBound = centerX + halfRowWidth;      338
            int adjustedY = MathHelper.floor(y - (double)this.getY()) - this.headerHeight + (int)this.getScrollAmount() - 4;   29
            int itemIndex = adjustedY / this.itemHeight;  20

            if (x >= (double)this.getScrollbarPositionX()) {
                return null;
            }

            if (x < (double)leftBound || x > (double)rightBound) {
                return null;
            }

            if (itemIndex < 0 || adjustedY < 0) {
                return null;
            }

            if (itemIndex >= this.getEntryCount()) {
                return null;
            }

            return this.children().get(itemIndex);
*/
    }
}
