package io.github.fablabsmc.fablabs.impl.bannerpattern;

import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.event.registry.RegistryIdRemapCallback;

public final class Bannerpp implements ModInitializer {
	// metadata
	public static final String MODID = "bannerpp";

	public static final Registry<LoomPattern> LOOM_PATTERN_REGISTRY = FabricRegistryBuilder
			.createSimple(LoomPattern.class, new Identifier(MODID, "loom_patterns"))
			.attribute(RegistryAttribute.SYNCED)
			.buildAndRegister();

	@Override
	public void onInitialize() {
		RegistryIdRemapCallback.event(LOOM_PATTERN_REGISTRY).register(state -> LoomPatternsInternal.remapLoomIndices());

		// registry sync is longer called on the server or in singleplayer, so we must set up the indices here using
		// a registry item added callback.
		for (LoomPattern p : LOOM_PATTERN_REGISTRY) {
			LoomPatternsInternal.addPattern(p);
		}

		RegistryEntryAddedCallback.event(LOOM_PATTERN_REGISTRY).register((raw, id, p) -> LoomPatternsInternal.addPattern(p));
	}
}
