package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBindsList.class)
public interface ControlsListWidgetAccessor {
    @Accessor("keyBindsScreen")
    KeyBindsScreen getParent();
}
