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
	 * Whether this loom pattern requires an item in the pattern slot.
	 */
	public boolean isSpecial() {
		return special;
	}

	/**
	 * @param type Context where this pattern is applied, e.g. <code>shield</code> or <code>banner</code>.
	 * @return the sprite ID for the pattern mask texture for this LoomPattern.
	 * @throws NullPointerException if this {@code LoomPattern} has not yet been registered.
	 */
	public Identifier getSpriteId(String type) {
		Identifier myId = LoomPatterns.REGISTRY.getId(this);
		return new Identifier(myId.getNamespace(), "pattern/" + type + "/" + myId.getPath());
	}

	/**
	 * @deprecated Acquire a {@link LoomPattern} instance and call {@link #getSpriteId(String)} directly.
	 * @throws NullPointerException if {@code patternId} does not refer to a registered LoomPattern.
	 */
	@Deprecated
	public static Identifier getSpriteId(Identifier patternId, String namespace) {
		return LoomPatterns.REGISTRY.get(patternId).getSpriteId(namespace);
	}
}
