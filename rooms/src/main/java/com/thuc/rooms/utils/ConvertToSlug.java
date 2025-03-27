package com.thuc.rooms.utils;

import com.github.slugify.Slugify;

public class ConvertToSlug {
    public static String convertToSlug(String input) {
        Slugify slugify = Slugify.builder().build();
        return slugify.slugify(input);
    }
}
