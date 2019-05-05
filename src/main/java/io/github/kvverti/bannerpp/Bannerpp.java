package io.github.kvverti.bannerpp;

import com.chocohead.mm.api.ClassTinkerers;

import net.fabricmc.api.ModInitializer;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bannerpp implements ModInitializer {

	private static final Logger log = LogManager.getLogger(Bannerpp.class);

	public static final String MODID = "bannerpp";

	public static final Item PIG_BANNER_PATTERN = new Item(new Item.Settings().itemGroup(ItemGroup.MISC));

	@Override
	public void onInitialize() {
		// Register items
		Registry.register(Registry.ITEM, new Identifier(MODID, "pig_banner_pattern"), PIG_BANNER_PATTERN);

		// ensure that custom banner patterns exist
		ClassTinkerers.getEnum(BannerPattern.class, "BANNERPP_PIG");
		log.info("Custom banner patterns exist");
	}
}
