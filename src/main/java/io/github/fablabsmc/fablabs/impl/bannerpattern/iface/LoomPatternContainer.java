package io.github.fablabsmc.fablabs.impl.bannerpattern.iface;

import java.util.List;

import io.github.fablabsmc.fablabs.impl.bannerpattern.LoomPatternData;

import net.minecraft.nbt.ListTag;

public interface LoomPatternContainer {
	String NBT_KEY = "Bannerpp_LoomPatterns";

	List<LoomPatternData> bannerpp_getLoomPatterns();

	/**
	 * Internal interface that allows the client mixin to communicate
	 * with the common mixin.
	 */
	interface Internal {
		ListTag bannerpp_getLoomPatternTag();

		void bannerpp_setLoomPatternTag(ListTag tag);
	}
}
