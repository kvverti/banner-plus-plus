package io.github.kvverti.bannerpp.mixin.client;

import io.github.kvverti.bannerpp.LoomPatternData;
import io.github.kvverti.bannerpp.api.LoomPatterns;
import io.github.kvverti.bannerpp.iface.LoomPatternContainer;
import io.github.kvverti.bannerpp.iface.LoomPatternConversions;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.DyeColor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BannerBlockEntity.class)
public abstract class BannerBlockEntityClientMixin extends BlockEntity implements LoomPatternContainer {

    @Shadow private List<?> patterns;
    @Shadow private boolean patternListTagRead;

    @Unique
    private List<LoomPatternData> loomPatterns = Collections.emptyList();

    private BannerBlockEntityClientMixin() {
        super(null);
    }

    @Override
    public List<LoomPatternData> bannerpp_getLoomPatterns() {
        if(this.patterns == null && this.patternListTagRead) {
            ListTag tag = ((LoomPatternContainer.Internal)this).bannerpp_getLoomPatternTag();
            loomPatterns = LoomPatternConversions.makeLoomPatternData(tag);
        }
        return Collections.unmodifiableList(loomPatterns);
    }

    /**
     * Reads Banner++ loom pattern data from an item stack.
     */
    @Inject(method = "readFrom", at = @At("RETURN"))
    private void bppReadPatternFromItemStack(ItemStack stack, DyeColor color, CallbackInfo info) {
        ((Internal)this).bannerpp_setLoomPatternTag(LoomPatternConversions.getLoomPatternTag(stack));
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

    @Unique
    private ListTag writeLoomPatternTag() {
        ListTag patterns = new ListTag();
        for(LoomPatternData data : loomPatterns) {
            CompoundTag patternTag = new CompoundTag();
            patternTag.putString("Pattern", LoomPatterns.REGISTRY.getId(data.pattern).toString());
            patternTag.putInt("Color", data.color.getId());
            patternTag.putInt("Index", data.index);
        }
        return patterns;
    }
}
