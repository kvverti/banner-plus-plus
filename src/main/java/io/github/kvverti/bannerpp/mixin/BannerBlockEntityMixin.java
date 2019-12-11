package io.github.kvverti.bannerpp.mixin;

import io.github.kvverti.bannerpp.iface.LoomPatternContainer;

import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Adds loom pattern data fields to the banner block entity.
 * The actual pattern parsing is done client side in the banner's
 * client-only methods.
 */
@Mixin(BannerBlockEntity.class)
public abstract class BannerBlockEntityMixin extends BlockEntity implements LoomPatternContainer.Internal {

    @Unique
    public ListTag loomPatternsTag = new ListTag();

    private BannerBlockEntityMixin() {
        super(null);
    }

    @Override
    public ListTag bannerpp_getLoomPatternTag() {
        return loomPatternsTag;
    }

    @Override
    public void bannerpp_setLoomPatternTag(ListTag tag) {
        loomPatternsTag = tag;
    }

    /**
     * Add Banner++ patterns to the pattern count.
     */
    @Inject(method = "getPatternCount", at = @At("RETURN"), cancellable = true)
    private static void modifyPatternCount(ItemStack stack, CallbackInfoReturnable<Integer> info) {
        CompoundTag beTag = stack.getSubTag("BlockEntityTag");
        if(beTag.contains(LoomPatternContainer.NBT_KEY)) {
            int count = beTag.getList(LoomPatternContainer.NBT_KEY, 10).size();
            info.setReturnValue(info.getReturnValueI() + count);
        }
    }

    /**
     * Write Banner++ data to tag.
     */
    @Inject(method = "toTag", at = @At("RETURN"))
    private void addBppPatternData(CallbackInfoReturnable<CompoundTag> info) {
        CompoundTag tag = info.getReturnValue();
        tag.put(LoomPatternContainer.NBT_KEY, loomPatternsTag);
    }

    /**
     * Read Banner++ data from tag.
     */
    @Inject(method = "fromTag", at = @At("RETURN"))
    private void readBppPatternData(CompoundTag tag, CallbackInfo info) {
        loomPatternsTag = tag.getList(LoomPatternContainer.NBT_KEY, 10);
    }
}
