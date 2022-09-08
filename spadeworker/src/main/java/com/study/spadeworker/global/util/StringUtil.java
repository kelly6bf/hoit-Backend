package com.study.spadeworker.global.util;

import java.util.Arrays;
import java.util.List;

public class StringUtil {
    public static List<String> splitParameter(String input, String separator) {
        return input == null ? null : Arrays.asList(input.split(separator));
    }
}
