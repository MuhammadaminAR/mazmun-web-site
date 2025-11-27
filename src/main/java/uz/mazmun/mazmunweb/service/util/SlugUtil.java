package uz.mazmun.mazmunweb.service.util;

import java.util.Locale;
import java.util.UUID;

public final class SlugUtil {
    public static String toSlug(String s) {
        String slug = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
        return slug.isBlank() ? UUID.randomUUID().toString().substring(0,8) : slug;
    }
}
