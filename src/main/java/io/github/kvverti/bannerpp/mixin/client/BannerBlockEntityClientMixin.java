package io.github.kvverti.bannerpp.mixin.client;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.item.ItemStack;
import io.github.kvverti.bannerpp.LoomPattern;
import io.github.kvverti.bannerpp.Bannerpp;
import io.github.kvverti.bannerpp.LoomPatternData;
import io.github.kvverti.bannerpp.iface.LoomPatternContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BannerBlockEntity.class)
public abstract class BannerBlockEntityClientMixin extends BlockEntity implements LoomPatternContainer {

    // vanilla banner fields
    @Shadow private List<BannerPattern> patterns;
    @Shadow private String patternCacheKey;

    @Unique
    public List<LoomPatternData> loomPatterns = new ArrayList<>();

    @Unique
    private int nextLoomPatternIndex;

    private BannerBlockEntityClientMixin() {
        super(null);
    }

    @Override
    public List<LoomPatternData> bannerpp_getLoomPatterns() {
        return Collections.unmodifiableList(loomPatterns);
    }

    /**
     * Reads Banner++ loom pattern data from an item stack.
     */
    @Inject(method = "readFrom", at = @At("RETURN"))
    private void bppReadPatternFromItemStack(ItemStack stack, DyeColor color, CallbackInfo info) {
        CompoundTag tag = stack.getSubTag("BlockEntityTag");
        if(tag != null && tag.contains(NBT_KEY, 9)) {
            ((Internal)this).bannerpp_setLoomPatternTag(tag.getList(NBT_KEY, 10));
        } else {
            ((Internal)this).bannerpp_setLoomPatternTag(null);
        }
    }

    /**
     * Adds Banner++ loom pattern data to the pick block stack.
     */
    @Inject(method = "getPickStack", at = @At("RETURN"))
    private void putBppPatternsInPickStack(CallbackInfoReturnable<ItemStack> info) {
        ItemStack stack = info.getReturnValue();
        ListTag tag = ((Internal)this).bannerpp_getLoomPatternTag();
        if(tag != null) {
            stack.getOrCreateSubTag("BlockEntityTag")
                .put(NBT_KEY, tag);
        }
    }

    /**
     * Reads in Banner++ loom pattern data and resets current loom pattern index.
     */
    @Inject(
        method = "readPattern",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
            ordinal = 0,
            remap = false
        )
    )
    private void bppReadPattern(CallbackInfo info) {
        readLoomPatternTag();
        nextLoomPatternIndex = 0;
    }

    /**
     * Add Banner++ loom patterns to the banner pattern cache key.
     */
    @Inject(
        method = "readPattern",
        at = @At(value = "JUMP", ordinal = 0, shift = At.Shift.AFTER),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/nbt/ListTag;size()I"
            )
        )
    )
    private void parseBppPatternDataInline(CallbackInfo info) {
        int nextIndex = this.patterns.size() - 1;
        while(nextLoomPatternIndex < loomPatterns.size()) {
            LoomPatternData data = loomPatterns.get(nextLoomPatternIndex);
            if(data.index == nextIndex) {
                this.patternCacheKey += partialCacheKey(data);
                nextLoomPatternIndex++;
            } else {
                break;
            }
        }
    }

    /**
     * Add Banner++ loom patterns that occur after all regular banner patterns.
     */
    @Inject(method = "readPattern", at = @At("TAIL"))
    private void parseBppPatternDataPost(CallbackInfo info) {
        for(int i = nextLoomPatternIndex; i < loomPatterns.size(); i++) {
            this.patternCacheKey += partialCacheKey(loomPatterns.get(i));
        }
    }

    private String partialCacheKey(LoomPatternData data) {
        return "#" + Bannerpp.LOOM_PATTERN_REGISTRY.getRawId(data.pattern) + "-" + data.color.getId();
    }

    @Unique
    private ListTag writeLoomPatternTag() {
        ListTag patterns = new ListTag();
        for(LoomPatternData data : loomPatterns) {
            CompoundTag patternTag = new CompoundTag();
            patternTag.putString("Pattern", Bannerpp.LOOM_PATTERN_REGISTRY.getId(data.pattern).toString());
            patternTag.putInt("Color", data.color.getId());
            patternTag.putInt("Index", data.index);
        }
        return patterns;
    }

    @Unique
    private void readLoomPatternTag() {
        loomPatterns.clear();
        ListTag tag = ((LoomPatternContainer.Internal)this).bannerpp_getLoomPatternTag();
        if(tag != null) {
            for(Tag t : tag) {
                CompoundTag patternTag = (CompoundTag)t;
                LoomPattern pattern = Bannerpp.LOOM_PATTERN_REGISTRY.get(new Identifier(patternTag.getString("Pattern")));
                DyeColor color = DyeColor.byId(patternTag.getInt("Color"));
                int index = patternTag.getInt("Index");
                loomPatterns.add(new LoomPatternData(pattern, color, index));
            }
        }
    }
}
