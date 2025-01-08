package com.tdtu.user_services.utils;
import java.text.Normalizer;
import java.util.regex.Pattern;
public class StringUtils {

    public static String removeVietnameseAccents(String s) {
        if (s == null) {
            return null;
        }
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("đ", "d").replaceAll("Đ", "D");
    }

    public static String toSlug(String s) {
        if (s == null) {
            return null;
        }
        return removeVietnameseAccents(s)
                .toLowerCase()
                .replaceAll(" ", "")
                .replaceAll("[^a-z0-9-]", "");
    }
}