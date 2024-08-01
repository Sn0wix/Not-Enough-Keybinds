package net.sn0wix_.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.sn0wix_.screen.ModKeybindsButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ControlsListWidget.class)
public abstract class ControlsListWidgetMixin extends ElementListWidget<ControlsListWidget.Entry> {
    public ControlsListWidgetMixin(MinecraftClient minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lorg/apache/commons/lang3/ArrayUtils;clone([Ljava/lang/Object;)[Ljava/lang/Object;"), remap = false)
    private Object[] filterKeybinds(Object[] keyBindings) {
        this.addEntry(new ModKeybindsButton(((ControlsListWidget)(Object)this)));
        //return Utils.filterModKeybindings((KeyBinding[]) keyBindings);
        return keyBindings;
    }
}
