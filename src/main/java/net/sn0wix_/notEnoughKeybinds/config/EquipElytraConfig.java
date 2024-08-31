package net.sn0wix_.notEnoughKeybinds.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;

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
    public String swapFirst = "off";
    @SerialEntry
    public boolean swapSecond = false;
    @SerialEntry
    public boolean chooseBestChestplate = true;
    @SerialEntry
    public boolean chooseBestElytra = true;
    @SerialEntry
    public boolean acceptCurseOfBinding = false;
    @SerialEntry
    public boolean acceptCurseOfVanishing = false;
    @SerialEntry
    public boolean autoFly = false;


    @SerialEntry
    public boolean canExplode = false;
    @SerialEntry
    public boolean longestDuration = false;
    @SerialEntry
    public byte fireworkSwapSlot = 0;
    @SerialEntry
    public boolean useFireworks = false;
    @SerialEntry
    public boolean swapBackOldItem = true;


    public String cycleSwapFirst(String oldValue) {
        if (oldValue.equals("off"))
            return "chestplate";
        if (oldValue.equals("chestplate"))
            return "elytra";
        return "off";
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
        if (oldValue.equals("off"))
            return "off";
        return "elytra";
    }

    public static EquipElytraConfig getConfig() {
        HANDLER.load();
        return HANDLER.instance();
    }

    public static void saveConfig() {
        HANDLER.save();
    }
}
