package io.github.fablabsmc.fablabs.api.bannerpattern.v1;

import com.mojang.serialization.Lifecycle;
import io.github.fablabsmc.fablabs.impl.bannerpattern.Bannerpp;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

/**
 * API location of the loom pattern registry.
 */
public final class LoomPatterns {
	/**
	 * The registry key for custom banner patterns, called Loom Patterns.
	 */
	public static final RegistryKey<Registry<LoomPattern>> REGISTRY_KEY = RegistryKey.ofRegistry(new Identifier(Bannerpp.MODID, "loom_patterns"));
	/**
	 * The registry for custom banner patterns, called Loom Patterns.
	 */
	public static final Registry<LoomPattern> REGISTRY = new SimpleRegistry<>(REGISTRY_KEY, Lifecycle.stable());

	private LoomPatterns() {
	}
}
