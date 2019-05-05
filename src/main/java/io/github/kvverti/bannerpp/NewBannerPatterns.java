package io.github.kvverti.bannerpp;

import com.chocohead.mm.api.ClassTinkerers;

public class NewBannerPatterns implements Runnable {

    @Override
    public void run() {
        boolean dev = "true".equalsIgnoreCase(System.getProperty("fabric.development"));
        String bannerPatternClass = "net.minecraft." + (dev ? "block.entity.BannerPattern" : "class_2582");
        ClassTinkerers.enumBuilder(bannerPatternClass, String.class, String.class)
            .addEnum("BANNERPP_PIG", "bannerpp_pig", "bpp_pig")
            .build();
    }
}
