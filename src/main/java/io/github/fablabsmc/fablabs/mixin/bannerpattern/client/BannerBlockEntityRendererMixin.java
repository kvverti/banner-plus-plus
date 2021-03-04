package io.github.fablabsmc.fablabs.mixin.bannerpattern.client;

import java.util.Collections;
import java.util.List;

import com.mojang.datafixers.util.Pair;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatterns;
import io.github.fablabsmc.fablabs.impl.bannerpattern.LoomPatternData;
import io.github.fablabsmc.fablabs.impl.bannerpattern.LoomPatternRenderContext;
import io.github.fablabsmc.fablabs.impl.bannerpattern.iface.LoomPatternContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

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
	private void preBppPatternRender(
			BannerBlockEntity banner,
			float f1,
			MatrixStack stack,
			VertexConsumerProvider provider,
			int i,
			int j,
			CallbackInfo info) {
		LoomPatternRenderContext.setLoomPatterns(((LoomPatternContainer) banner).bannerpp_getLoomPatterns());
	}

	@Inject(method = "renderCanvas", at = @At("HEAD"))
	private static void bppResetLocalCtx(CallbackInfo info) {
		nextLoomPatternIndex = 0;
		loomPatterns = LoomPatternRenderContext.getLoomPatterns();
	}

	/**
	 * Renders Banner++ loom patterns in line with vanilla banner patterns.
	 */
	@Inject(
			method = "renderCanvas",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;get(I)Ljava/lang/Object;",
					ordinal = 0,
					remap = false
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private static void bppPatternRenderInline(
			MatrixStack stack,
			VertexConsumerProvider provider,
			int haha,
			int no,
			ModelPart part,
			SpriteIdentifier spriteId,
			boolean notShield,
			List<Pair<BannerPattern, DyeColor>> list,
			boolean glint,
			CallbackInfo info,
			int idx) {
		while (nextLoomPatternIndex < loomPatterns.size()) {
			LoomPatternData data = loomPatterns.get(nextLoomPatternIndex);

			if (data.index == idx - 1) {
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
	@Inject(method = "renderCanvas", at = @At("RETURN"))
	private static void bppPatternRenderPost(
			MatrixStack stack,
			VertexConsumerProvider provider,
			int haha,
			int no,
			ModelPart part,
			SpriteIdentifier spriteId,
			boolean notShield,
			List<Pair<BannerPattern, DyeColor>> list,
			boolean glint,
			CallbackInfo info) {
		for (int i = nextLoomPatternIndex; i < loomPatterns.size(); i++) {
			renderBppLoomPattern(loomPatterns.get(i), stack, provider, part, haha, no, notShield);
		}

		loomPatterns = Collections.emptyList();
	}

	@Unique
	private static void renderBppLoomPattern(
			LoomPatternData data,
			MatrixStack stack,
			VertexConsumerProvider provider,
			ModelPart part,
			int haha,
			int no,
			boolean notShield) {
		Identifier spriteId = data.pattern.getSpriteId(notShield ? "banner": "shield");
		SpriteIdentifier realSpriteId = new SpriteIdentifier(notShield ? TexturedRenderLayers.BANNER_PATTERNS_ATLAS_TEXTURE : TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE, spriteId);
		float[] color = data.color.getColorComponents();
		part.render(stack, realSpriteId.getVertexConsumer(provider, RenderLayer::getEntityNoOutline), haha, no, color[0], color[1], color[2], 1.0f);
	}
}
