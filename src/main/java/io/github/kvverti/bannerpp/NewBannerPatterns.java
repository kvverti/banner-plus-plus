package io.github.kvverti.bannerpp;

import com.chocohead.mm.api.ClassTinkerers;

public class NewBannerPatterns implements Runnable {

    @Override
    public void run() {
        boolean dev = "true".equalsIgnoreCase(System.getProperty("fabric.development"));
        String bannerPatternClass = "net.minecraft." + (dev ? "block.entity.BannerPattern" : "class_2582");
        // dye banner patterns
        ClassTinkerers.enumBuilder(bannerPatternClass, String.class, String.class, String.class, String.class, String.class)
            .addEnum("BANNERPP_STRIPE_CENTER_LEFT_FIFTH", "bannerpp_stripe_center_left_fifth", "bpp_clf", null, null, null)
            .addEnum("BANNERPP_STRIPE_CENTER_RIGHT_FIFTH", "bannerpp_stripe_center_right_fifth", "bpp_crf", null, null, null)
            .addEnum("BANNERPP_STRIPE_MIDDLE_UPPER_FIFTH", "bannerpp_stripe_middle_upper_fifth", "bpp_muf", null, null, null)
            .addEnum("BANNERPP_STRIPE_MIDDLE_LOWER_FIFTH", "bannerpp_stripe_middle_lower_fifth", "bpp_mof", null, null, null)
            .addEnum("BANNERPP_STRIPE_CENTER_LEFT_FORTH", "bannerpp_stripe_center_left_forth", "bpp_cl4", null, null, null)
            .addEnum("BANNERPP_STRIPE_CENTER_RIGHT_FORTH", "bannerpp_stripe_center_right_forth", "bpp_cr4", null, null, null)
            .addEnum("BANNERPP_STRIPE_MIDDLE_UPPER_FORTH", "bannerpp_stripe_middle_upper_forth", "bpp_mu4", null, null, null)
            .addEnum("BANNERPP_STRIPE_MIDDLE_LOWER_FORTH", "bannerpp_stripe_middle_lower_forth", "bpp_mo4", null, null, null)
            .build();
        // special banner patterns
        ClassTinkerers.enumBuilder(bannerPatternClass, String.class, String.class)
            .addEnum("BANNERPP_PIG", "bannerpp_pig", "bpp_pig")
            .build();
    }
}
