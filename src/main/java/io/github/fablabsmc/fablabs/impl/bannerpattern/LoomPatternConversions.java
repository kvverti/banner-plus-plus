package io.github.fablabsmc.fablabs.impl.bannerpattern;

import java.util.ArrayList;
import java.util.List;

import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatterns;
import io.github.fablabsmc.fablabs.impl.bannerpattern.iface.LoomPatternContainer;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public final class LoomPatternConversions {
	private LoomPatternConversions() {
	}

	/**
	 * Extracts the loom pattern tag from the given ItemStack.
	 *
	 * @return the loom pattern tag, or null if it is not present.
	 */
	public static ListTag getLoomPatternTag(ItemStack stack) {
		CompoundTag tag = stack.getSubTag("BlockEntityTag");

		if (tag != null && tag.contains(LoomPatternContainer.NBT_KEY, 9)) {
			return tag.getList(LoomPatternContainer.NBT_KEY, 10);
		} else {
			return null;
		}
	}

	/**
	 * Parses the given NBT data into a list of LoomPatternData objects.
	 *
	 * @param tag a nullable NBT ListTag with loom pattern data
	 */
	public static List<LoomPatternData> makeLoomPatternData(ListTag tag) {
		List<LoomPatternData> res = new ArrayList<>();

		if (tag != null) {
			for (Tag t : tag) {
				CompoundTag patternTag = (CompoundTag) t;
				LoomPattern pattern = LoomPatterns.REGISTRY.get(new Identifier(patternTag.getString("Pattern")));
				DyeColor color = DyeColor.byId(patternTag.getInt("Color"));
				int index = patternTag.getInt("Index");
				res.add(new LoomPatternData(pattern, color, index));
			}
		}

		return res;
	}
}
