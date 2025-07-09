package net.sn0wix_.notEnoughKeybinds.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

public class SwapTotemShieldConfig {
    public static ConfigClassHandler<SwapTotemShieldConfig> HANDLER = ConfigClassHandler.createBuilder(SwapTotemShieldConfig.class)
            .id(Identifier.of(NotEnoughKeybinds.MOD_ID, "swap_totem_shield"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve(NotEnoughKeybinds.MOD_ID).resolve("swap_totem_shield.json"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(false)
                    .build())
            .build();

    @SerialEntry
    public String swapFirst = "off";

    @SerialEntry
    public boolean swapSecond = false;

    @SerialEntry
    public boolean chooseBestShield = true;

    @SerialEntry
    public int swapMendingPoints = 100;

    public String cycleSwapFirst(String oldValue) {
        if (oldValue.equals("off"))
            return "totem";
        if (oldValue.equals("totem"))
            return "shield";
        return "off";
    }

    public void cycleSwapFirst() {
        if (swapFirst.equals("off")) {
            swapFirst = "totem";
        } else if (swapFirst.equals("totem")) {
            swapFirst = "shield";
        } else {
            swapFirst = "off";
        }
    }

    public String getOppositeSwap() {
        return getOppositeSwap(swapFirst);
    }

    public String getOppositeSwap(String oldValue) {
        if (oldValue.equals("totem"))
            return "shield";
        if (oldValue.equals("off"))
            return "off";
        return "totem";
    }

    public String getSwapTranslationKey() {
        return getSwapTranslationKey(swapFirst);
    }

    public String getSwapTranslationKey(String value) {
        String swapValue = TextUtils.getTranslationKey(value);

        if (!value.equals("off")) {
            if (value.equals("shield")) {
                swapValue = Items.SHIELD.getTranslationKey();
            } else if (value.equals("totem")) {
                swapValue = Items.TOTEM_OF_UNDYING.getTranslationKey();
            }
        }

        return swapValue;
    }

    public static SwapTotemShieldConfig getConfig() {
        HANDLER.load();
        return HANDLER.instance();
    }

    public static void saveConfig() {
        HANDLER.save();
    }
}
