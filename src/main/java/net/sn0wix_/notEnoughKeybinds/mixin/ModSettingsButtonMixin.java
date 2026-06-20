package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.sn0wix_.notEnoughKeybinds.gui.screen.keybindsScreen.ModKeybindsButton;
import net.sn0wix_.notEnoughKeybinds.gui.screen.presets.PresetsButton;
import net.sn0wix_.notEnoughKeybinds.util.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBindsList.class)
public abstract class ModSettingsButtonMixin extends ContainerObjectSelectionList<KeyBindsList.Entry> {
    public ModSettingsButtonMixin(Minecraft minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lorg/apache/commons/lang3/ArrayUtils;clone([Ljava/lang/Object;)[Ljava/lang/Object;"), remap = false)
    private Object[] filterKeybinds(Object[] keyBindings) {
        return Utils.filterModKeybindings((KeyMapping[]) keyBindings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectTail(KeyBindsScreen parent, Minecraft client, CallbackInfo ci) {
        this.addEntryToTop(new PresetsButton(((KeyBindsList)(Object)this), client.font));
        this.addEntryToTop(new ModKeybindsButton(((KeyBindsList)(Object)this)));
        this.setScrollAmount(0);
    }
}
