package io.github.fablabsmc.fablabs.mixin.bannerpattern;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;

@Mixin(Registry.class)
public interface RegistryAccessor {
	@Accessor("ROOT")
	static MutableRegistry<MutableRegistry<?>> getRoot() {
		throw new AssertionError();
	}
}
