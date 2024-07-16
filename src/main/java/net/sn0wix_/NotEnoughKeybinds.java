package net.sn0wix_;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.sn0wix_.events.ClientEndTickEvent;
import net.sn0wix_.keybinds.ModKeybinds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class NotEnoughKeybinds implements ClientModInitializer {
    public static final String MOD_ID = "not-enough-keybinds";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        ModKeybinds.registerModKeybinds();

        ClientTickEvents.END_CLIENT_TICK.register(new ClientEndTickEvent());

        /*ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier(MOD_ID, "icon_loader");
            }

            @Override
            public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
                return CompletableFuture.runAsync(() -> {
                    MinecraftClient.getInstance().getTextureManager().registerTexture(Sprites.NOT_BOUND_BUTTON_ICON, new SpriteAtlasTexture(Sprites.NOT_BOUND_BUTTON_ICON));
                });
            }
        });*/
    }
}