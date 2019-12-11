package io.github.kvverti.bannerpp.mixin;

import io.github.kvverti.bannerpp.iface.LoomPatternContainer;

import java.util.List;

import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BannerItem.class)
public abstract class BannerItemMixin extends WallStandingBlockItem {

    private BannerItemMixin() {
        super(null, null, null);
    }

    @Unique
    private static ListTag loomPatterns;

    @Unique
    private static int nextLoomPatternIndex;

    /**
     * Reads in Banner++ loom pattern data and resets current loom pattern index.
     */
    @Inject(
        method = "appendBannerTooltip",
        at = @At("HEAD")
    )
    private static void preAppendBppLoomPatterns(ItemStack stack, List<Text> lines, CallbackInfo info) {
        nextLoomPatternIndex = 0;
        CompoundTag beTag = stack.getSubTag("BlockEntityTag");
        if (beTag != null && beTag.contains(LoomPatternContainer.NBT_KEY)) {
            loomPatterns = beTag.getList(LoomPatternContainer.NBT_KEY, 10);
        }
    }

    /**
     * Add Banner++ loom patterns to the tooltip.
     */
    @Inject(
        method = "appendBannerTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/nbt/ListTag;getCompound(I)Lnet/minecraft/nbt/CompoundTag;",
            ordinal = 0
        )
    )
    private static void appendBppLoomPatternsInline(ItemStack stack, List<Text> lines, CallbackInfo info) {
        int nextIndex = lines.size() - 1;
        while(nextLoomPatternIndex < loomPatterns.size()) {
            CompoundTag data = loomPatterns.getCompound(nextLoomPatternIndex);
            if(data.getInt("Index") == nextIndex) {
                addLoomPatternLine(data, lines);
                nextLoomPatternIndex++;
            } else {
                break;
            }
        }
    }

    /**
     * Add Banner++ loom patterns that occur after all regular banner patterns
     * in the tooltip (this also covers the case where no vanilla banner patterns
     * are present).
     */
    @Inject(method = "appendBannerTooltip", at = @At("RETURN"))
    private static void appendBppLoomPatternsPost(ItemStack stack, List<Text> lines, CallbackInfo info) {
        if(loomPatterns != null) {
            for(int i = nextLoomPatternIndex; i < loomPatterns.size(); i++) {
                CompoundTag data = loomPatterns.getCompound(i);
                addLoomPatternLine(data, lines);
            }
            // allow NBT tag to be garbage collected
            loomPatterns = null;
        }
    }

    @Unique
    private static void addLoomPatternLine(CompoundTag data, List<Text> lines) {
        String id = data.getString("Pattern");
        int sepIdx = id.indexOf(':');
        String color = DyeColor.byId(data.getInt("Color")).getName();
        lines.add(new TranslatableText(
            "bannerpp.pattern." + id.substring(0, sepIdx) + "." + id.substring(sepIdx + 1) + "." + color)
            .formatted(Formatting.GRAY));
    }
}
