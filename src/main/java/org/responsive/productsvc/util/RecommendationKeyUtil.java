package org.responsive.productsvc.util;

import java.util.StringJoiner;
import java.util.Objects;

/**
 * Utility to create a stable normalized key for recommendation queries.
 */
public final class RecommendationKeyUtil {

    private RecommendationKeyUtil() {}

    public static String buildKey(Long minPrice, Long maxPrice, String type, String category, Integer age) {
        StringJoiner sj = new StringJoiner("|");
        sj.add("min:" + (minPrice == null ? "" : minPrice));
        sj.add("max:" + (maxPrice == null ? "" : maxPrice));
        sj.add("type:" + nullSafeLower(type));
        sj.add("category:" + nullSafeLower(category));
        sj.add("age:" + (age == null ? "" : age));
        return sj.toString();
    }

    private static String nullSafeLower(String s) {
        return s == null ? "" : s.trim().toLowerCase();
    }
}
