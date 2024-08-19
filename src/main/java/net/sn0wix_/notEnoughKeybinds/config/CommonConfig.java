package net.sn0wix_.notEnoughKeybinds.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;

public class CommonConfig {
    public static ConfigClassHandler<CommonConfig> HANDLER = ConfigClassHandler.createBuilder(CommonConfig.class)
            .id(new Identifier(NotEnoughKeybinds.MOD_ID, "common"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve(NotEnoughKeybinds.MOD_ID).resolve("common.json"))
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


    public static CommonConfig getConfig() {
        HANDLER.load();
        return HANDLER.instance();
    }

    public static void saveConfig() {
        HANDLER.save();
    }
}
