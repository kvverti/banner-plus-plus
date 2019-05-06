package io.github.kvverti.bannerpp;

import com.chocohead.mm.api.ClassTinkerers;

import io.github.kvverti.bannerpp.api.LoomPattern;

import net.fabricmc.api.ModInitializer;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.item.BannerPatternItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bannerpp implements ModInitializer {

	@SuppressWarnings("unused")
	private static final Logger log = LogManager.getLogger(Bannerpp.class);

	// metadata
	public static final String MODID = "bannerpp";

	// custom banner patterns
	public static final BannerPattern PIG = ClassTinkerers.getEnum(BannerPattern.class, "BANNERPP_PIG");

	// custom items
	public static final Item PIG_BANNER_PATTERN = new BannerPatternItem(PIG, new Item.Settings().itemGroup(ItemGroup.MISC));

	@Override
	public void onInitialize() {
		// Register items
		Registry.register(Registry.ITEM, new Identifier(MODID, "pig_banner_pattern"), PIG_BANNER_PATTERN);
		// log patterns
		log.info("Loom pattern item required:");
		for(BannerPattern p : BannerPattern.values()) {
			log.info(p.getName() + ": " + ((LoomPattern)(Object)p).requiresPatternItem());
		}
	}
}
