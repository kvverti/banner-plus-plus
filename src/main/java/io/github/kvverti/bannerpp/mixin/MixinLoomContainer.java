package io.github.kvverti.bannerpp.mixin;

import io.github.kvverti.bannerpp.api.LoomPattern;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.container.LoomContainer;
import net.minecraft.container.Property;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Overwrites LoomContainer methods to correctly determine dye banner patterms.
 */
@Mixin(LoomContainer.class)
public abstract class MixinLoomContainer extends Container {

    // unused
    private MixinLoomContainer(ContainerType<?> ct, int i) {
        super(ct, i);
    }

    // mixin members

    private static final BannerPattern[] patterns = BannerPattern.values();

    /**
     * Replace the default dye banner pattern cutoff with our own.
     */
    @Redirect(
        method = "onButtonClick",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/block/entity/BannerPattern;field_18283:I"
        )
    )
    private int getBannerPatternCutoff() {
        return LoomPattern.RECIPE_PATTERNS.size();
    }

    /**
     * Transform the dye banner pattern index into the actual ordinal
     * that Minecraft expects
     */
    @ModifyVariable(
        method = "onButtonClick",
        at = @At(value = "LOAD", ordinal = 2)
    )
    private int transformPatternIndex(int indexPlusOne) {
        return LoomPattern.RECIPE_PATTERNS.get(indexPlusOne - 1).ordinal();
    }

    /**
     * Replace the upper bound test for `this.selectedPattern.get()` with a test
     * for our mixed in interface. Because the method expects an int, we return 0
     * for true, and Integer.MAX_VALUE for false.
     */
    @Redirect(
        method = "onContentChanged",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/container/Property;get()I",
            ordinal = 1
        )
    )
    private int isActuallySpecialPattern(Property self) {
        boolean special = ((LoomPattern)(Object)patterns[self.get()]).bannerpp_isSpecial();
        return special ? Integer.MAX_VALUE : 0;
    }
}
