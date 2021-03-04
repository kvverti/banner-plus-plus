package io.github.fablabsmc.fablabs.impl.bannerpattern;

import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatterns;

import net.minecraft.client.render.TexturedRenderLayers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;

public class BannerppClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientSpriteRegistryCallback.event(TexturedRenderLayers.BANNER_PATTERNS_ATLAS_TEXTURE)
				.register((texture, registry) -> {
					for (LoomPattern pattern : LoomPatterns.REGISTRY) {
						registry.register(pattern.getSpriteId("banner"));
					}
				});
		ClientSpriteRegistryCallback.event(TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE)
				.register((texture, registry) -> {
					for (LoomPattern pattern : LoomPatterns.REGISTRY) {
						registry.register(pattern.getSpriteId("shield"));
					}
				});
	}
}
