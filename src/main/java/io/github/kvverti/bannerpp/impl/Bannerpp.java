package io.github.kvverti.bannerpp.impl;

import io.github.kvverti.bannerpp.api.LoomPatterns;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryIdRemapCallback;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class Bannerpp implements ModInitializer {

    // metadata
    public static final String MODID = "bannerpp";

    @Override
    public void onInitialize() {
        Registry.register(Registry.REGISTRIES, new Identifier(MODID, "loom_patterns"), LoomPatterns.REGISTRY);
        RegistryIdRemapCallback.event(LoomPatterns.REGISTRY).register(state -> LoomPatterns.remapLoomIndices());
    }
}
