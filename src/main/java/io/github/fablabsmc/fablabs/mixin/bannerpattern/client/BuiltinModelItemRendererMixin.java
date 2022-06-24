package io.github.fablabsmc.fablabs.mixin.bannerpattern.client;

import io.github.fablabsmc.fablabs.impl.bannerpattern.LoomPatternConversions;
import io.github.fablabsmc.fablabs.impl.bannerpattern.LoomPatternRenderContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;

/**
 * Sets loom pattern context for shield rendering.
 */
@Mixin(BuiltinModelItemRenderer.class)
public abstract class BuiltinModelItemRendererMixin {
	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/render/block/entity/BannerBlockEntityRenderer;renderCanvas(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/SpriteIdentifier;ZLjava/util/List;Z)V"
			)
	)
	private void setBppLoomPatterns(ItemStack itemStack, ModelTransformation.Mode mode, MatrixStack matrixStack, VertexConsumerProvider provider, int i, int j, CallbackInfo info) {
		NbtList tag = LoomPatternConversions.getLoomPatternNbt(itemStack);
		LoomPatternRenderContext.setLoomPatterns(LoomPatternConversions.makeLoomPatternData(tag));
	}
}
