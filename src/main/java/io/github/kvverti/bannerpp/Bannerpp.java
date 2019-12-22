package io.github.kvverti.bannerpp;

import io.github.kvverti.bannerpp.api.LoomPattern;
import io.github.kvverti.bannerpp.api.LoomPatternItem;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryIdRemapCallback;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.SimpleRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Bannerpp implements ModInitializer {

    @SuppressWarnings("unused")
    private static final Logger log = LogManager.getLogger(Bannerpp.class);

    // metadata
    public static final String MODID = "bannerpp";

    // LoomPattern registry
    public static final MutableRegistry<LoomPattern> LOOM_PATTERN_REGISTRY = new SimpleRegistry<>();

    private static final List<LoomPattern> nonSpecialPatterns = new ArrayList<>();
    private static final List<LoomPattern> specialPatterns = new ArrayList<>();

    /**
     * Called every time the registries are synced to rebuild the loom
     * index table. Loom pattern indices are based on the order in which
     * loom patterns appear in the registry, not on the raw IDs themselves,
     * hence why we don't use the remap context.
     */
    private static void remapLoomIndices() {
        nonSpecialPatterns.clear();
        specialPatterns.clear();
        for(LoomPattern p : LOOM_PATTERN_REGISTRY) {
            if(!p.isSpecial()) {
                nonSpecialPatterns.add(p);
            } else {
                specialPatterns.add(p);
            }
        }
    }

    public static int getLoomIndex(LoomPattern pattern) {
        if(pattern.isSpecial()) {
            return specialPatterns.indexOf(pattern) + nonSpecialPatterns.size();
        } else {
            return nonSpecialPatterns.indexOf(pattern);
        }
    }

    public static LoomPattern byLoomIndex(int loomIndex) {
        if(loomIndex < nonSpecialPatterns.size()) {
            return nonSpecialPatterns.get(loomIndex);
        } else {
            return specialPatterns.get(loomIndex - nonSpecialPatterns.size());
        }
    }

    public static int dyeLoomPatternCount() {
        return nonSpecialPatterns.size();
    }

    public static int totalLoomPatternCount() {
        return nonSpecialPatterns.size() + specialPatterns.size();
    }

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
        registerPattern("pig", true);
        registerPattern("bee", true);

        RegistryIdRemapCallback.event(LOOM_PATTERN_REGISTRY).register(state -> remapLoomIndices());
    }

    private void registerPattern(String name) {
        registerPattern(name, false);
    }

    // custom item settings
    private static final Item.Settings itemSettings = new Item.Settings().maxCount(1).group(ItemGroup.MISC);

    private void registerPattern(String name, boolean special) {
        LoomPattern pattern = new LoomPattern(special);
        Registry.register(LOOM_PATTERN_REGISTRY, new Identifier(MODID, name), pattern);
        if(special) {
            Registry.register(Registry.ITEM, new Identifier(MODID, name + "_banner_pattern"), new LoomPatternItem(pattern, itemSettings));
        }
    }
}
