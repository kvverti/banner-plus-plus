package io.github.kvverti.bannerpp.mixin;

import io.github.kvverti.bannerpp.Bannerpp;
import io.github.kvverti.bannerpp.LoomPattern;
import io.github.kvverti.bannerpp.iface.LoomPatternContainer;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.container.Container;
import net.minecraft.container.LoomContainer;
import net.minecraft.container.Property;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.DyeColor;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LoomContainer.class)
public abstract class LoomContainerMixin extends Container {

    @Shadow @Final private Property selectedPattern;
    @Shadow @Final private Slot bannerSlot;
    @Shadow @Final private Slot dyeSlot;
    @Shadow @Final private Slot patternSlot;
    @Shadow @Final private Slot outputSlot;

    private LoomContainerMixin() {
        super(null, 0);
    }

    @Shadow private native void updateOutputSlot();

    /**
     * When the player clicks on a square that contains a loom pattern,
     * store the negative of the index clicked. This number is
     * -(loomPatternIndex + 1 + BannerPattern.LOOM_APPLICABLE_COUNT).
     */
    @Inject(method = "onButtonClick", at = @At("HEAD"), cancellable = true)
    private void selectBppLoomPatternOnClick(PlayerEntity entity, int clicked, CallbackInfoReturnable<Boolean> info) {
        if(clicked > BannerPattern.LOOM_APPLICABLE_COUNT) {
            selectedPattern.set(-clicked);
            this.updateOutputSlot();
            info.setReturnValue(true);
        }
    }

    /**
     * Trigger the then branch if the selected pattern is a Banner++ loom pattern.
     * We make the condition `selectedPattern.get() < BannerPattern.COUNT - 5`
     * true in this case.
     */
    @Redirect(
        method = "onContentChanged",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/container/Property;get()I"
        )
    )
    private int addBppLoomPatternCondition(Property self) {
        int res = self.get();
        if(res < 0) {
            res = 1;
        }
        return res;
    }

    // private void updateBppContentChanged(CallbackInfo info) {
    //    ItemStack banner = this.bannerSlot.getStack();
    //    ItemStack dyeStack = this.dyeSlot.getStack();
    //    ItemStack pattern = this.patternSlot.getStack();
    //    ItemStack output = this.outputSlot.getStack();
    //    if (output.isEmpty() || (!banner.isEmpty() && !dyeStack.isEmpty()) && (this.selectedPattern.get() < 0 || !pattern.isEmpty())) {
    //       if (!pattern.isEmpty() && pattern.getItem() instanceof ...) {
    //          CompoundTag beTag = banner.getOrCreateSubTag("BlockEntityTag");
    //          boolean overfull = BannerBlockEntity.getPatternCount(banner) >= 6;
    //          if (!overfull) {
    //             this.selectedPattern.set(...);
    //             this.updateOutputSlot();
    //             this.sendContentUpdates();
    //             info.cancel();
    //          }
    //       }
    //    }
    // }

    /**
     * When the output slot is updated, add the loom pattern to the
     * output banner.
     */
    @Inject(method = "updateOutputSlot", at = @At("HEAD"))
    private void addBppLoomPatternToOutput(CallbackInfo info) {
        ItemStack bannerStack = this.bannerSlot.getStack();
        ItemStack dyeStack = this.dyeSlot.getStack();
        if(this.selectedPattern.get() < 0 && !bannerStack.isEmpty() && !dyeStack.isEmpty()) {
            int rawId = -this.selectedPattern.get() - (1 + BannerPattern.LOOM_APPLICABLE_COUNT);
            if(rawId < Bannerpp.dyeLoomPatternCount()) {
                LoomPattern pattern = Bannerpp.byLoomIndex(rawId);
                DyeColor color = ((DyeItem)dyeStack.getItem()).getColor();
                ItemStack output = bannerStack.copy();
                output.setCount(1);
                CompoundTag beTag = output.getOrCreateSubTag("BlockEntityTag");
                ListTag loomPatterns;
                if(beTag.contains(LoomPatternContainer.NBT_KEY, 9)) {
                    loomPatterns = beTag.getList(LoomPatternContainer.NBT_KEY, 10);
                } else {
                    loomPatterns = new ListTag();
                    beTag.put(LoomPatternContainer.NBT_KEY, loomPatterns);
                }
                int vanillaPatternCount = beTag.getList("Patterns", 10).size();
                CompoundTag patternTag = new CompoundTag();
                patternTag.putString("Pattern", Bannerpp.LOOM_PATTERN_REGISTRY.getId(pattern).toString());
                patternTag.putInt("Color", color.getId());
                patternTag.putInt("Index", vanillaPatternCount);
                loomPatterns.add(patternTag);
                if(!ItemStack.areEqualIgnoreDamage(output, this.outputSlot.getStack())) {
                    this.outputSlot.setStack(output);
                }
            }
        }
    }
}
