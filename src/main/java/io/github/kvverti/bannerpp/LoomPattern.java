package io.github.kvverti.bannerpp;

import net.minecraft.util.Identifier;
import net.minecraft.item.Items;
import net.minecraft.item.Item;

/**
 * An extensible version of BannerPattern. Instances are referenced
 * by their registry ID.
 */
public class LoomPattern {

    private final Item specialItem;

    public LoomPattern(Item item) {
        this.specialItem = item;
    }

    public LoomPattern() {
        this(Items.AIR);
    }

    /**
     * Whether this loom pattern requres an item in the pattern slot.
     */
    public boolean isSpecial() {
        return specialItem != Items.AIR;
    }

    /**
     * Returns the sprite ID for the pattern mask texture for this LoomPattern.
     */
    public static Identifier getSpriteId(Identifier patternId, String namespace) {
        return new Identifier(patternId.getNamespace(), "pattern/" + namespace + "/" + patternId.getPath());
    }
}
