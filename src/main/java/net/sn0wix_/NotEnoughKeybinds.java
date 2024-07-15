package net.sn0wix_;

import net.fabricmc.api.ClientModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class NotEnoughKeybinds implements ClientModInitializer {
    public static final String MOD_ID = "not-enough-keybinds";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {

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