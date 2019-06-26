package io.github.kvverti.bannerpp;

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
    public static final BannerPattern PIG = BannerPattern.valueOf("BANNERPP_PIG");

    // custom items
    public static final Item PIG_BANNER_PATTERN = new BannerPatternItem(PIG, new Item.Settings().maxCount(1).group(ItemGroup.MISC));

    @Override
    public void onInitialize() {
        // Register items
        Registry.register(Registry.ITEM, new Identifier(MODID, "pig_banner_pattern"), PIG_BANNER_PATTERN);
    }
}
