package io.github.kvverti.bannerpp;

import io.github.kvverti.bannerpp.api.LoomPatterns;
import io.github.kvverti.bannerpp.api.LoomPattern;
import io.github.kvverti.bannerpp.api.LoomPatternItem;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryIdRemapCallback;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class Bannerpp implements ModInitializer {

    // metadata
    public static final String MODID = "bannerpp";

    @Override
    public void onInitialize() {
        Registry.register(Registry.REGISTRIES, new Identifier(MODID, "loom_patterns"), LoomPatterns.REGISTRY);
        RegistryIdRemapCallback.event(LoomPatterns.REGISTRY).register(state -> LoomPatterns.remapLoomIndices());

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
        registerPattern("pig", true);
        registerPattern("bee", true);
    }

    private void registerPattern(String name) {
        registerPattern(name, false);
    }

    // custom item settings
    private static final Item.Settings itemSettings = new Item.Settings().maxCount(1).group(ItemGroup.MISC);

    private void registerPattern(String name, boolean special) {
        LoomPattern pattern = new LoomPattern(special);
        Registry.register(LoomPatterns.REGISTRY, new Identifier(MODID, name), pattern);
        if(special) {
            Registry.register(Registry.ITEM, new Identifier(MODID, name + "_banner_pattern"), new LoomPatternItem(pattern, itemSettings));
        }
    }
}
