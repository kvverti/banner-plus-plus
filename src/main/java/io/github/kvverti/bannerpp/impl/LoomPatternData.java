package io.github.kvverti.bannerpp.impl;

import io.github.kvverti.bannerpp.api.LoomPattern;

import net.minecraft.util.DyeColor;

/**
 * Class to store loom pattern data in the banner - its pattern,
 * color, and the index (in the vanilla banner pattern list) that
 * this pattern appears before. This allows Banner++ loom patterns
 * to be used with vanilla banner patterns.
 */
public final class LoomPatternData {

    public final LoomPattern pattern;
    public final DyeColor color;
    public final int index;

    public LoomPatternData(LoomPattern pattern, DyeColor color, int index) {
        if(index < 0) {
            throw new IllegalArgumentException("index: " + index);
        }
        this.pattern = pattern;
        this.color = color;
        this.index = index;
    }
}
