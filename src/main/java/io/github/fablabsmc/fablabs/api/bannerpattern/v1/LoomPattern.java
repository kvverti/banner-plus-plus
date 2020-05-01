package io.github.fablabsmc.fablabs.api.bannerpattern.v1;

import net.minecraft.util.Identifier;

/**
 * An extensible version of BannerPattern. Instances are referenced
 * by their registry ID.
 */
public class LoomPattern {
	private final boolean special;

	public LoomPattern(boolean special) {
		this.special = special;
	}

	/**
	 * Whether this loom pattern requres an item in the pattern slot.
	 */
	public boolean isSpecial() {
		return special;
	}

	/**
	 * Returns the sprite ID for the pattern mask texture for this LoomPattern.
	 */
	public static Identifier getSpriteId(Identifier patternId, String namespace) {
		return new Identifier(patternId.getNamespace(), "pattern/" + namespace + "/" + patternId.getPath());
	}
}
