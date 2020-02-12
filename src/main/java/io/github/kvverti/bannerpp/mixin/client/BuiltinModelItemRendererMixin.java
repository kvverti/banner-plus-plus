package io.github.kvverti.bannerpp.mixin.client;

import io.github.kvverti.bannerpp.iface.LoomPatternConversions;
import io.github.kvverti.bannerpp.iface.LoomPatternRenderContext;

import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Sets loom pattern context for shield rendering.
 */
@Mixin(BuiltinModelItemRenderer.class)
public abstract class BuiltinModelItemRendererMixin {

    @Shadow @Final private BannerBlockEntity renderBanner;

    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/block/entity/BannerBlockEntityRenderer;method_23802(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/SpriteIdentifier;ZLjava/util/List;)V"
        )
    )
    private void setBppLoomPatterns(ItemStack itemStack, MatrixStack matrixStack, VertexConsumerProvider provider, int i, int j, CallbackInfo info) {
        ListTag tag = LoomPatternConversions.getLoomPatternTag(itemStack);
        LoomPatternRenderContext.setLoomPatterns(LoomPatternConversions.makeLoomPatternData(tag));
    }
}
