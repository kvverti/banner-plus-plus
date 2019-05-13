package io.github.kvverti.bannerpp.mixin.client;

import io.github.kvverti.bannerpp.api.LoomPattern;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.client.gui.container.LoomScreen;
import net.minecraft.container.LoomContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.TextComponent;
import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LoomScreen.class)
public abstract class MixinLoomScreen extends ContainerScreen<LoomContainer> {

    private MixinLoomScreen(LoomContainer lc, PlayerInventory pi, TextComponent tc) {
        super(lc, pi, tc);
    }

    // shadow members

    @Shadow private boolean canApplyDyePattern;
    @Shadow private int firstPatternButtonId;

    /**
     * We reinterpret this.firstPatternButtonId as a 1-indexed field
     * into LoomPattern.RECIPE_PATTERNS for the dye pattern section
     * of LoomScreen#drawBackground.
     */
    @Redirect(
        method = "drawBackground",
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/client/gui/container/LoomScreen;canApplyDyePattern:Z",
                ordinal = 1
            )
        ),
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/gui/container/LoomScreen;firstPatternButtonId:I"
        )
    )
    private int offsetFirstPatternButtonId(LoomScreen self) {
        // self == this
        return this.firstPatternButtonId - 1;
    }

    /**
     * Get the proper bound on dye banner patterns by redirecting the
     * `this.patternButtonTextureIds.length` call.
     */
    @Redirect(
        method = "drawBackground",
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/client/gui/container/LoomScreen;canApplyDyePattern:Z",
                ordinal = 1
            )
        ),
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/gui/container/LoomScreen;patternButtonTextureIds:[Lnet/minecraft/util/Identifier;",
            args = "array=length"
        )
    )
    private int getProperDyePatternBound(Identifier[] patternButtonTextureIds) {
        return LoomPattern.RECIPE_PATTERNS.size();
    }

    /**
     * Change the dye pattern cutoff constant to zero, since the functionality
     * is already performed in getProperDyePatternBound.
     */
    @ModifyConstant(
        method = "drawBackground",
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/client/gui/container/LoomScreen;canApplyDyePattern:Z",
                ordinal = 1
            )
        ),
        constant = @Constant(intValue = 5, ordinal = 0)
    )
    private int removeHardcodedDyePatternCutoff(int cutoff) {
        return 0;
    }

    private static final BannerPattern[] bannerpp_patterns = BannerPattern.values();

    /**
     * When comparing the pattern index to the selected pattern, map the
     * selected pattern ID to the corresponding index.
     */
    @Redirect(
        method = "drawBackground",
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/client/gui/container/LoomScreen;canApplyDyePattern:Z",
                ordinal = 1
            )
        ),
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/container/LoomContainer;getSelectedPattern()I",
            ordinal = 0
        )
    )
    private int mapSelectedPatternId(LoomContainer self) {
        int patternId = self.getSelectedPattern();
        return LoomPattern.RECIPE_PATTERNS.indexOf(bannerpp_patterns[patternId]);
    }

    /**
     * Map dye pattern indices to a BannerPattern ID for indexing into
     * this.patternButtonTextureIds.
     */
    @Redirect(
        method = "drawBackground",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/gui/container/LoomScreen;patternButtonTextureIds:[Lnet/minecraft/util/Identifier;",
            args = "array=get"
        )
    )
    private Identifier turnDyePatternIdxToPatternId(Identifier[] patternButtonTextureIds, int idx) {
        if(this.canApplyDyePattern) {
            idx = LoomPattern.RECIPE_PATTERNS.get(idx).ordinal();
        }
        return patternButtonTextureIds[idx];
    }
}
