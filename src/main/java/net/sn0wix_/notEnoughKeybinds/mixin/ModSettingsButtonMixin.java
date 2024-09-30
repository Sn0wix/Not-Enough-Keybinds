package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.option.KeyBinding;
import net.sn0wix_.notEnoughKeybinds.gui.ModKeybindsButton;
import net.sn0wix_.notEnoughKeybinds.gui.PresetButtons;
import net.sn0wix_.notEnoughKeybinds.util.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlsListWidget.class)
public abstract class ModSettingsButtonMixin extends ElementListWidget<ControlsListWidget.Entry> {
    public ModSettingsButtonMixin(MinecraftClient minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lorg/apache/commons/lang3/ArrayUtils;clone([Ljava/lang/Object;)[Ljava/lang/Object;"), remap = false)
    private Object[] filterKeybinds(Object[] keyBindings) {
        return Utils.filterModKeybindings((KeyBinding[]) keyBindings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectTail(KeybindsScreen parent, MinecraftClient client, CallbackInfo ci) {
        this.addEntryToTop(new PresetButtons(((ControlsListWidget)(Object)this)));
        this.addEntryToTop(new ModKeybindsButton(((ControlsListWidget)(Object)this)));
        this.setScrollAmount(0);
    }
}
