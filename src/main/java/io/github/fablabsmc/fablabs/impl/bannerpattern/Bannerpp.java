package io.github.fablabsmc.fablabs.impl.bannerpattern;

import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatterns;
import io.github.fablabsmc.fablabs.mixin.bannerpattern.RegistryAccessor;

import net.minecraft.util.registry.MutableRegistry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryIdRemapCallback;

public final class Bannerpp implements ModInitializer {
	// metadata
	public static final String MODID = "bannerpp";

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void onInitialize() {
		// Mojang's root registry does not type check
		MutableRegistry root = RegistryAccessor.getRoot();
		root.add(LoomPatterns.REGISTRY_KEY, LoomPatterns.REGISTRY);
		RegistryIdRemapCallback.event(LoomPatterns.REGISTRY).register(state -> LoomPatterns.remapLoomIndices());
	}
}
