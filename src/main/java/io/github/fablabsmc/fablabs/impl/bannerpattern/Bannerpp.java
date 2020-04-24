package io.github.fablabsmc.fablabs.impl.bannerpattern;

import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatterns;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryIdRemapCallback;

public final class Bannerpp implements ModInitializer {
	// metadata
	public static final String MODID = "bannerpp";

	@Override
	public void onInitialize() {
		Registry.register(Registry.REGISTRIES, new Identifier(MODID, "loom_patterns"), LoomPatterns.REGISTRY);
		RegistryIdRemapCallback.event(LoomPatterns.REGISTRY).register(state -> LoomPatterns.remapLoomIndices());
	}
}
