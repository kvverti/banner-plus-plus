package io.github.kvverti.bannerpp;

import com.chocohead.mm.api.ClassTinkerers;

public class NewBannerPatterns implements Runnable {

    @Override
    public void run() {
        boolean dev = "true".equalsIgnoreCase(System.getProperty("fabric.development"));
        String bannerPatternClass = "net.minecraft." + (dev ? "block.entity.BannerPattern" : "class_2582");
        // dye banner patterns
        ClassTinkerers.enumBuilder(bannerPatternClass, String.class, String.class, String.class, String.class, String.class)
            .addEnum("BANNERPP_GRADIENT_RIGHT", "bannerpp_gradient_right", "bpp_gr", null, null, null)
            .addEnum("BANNERPP_GRADIENT_LEFT", "bannerpp_gradient_left", "bpp_gl", null, null, null)
            .addEnum("BANNERPP_STRIPE_RIGHT_FIFTH", "bannerpp_stripe_right_fifth", "bpp_rf", null, null, null)
            .addEnum("BANNERPP_STRIPE_CENTER_RIGHT_FIFTH", "bannerpp_stripe_center_right_fifth", "bpp_crf", null, null, null)
            .addEnum("BANNERPP_STRIPE_CENTER_FIFTH", "bannerpp_stripe_center_fifth", "bpp_cf", null, null, null)
            .addEnum("BANNERPP_STRIPE_CENTER_LEFT_FIFTH", "bannerpp_stripe_center_left_fifth", "bpp_clf", null, null, null)
            .addEnum("BANNERPP_STRIPE_LEFT_FIFTH", "bannerpp_stripe_left_fifth", "bpp_lf", null, null, null)
            .addEnum("BANNERPP_STRIPE_UPPER_FIFTH", "bannerpp_stripe_upper_fifth", "bpp_uf", null, null, null)
            .addEnum("BANNERPP_STRIPE_MIDDLE_UPPER_FIFTH", "bannerpp_stripe_middle_upper_fifth", "bpp_muf", null, null, null)
            .addEnum("BANNERPP_STRIPE_MIDDLE_FIFTH", "bannerpp_stripe_middle_fifth", "bpp_mf", null, null, null)
            .addEnum("BANNERPP_STRIPE_MIDDLE_LOWER_FIFTH", "bannerpp_stripe_middle_lower_fifth", "bpp_mof", null, null, null)
            .addEnum("BANNERPP_STRIPE_LOWER_FIFTH", "bannerpp_stripe_lower_fifth", "bpp_of", null, null, null)
            .addEnum("BANNERPP_STRIPE_RIGHT_FORTH", "bannerpp_stripe_right_forth", "bpp_r4", null, null, null)
            .addEnum("BANNERPP_STRIPE_CENTER_RIGHT_FORTH", "bannerpp_stripe_center_right_forth", "bpp_cr4", null, null, null)
            .addEnum("BANNERPP_STRIPE_CENTER_LEFT_FORTH", "bannerpp_stripe_center_left_forth", "bpp_cl4", null, null, null)
            .addEnum("BANNERPP_STRIPE_LEFT_FORTH", "bannerpp_stripe_left_forth", "bpp_l4", null, null, null)
            .addEnum("BANNERPP_STRIPE_UPPER_FORTH", "bannerpp_stripe_upper_forth", "bpp_u4", null, null, null)
            .addEnum("BANNERPP_STRIPE_MIDDLE_UPPER_FORTH", "bannerpp_stripe_middle_upper_forth", "bpp_mu4", null, null, null)
            .addEnum("BANNERPP_STRIPE_MIDDLE_LOWER_FORTH", "bannerpp_stripe_middle_lower_forth", "bpp_mo4", null, null, null)
            .addEnum("BANNERPP_STRIPE_LOWER_FORTH", "bannerpp_stripe_lower_forth", "bpp_o4", null, null, null)
            .build();
        // special banner patterns
        ClassTinkerers.enumBuilder(bannerPatternClass, String.class, String.class)
            .addEnum("BANNERPP_PIG", "bannerpp_pig", "bpp_pig")
            .build();
    }
}
