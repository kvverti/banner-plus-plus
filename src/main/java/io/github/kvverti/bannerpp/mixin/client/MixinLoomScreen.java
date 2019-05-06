package io.github.kvverti.bannerpp.mixin.client;

import com.mojang.blaze3d.platform.GlStateManager;

import io.github.kvverti.bannerpp.api.LoomPattern;

import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.client.gui.container.LoomScreen;
import net.minecraft.container.LoomContainer;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.TextComponent;
import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LoomScreen.class)
public abstract class MixinLoomScreen extends ContainerScreen<LoomContainer> {

    private MixinLoomScreen(LoomContainer lc, PlayerInventory pi, TextComponent tc) {
        super(lc, pi, tc);
    }

    // shadow members

    @Shadow private static Identifier TEXTURE;
    @Shadow private Identifier output;
    @Shadow private Identifier[] patternButtonTextureIds;
    @Shadow private float scrollPosition;
    @Shadow private boolean hasTooManyPatterns;
    @Shadow private boolean canApplyDyePattern;
    @Shadow private boolean canApplySpecialPattern;
    @Shadow private int firstPatternButtonId;

    /**
     * Draws the pattern selection screen.
     */
    @Overwrite
    public void drawBackground(float float_1, int mouseX, int mouseY) {
        this.renderBackground();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        int left = this.left;
        int top = this.top;
        this.blit(left, top, 0, 0, this.containerWidth, this.containerHeight);
        Slot bannerSlot = ((LoomContainer)this.container).getBannerSlot();
        Slot dyeSlot = ((LoomContainer)this.container).getDyeSlot();
        Slot patternSlot = ((LoomContainer)this.container).getPatternSlot();
        Slot outputSlot = ((LoomContainer)this.container).getOutputSlot();
        if (!bannerSlot.hasStack()) {
            this.blit(left + bannerSlot.xPosition, top + bannerSlot.yPosition, this.containerWidth, 0, 16, 16);
        }

        if (!dyeSlot.hasStack()) {
            this.blit(left + dyeSlot.xPosition, top + dyeSlot.yPosition, this.containerWidth + 16, 0, 16, 16);
        }

        if (!patternSlot.hasStack()) {
            this.blit(left + patternSlot.xPosition, top + patternSlot.yPosition, this.containerWidth + 32, 0, 16, 16);
        }

        int int_5 = (int)(41.0F * this.scrollPosition);
        this.blit(left + 119, top + 13 + int_5, 232 + (this.canApplyDyePattern ? 0 : 12), 0, 12, 15);
        if (this.output != null && !this.hasTooManyPatterns) {
            this.minecraft.getTextureManager().bindTexture(this.output);
            blit(left + 141, top + 8, 20, 40, 1.0F, 1.0F, 20, 40, 64, 64);
        } else if (this.hasTooManyPatterns) {
            this.blit(left + outputSlot.xPosition - 2, top + outputSlot.yPosition - 2, this.containerWidth, 17, 17, 16);
        }

        int bgOffsetX;
        int bgOffsetY;
        int endPatternButtonId;
        if (this.canApplyDyePattern) {
            bgOffsetX = left + 60;
            bgOffsetY = top + 13;
            endPatternButtonId = (this.firstPatternButtonId - 1) + 16;
            // repurpose this.firstPatternButtonId to be a pattern index into
            // LoomPatterns.RECIPE_PATTERNS (1-indexed), and remove the hardcoded
            // assumption of (COUNT - 5) dye banner patterns
            int displayPatternCount = Math.min(endPatternButtonId, LoomPattern.RECIPE_PATTERNS.size());
            for(int patternIdx = this.firstPatternButtonId - 1; patternIdx < displayPatternCount; ++patternIdx) {
                // derive pattern ID from pattern index
                int patternId = LoomPattern.RECIPE_PATTERNS.get(patternIdx).ordinal();
                int colPixelPos = bgOffsetX + (patternIdx - this.firstPatternButtonId + 1) % 4 * 14;
                int rowPixelPos = bgOffsetY + (patternIdx - this.firstPatternButtonId + 1) / 4 * 14;
                this.minecraft.getTextureManager().bindTexture(TEXTURE);
                int textureOffsetY = this.containerHeight;
                if (patternId == ((LoomContainer)this.container).getSelectedPattern()) {
                    textureOffsetY += 14;
                } else if (mouseX >= colPixelPos && mouseY >= rowPixelPos && mouseX < colPixelPos + 14 && mouseY < rowPixelPos + 14) {
                    textureOffsetY += 28;
                }

                this.blit(colPixelPos, rowPixelPos, 0, textureOffsetY, 14, 14);
                if (this.patternButtonTextureIds[patternId] != null) {
                    this.minecraft.getTextureManager().bindTexture(this.patternButtonTextureIds[patternId]);
                    blit(colPixelPos + 4, rowPixelPos + 2, 5, 10, 1.0F, 1.0F, 20, 40, 64, 64);
                }
            }
        } else if (this.canApplySpecialPattern) {
            bgOffsetX = left + 60;
            bgOffsetY = top + 13;
            this.minecraft.getTextureManager().bindTexture(TEXTURE);
            this.blit(bgOffsetX, bgOffsetY, 0, this.containerHeight, 14, 14);
            endPatternButtonId = ((LoomContainer)this.container).getSelectedPattern();
            if (this.patternButtonTextureIds[endPatternButtonId] != null) {
                this.minecraft.getTextureManager().bindTexture(this.patternButtonTextureIds[endPatternButtonId]);
                blit(bgOffsetX + 4, bgOffsetY + 2, 5, 10, 1.0F, 1.0F, 20, 40, 64, 64);
            }
        }
    }
}
