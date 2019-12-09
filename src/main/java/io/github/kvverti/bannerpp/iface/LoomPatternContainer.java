package io.github.kvverti.bannerpp.iface;

import io.github.kvverti.bannerpp.LoomPatternData;

import java.util.List;

import net.minecraft.nbt.ListTag;

public interface LoomPatternContainer {

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
