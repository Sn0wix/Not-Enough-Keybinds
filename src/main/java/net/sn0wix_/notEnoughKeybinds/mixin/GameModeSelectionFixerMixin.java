package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3DebugKeys;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameModeSelectionScreen.class)
public abstract class GameModeSelectionFixerMixin {
    @ModifyVariable(method = "keyPressed", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public int modifyF4(int value) {
        if (F3DebugKeys.GAMEMODES.boundKey.getCode() == value) {
            return GLFW.GLFW_KEY_F4;
        } else if (value == GLFW.GLFW_KEY_F4) {
            return F3DebugKeys.GAMEMODES.boundKey.getCode();
        }

        return value;
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;"), index = 1)
    private static Object[] injectF4Text(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Text text) {
                NotEnoughKeybinds.LOGGER.info(text.getString());
                if (text.getString().contains("debug.gamemodes.press_f4")) {
                    args[i] = Text.translatable("debug." + NotEnoughKeybinds.MOD_ID + ".press_next", F3DebugKeys.GAMEMODES.boundKey.getLocalizedText()).formatted(Formatting.AQUA);
                }
            }
        }

        return args;
    }
}
