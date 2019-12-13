package io.github.kvverti.bannerpp;

import io.github.kvverti.bannerpp.LoomPattern;

import net.fabricmc.api.ModInitializer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.SimpleRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bannerpp implements ModInitializer {

    @SuppressWarnings("unused")
    private static final Logger log = LogManager.getLogger(Bannerpp.class);

    // metadata
    public static final String MODID = "bannerpp";

    // custom items
    public static final Item PIG_BANNER_PATTERN = new Item(new Item.Settings().maxCount(1).group(ItemGroup.MISC));
    public static final Item BEE_BANNER_PATTERN = new Item(new Item.Settings().maxCount(1).group(ItemGroup.MISC));

    // LoomPattern registry
    public static final MutableRegistry<LoomPattern> LOOM_PATTERN_REGISTRY = new SimpleRegistry<>();

    @Override
    public void onInitialize() {
        Registry.register(Registry.REGISTRIES, new Identifier(MODID, "loom_patterns"), LOOM_PATTERN_REGISTRY);
        registerPattern("gradient_right");
        registerPattern("gradient_left");
        registerPattern("right_fifth_stripe");
        registerPattern("right_center_fifth_stripe");
        registerPattern("center_fifth_stripe");
        registerPattern("left_center_fifth_stripe");
        registerPattern("left_fifth_stripe");
        registerPattern("top_fifth_stripe");
        registerPattern("upper_middle_fifth_stripe");
        registerPattern("middle_fifth_stripe");
        registerPattern("lower_middle_fifth_stripe");
        registerPattern("bottom_fifth_stripe");
        registerPattern("right_quarter_stripe");
        registerPattern("right_center_quarter_stripe");
        registerPattern("left_center_quarter_stripe");
        registerPattern("left_quarter_stripe");
        registerPattern("top_quarter_stripe");
        registerPattern("upper_middle_quarter_stripe");
        registerPattern("lower_middle_quarter_stripe");
        registerPattern("bottom_quarter_stripe");
        registerPattern("pig", PIG_BANNER_PATTERN);
        registerPattern("bee", BEE_BANNER_PATTERN);

        registerItem("pig_banner_pattern", PIG_BANNER_PATTERN);
    }

    private void registerPattern(String name) {
        Registry.register(LOOM_PATTERN_REGISTRY, new Identifier(MODID, name), new LoomPattern());
    }

    private void registerPattern(String name, Item item) {
        Registry.register(LOOM_PATTERN_REGISTRY, new Identifier(MODID, name), new LoomPattern(item));
    }

    private void registerItem(String name, Item item) {
        Registry.register(Registry.ITEM, new Identifier(MODID, name), item);
    }
}
