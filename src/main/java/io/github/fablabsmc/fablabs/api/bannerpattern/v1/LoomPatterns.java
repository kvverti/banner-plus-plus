package io.github.fablabsmc.fablabs.api.bannerpattern.v1;

import io.github.fablabsmc.fablabs.impl.bannerpattern.Bannerpp;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

/**
 * API location of the loom pattern registry.
 */
public final class LoomPatterns {
	/**
	 * The registry key for custom banner patterns, called Loom Patterns.
	 */
	@SuppressWarnings("unchecked")
	public static final RegistryKey<Registry<LoomPattern>> REGISTRY_KEY = (RegistryKey<Registry<LoomPattern>>) Bannerpp.LOOM_PATTERN_REGISTRY.getKey();
	/**
	 * The registry for custom banner patterns, called Loom Patterns.
	 */
	public static final Registry<LoomPattern> REGISTRY = Bannerpp.LOOM_PATTERN_REGISTRY;

	private LoomPatterns() {
	}
}
