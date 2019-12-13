package io.github.kvverti.bannerpp.mixin;

import io.github.kvverti.bannerpp.Bannerpp;
import io.github.kvverti.bannerpp.iface.LoomPatternContainer;

import java.util.Iterator;

import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static java.util.Comparator.comparingInt;

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
     * Handles removing Banner++ loom patterns instead of vanilla loom patterns
     * when a banner is cleaned in a cauldron. Yes, this is an "inject-and-cancel"
     * callback. Let me know if there are incompatibilities.
     */
    @Inject(method = "loadFromItemStack", at = @At("HEAD"), cancellable = true)
    private static void cleanBppLoomPattern(ItemStack stack, CallbackInfo info) {
        CompoundTag beTag = stack.getSubTag("BlockEntityTag");
        if(beTag != null) {
            ListTag loomPatterns = beTag.getList(LoomPatternContainer.NBT_KEY, 10);
            ListTag patterns = beTag.getList("Patterns", 10);
            boolean cleaned = false;
            if(!loomPatterns.isEmpty()) {
                // determine if the last loom pattern is the topmost
                int lastIndex = loomPatterns.getCompound(loomPatterns.size() - 1).getInt("Index");
                if(lastIndex >= patterns.size()) {
                    loomPatterns.remove(loomPatterns.size() - 1);
                    cleaned = true;
                }
            }
            if(!cleaned && !patterns.isEmpty()) {
                patterns.remove(patterns.size() - 1);
            }
            if(loomPatterns.isEmpty()) {
                if(patterns.isEmpty()) {
                    stack.removeSubTag("BlockEntityTag");
                } else {
                    beTag.remove(LoomPatternContainer.NBT_KEY);
                }
            } else if(patterns.isEmpty()) {
                beTag.remove("Patterns");
            }
        }
        info.cancel();
    }

    /**
     * Write Banner++ data to tag.
     */
    @Inject(method = "toTag", at = @At("RETURN"))
    private void addBppPatternData(CallbackInfoReturnable<CompoundTag> info) {
        CompoundTag tag = info.getReturnValue();
        if(tag != null) {
            tag.put(LoomPatternContainer.NBT_KEY, loomPatternsTag);
        }
    }

    /**
     * Read Banner++ data from tag.
     */
    @Inject(method = "fromTag", at = @At("RETURN"))
    private void readBppPatternData(CompoundTag tag, CallbackInfo info) {
        loomPatternsTag = tag.getList(LoomPatternContainer.NBT_KEY, 10);
        // validate NBT data, removing and/or resetting invalid data
        for(Iterator<Tag> itr = loomPatternsTag.iterator(); itr.hasNext(); ) {
            CompoundTag element = (CompoundTag)itr.next();
            Identifier id = Identifier.tryParse(element.getString("Pattern"));
            int colorId = element.getInt("Color");
            int index = element.getInt("Index");
            if(id == null || !Bannerpp.LOOM_PATTERN_REGISTRY.containsId(id)) {
                itr.remove();
            } else {
                int rtColorId = DyeColor.byId(colorId).getId();
                if(rtColorId != colorId) {
                    element.putInt("Color", rtColorId);
                }
                if(index < 0) {
                    element.putInt("Index", 0);
                }
            }
        }
        // the Java API requires that this sort be stable
        loomPatternsTag.sort(comparingInt(t -> ((CompoundTag)t).getInt("Index")));
    }
}
