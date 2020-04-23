package io.github.kvverti.bannerpp.mixin.client;

import com.mojang.datafixers.util.Pair;

import io.github.kvverti.bannerpp.impl.LoomPatternData;
import io.github.kvverti.bannerpp.api.LoomPattern;
import io.github.kvverti.bannerpp.api.LoomPatterns;
import io.github.kvverti.bannerpp.impl.iface.LoomPatternContainer;
import io.github.kvverti.bannerpp.impl.LoomPatternRenderContext;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BannerBlockEntityRenderer.class)
public abstract class BannerBlockEntityRendererMixin extends BlockEntityRenderer<BannerBlockEntity> {

    private BannerBlockEntityRendererMixin() {
        super(null);
    }

    @Unique
    private static List<LoomPatternData> loomPatterns;

    @Unique
    private static int nextLoomPatternIndex;

    /**
     * Saves Banner++ loom pattens in a field for rendering.
     */
    @Inject(method = "render", at = @At("HEAD"))
    private void preBppPatternRender(BannerBlockEntity banner, float f1,
            MatrixStack stack, VertexConsumerProvider provider, int i, int j, CallbackInfo info) {
        LoomPatternRenderContext.setLoomPatterns(((LoomPatternContainer)banner).bannerpp_getLoomPatterns());
    }

    @Inject(method = "method_23802", at = @At("HEAD"))
    private static void bppResetLocalCtx(CallbackInfo info) {
        nextLoomPatternIndex = 0;
        loomPatterns = LoomPatternRenderContext.getLoomPatterns();
    }

    /**
     * Renders Banner++ loom patterns in line with vanilla banner patterns.
     */
    @Inject(
        method = "method_23802",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;get(I)Ljava/lang/Object;",
            ordinal = 0,
            remap = false
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void bppPatternRenderInline(MatrixStack stack, VertexConsumerProvider provider,
            int haha, int no, ModelPart part, SpriteIdentifier spriteId, boolean notShield,
            List<Pair<BannerPattern, DyeColor>> list, CallbackInfo info,
            int idx) {
        while(nextLoomPatternIndex < loomPatterns.size()) {
            LoomPatternData data = loomPatterns.get(nextLoomPatternIndex);
            if(data.index == idx - 1) {
                renderBppLoomPattern(data, stack, provider, part, haha, no, notShield);
                nextLoomPatternIndex++;
            } else {
                break;
            }
        }
    }

    /**
     * Renders Banner++ loom patterns that occur after all vanilla banner patterns.
     */
    @Inject(method = "method_23802", at = @At("RETURN"))
    private static void bppPatternRenderPost(MatrixStack stack, VertexConsumerProvider provider,
            int haha, int no, ModelPart part, SpriteIdentifier spriteId, boolean notShield,
            List<Pair<BannerPattern, DyeColor>> list, CallbackInfo info) {
        for(int i = nextLoomPatternIndex; i < loomPatterns.size(); i++) {
            renderBppLoomPattern(loomPatterns.get(i), stack, provider, part, haha, no, notShield);
        }
        loomPatterns = Collections.emptyList();
    }

    @Unique
    private static void renderBppLoomPattern(LoomPatternData data, MatrixStack stack, VertexConsumerProvider provider, ModelPart part, int haha, int no, boolean notShield) {
        Identifier spriteId = LoomPattern.getSpriteId(LoomPatterns.REGISTRY.getId(data.pattern), notShield ? "banner" : "shield");
        SpriteIdentifier realSpriteId = new SpriteIdentifier(notShield ? TexturedRenderLayers.BANNER_PATTERNS_ATLAS_TEXTURE : TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE, spriteId);
        float[] color = data.color.getColorComponents();
        part.render(stack, realSpriteId.getVertexConsumer(provider, RenderLayer::getEntityNoOutline), haha, no, color[0], color[1], color[2], 1.0f);
    }
}
