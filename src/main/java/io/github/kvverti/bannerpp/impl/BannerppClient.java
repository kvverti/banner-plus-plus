package io.github.kvverti.bannerpp.impl;

import io.github.kvverti.bannerpp.api.LoomPattern;
import io.github.kvverti.bannerpp.api.LoomPatterns;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;

import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.util.Identifier;

public class BannerppClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientSpriteRegistryCallback.event(TexturedRenderLayers.BANNER_PATTERNS_ATLAS_TEXTURE)
            .register((texture, registry) -> {
                for(Identifier id : LoomPatterns.REGISTRY.getIds()) {
                    registry.register(LoomPattern.getSpriteId(id, "banner"));
                }
            });
        ClientSpriteRegistryCallback.event(TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE)
            .register((texture, registry) -> {
                for(Identifier id : LoomPatterns.REGISTRY.getIds()) {
                    registry.register(LoomPattern.getSpriteId(id, "shield"));
                }
            });
    }
}
