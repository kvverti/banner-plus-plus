package io.github.kvverti.bannerpp;

import com.chocohead.mm.api.ClassTinkerers;

public class NewBannerPatterns implements Runnable {

    @Override
    public void run() {
        boolean dev = "true".equalsIgnoreCase(System.getProperty("fabric.development"));
        String bannerPatternClass = "net.minecraft." + (dev ? "block.entity.BannerPattern" : "class_2582");
        // dye banner patterns
        ClassTinkerers.enumBuilder(bannerPatternClass, String.class, String.class, String.class, String.class, String.class)
            .addEnum("BANNERPP_STRIPE_CENTER_FIFTH", "bannerpp_stripe_center_fifth", "bpp_scf", null, null, null)
            .build();
        // special banner patterns
        ClassTinkerers.enumBuilder(bannerPatternClass, String.class, String.class)
            .addEnum("BANNERPP_PIG", "bannerpp_pig", "bpp_pig")
            .build();
    }
}
