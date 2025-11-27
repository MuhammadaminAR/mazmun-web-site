package uz.mazmun.mazmunweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 1) HTML – no-cache (har safar yangi markup)
        registry.addResourceHandler(
                        "/frontend/*.html"             // ildizdagi html
                ).addResourceLocations("classpath:/static/frontend/")
                .setCacheControl(CacheControl.noCache())   // ETag/If-None-Match bilan 304 qaytadi
                .resourceChain(true);

        // 2) CSS/JS/Fonts/SVG – 30 kun (hash bilan kelsa deyarli “immutable”)
        registry.addResourceHandler(
                        "/frontend/*.css",
                        "/frontend/*.js",
                        "/frontend/*.map",
                        "/frontend/*.svg",
                        "/frontend/*.woff",
                        "/frontend/*.woff2",
                        "/frontend/*.ttf"
                ).addResourceLocations("classpath:/static/frontend/")
                .setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic())
                .resourceChain(true);

        // 3) Images (static) – 30 kun
        registry.addResourceHandler(
                        "/frontend/*.png",
                        "/frontend/*.jpg",
                        "/frontend/*.jpeg",
                        "/frontend/*.webp",
                        "/frontend/*.gif",
                        "/frontend/*.ico"
                ).addResourceLocations("classpath:/static/frontend/")
                .setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic())
                .resourceChain(true);

        // 4) Uploads (server fayl tizimidan) – 30 kun
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/")
                .setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic())
                .resourceChain(true);
    }
}