package io.github.fablabsmc.fablabs.api.bannerpattern.v1;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

/**
 * The Banner++ equivalent of BannerPatternItem.
 */
public class LoomPatternItem extends Item {
	private final LoomPattern pattern;

	public LoomPatternItem(LoomPattern pattern, Item.Settings settings) {
		super(settings);
		this.pattern = checkNotNull(pattern);
	}

	public final LoomPattern getPattern() {
		return pattern;
	}

	public MutableText getDescription() {
		return new TranslatableText(this.getTranslationKey() + ".desc");
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> lines, TooltipContext ctx) {
		lines.add(getDescription().formatted(Formatting.GRAY));
	}
}
