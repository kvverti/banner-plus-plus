package io.github.kvverti.bannerpp.api;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;

import net.minecraft.block.entity.BannerPattern;

public interface LoomPattern {

    ImmutableList<BannerPattern> RECIPE_PATTERNS =
        ImmutableList.copyOf(Arrays.asList(BannerPattern.values()).stream()
            .filter(v -> !((LoomPattern)(Object)v).requiresPatternItem())
            .iterator());

    /**
     * Whether this loom pattern requres an item in the pattern slot.
     */
    boolean requiresPatternItem();
}
