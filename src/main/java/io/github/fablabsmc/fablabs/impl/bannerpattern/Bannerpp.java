package io.github.fablabsmc.fablabs.impl.bannerpattern;

import java.util.Arrays;
import java.util.List;

import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.impl.bannerpattern.iface.LoomPatternContainer;

import net.minecraft.loot.function.CopyNbtLootFunction;
import net.minecraft.loot.provider.nbt.ContextLootNbtProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.event.registry.RegistryIdRemapCallback;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;

public final class Bannerpp implements ModInitializer {
	// metadata
	public static final String MODID = "bannerpp";

	public static final Registry<LoomPattern> LOOM_PATTERN_REGISTRY = FabricRegistryBuilder
			.createSimple(LoomPattern.class, new Identifier(MODID, "loom_patterns"))
			.attribute(RegistryAttribute.SYNCED)
			.buildAndRegister();

	private static final List<Identifier> BANNER_LOOT_TABLES = Arrays.asList(
			new Identifier("minecraft", "blocks/black_banner"),
			new Identifier("minecraft", "blocks/red_banner"),
			new Identifier("minecraft", "blocks/green_banner"),
			new Identifier("minecraft", "blocks/brown_banner"),
			new Identifier("minecraft", "blocks/blue_banner"),
			new Identifier("minecraft", "blocks/purple_banner"),
			new Identifier("minecraft", "blocks/cyan_banner"),
			new Identifier("minecraft", "blocks/light_gray_banner"),
			new Identifier("minecraft", "blocks/gray"),
			new Identifier("minecraft", "blocks/pink_banner"),
			new Identifier("minecraft", "blocks/lime_banner"),
			new Identifier("minecraft", "blocks/yellow_banner"),
			new Identifier("minecraft", "blocks/light_blue_banner"),
			new Identifier("minecraft", "blocks/magenta_banner"),
			new Identifier("minecraft", "blocks/orange_banner"),
			new Identifier("minecraft", "blocks/white_banner")
	);

	@Override
	public void onInitialize() {
		RegistryIdRemapCallback.event(LOOM_PATTERN_REGISTRY).register(state -> LoomPatternsInternal.remapLoomIndices());

		// registry sync is longer called on the server or in singleplayer, so we must set up the indices here using
		// a registry item added callback.
		for (LoomPattern p : LOOM_PATTERN_REGISTRY) {
			LoomPatternsInternal.addPattern(p);
		}

		RegistryEntryAddedCallback.event(LOOM_PATTERN_REGISTRY).register((raw, id, p) -> LoomPatternsInternal.addPattern(p));
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, lootTableId, supplier, setter) -> {
			if (BANNER_LOOT_TABLES.contains(lootTableId)) {
				supplier.withFunction(CopyNbtLootFunction
						.builder(ContextLootNbtProvider.BLOCK_ENTITY)
						.withOperation(LoomPatternContainer.NBT_KEY, "BlockEntityTag." + LoomPatternContainer.NBT_KEY)
						.build()
				);
			}
		});
	}
}
