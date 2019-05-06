package io.github.kvverti.bannerpp.mixin;

import io.github.kvverti.bannerpp.api.LoomPattern;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Adds a property to determine whether a BannerPattern needs an item in the loom.
 */
@Mixin(BannerPattern.class)
public abstract class MixinBannerPattern implements LoomPattern {

	private boolean needsItemInLoom;

	@Override
	public boolean requiresPatternItem() {
		return needsItemInLoom;
	}

	// set field via constructor injections

	@Inject(method = "<init>(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", at = @At("RETURN"))
	private void onRecipeConstructor(CallbackInfo ci) {
		needsItemInLoom = false;
	}

	@Inject(method = "<init>(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;)V", at = @At("RETURN"))
	private void onNewConstructor(CallbackInfo ci) {
		needsItemInLoom = true;
	}

	@Inject(method = "<init>(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Lnet/minecraft/item/ItemStack;)V", at = @At("RETURN"))
	private void onItemStackConstructor(String enumName, int ord, String name, String id, ItemStack stack, CallbackInfo ci) {
		// curly border and bricks use this constructor, but do not
		// need a pattern item in the loom
		if("cbo".equals(id) || "bri".equals(id)) {
			needsItemInLoom = false;
		} else {
			needsItemInLoom = true;
		}
	}
}
