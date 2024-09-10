package net.sn0wix_.notEnoughKeybinds.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

public class EquipElytraConfig {
    public static ConfigClassHandler<EquipElytraConfig> HANDLER = ConfigClassHandler.createBuilder(EquipElytraConfig.class)
            .id(new Identifier(NotEnoughKeybinds.MOD_ID, "equip_elytra"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve(NotEnoughKeybinds.MOD_ID).resolve("equip_elytra.json"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(false)
                    .build())
            .build();

    @SerialEntry
    public String swapFirst = "off";//
    @SerialEntry
    public boolean swapSecond = false;//
    @SerialEntry
    public boolean chooseBestChestplate = true;//
    @SerialEntry
    public boolean chooseBestElytra = true;//
    @SerialEntry
    public boolean acceptCurseOfBinding = false;//
    @SerialEntry
    public boolean acceptCurseOfVanishing = true;//
    @SerialEntry
    public boolean enterFlightMode = true;//
    @SerialEntry
    public boolean canExplode = false;//
    @SerialEntry
    public boolean longestDuration = false;//
    @SerialEntry
    public byte fireworkSwapSlot = -1;//
    @SerialEntry
    public byte equipMode = 2;//
    @SerialEntry
    public boolean swapBackOldItem = true;
    @SerialEntry
    public boolean useRocket = true;//
    @SerialEntry
    public boolean selectRocket = true;//
    @SerialEntry
    public boolean autoDetect = false;


    public void cycleUseMode() {
        equipMode++;
        if (equipMode > 4)
            equipMode = 0;
    }

    public String getUseModeString() {
        if (equipMode == 0)
            return "off";
        if (equipMode == 1)
            return "hotbar";
        if (equipMode == 2)
            return "current_slot";
        if (equipMode == 3)
            return "offhand";

        return "quick_use";
    }

    public void cycleSlot() {
        if (PlayerInventory.isValidHotbarIndex(fireworkSwapSlot)) {
            fireworkSwapSlot++;
        }

        if (!PlayerInventory.isValidHotbarIndex(fireworkSwapSlot)) {
            fireworkSwapSlot = 0;
        }
    }

    public void cycleSwapFirst() {
        if (swapFirst.equals("off")) {
            swapFirst = "chestplate";
        } else if (swapFirst.equals("chestplate")) {
            swapFirst = "elytra";
        } else {
            swapFirst = "off";
        }
    }

    public String getOppositeSwap() {
        return getOppositeSwap(swapFirst);
    }

    public String getOppositeSwap(String oldValue) {
        if (oldValue.equals("chestplate"))
            return "elytra";
        if (oldValue.equals("elytra"))
            return "chestplate";
        return "off";
    }

    public String getSwapTranslationKey() {
        return getSwapTranslationKey(swapFirst);
    }

    public String getSwapTranslationKey(String value) {
        String swapValue = TextUtils.getTranslationKey(value);

        if (!value.equals("off")) {
            if (value.equals("elytra")) {
                swapValue = Items.ELYTRA.getTranslationKey();
            } else if (value.equals("chestplate")) {
                swapValue = TextUtils.getTranslationKey("chestplate");
            }
        }

        return swapValue;
    }

    public static EquipElytraConfig getConfig() {
        HANDLER.load();
        return HANDLER.instance();
    }

    public static void saveConfig() {
        HANDLER.save();
    }
}
