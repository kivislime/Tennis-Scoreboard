package org.kivislime.tennisscoreboard.util;

import java.util.UUID;
import java.util.regex.Pattern;

public class ValidatorUtil {
    private static final int MAX_NAME_LENGTH = 15;
    private static final int MIN_NAME_LENGTH = 2;
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z]+$");
    private static final Pattern PAGE_PATTERN = Pattern.compile("[1-9]\\d*");

    public static boolean isValidParameter(String parameter) {
        return parameter != null && !parameter.isBlank();
    }

    public static boolean isValidName(String name) {
        return isValidParameter(name) &&
                NAME_PATTERN.matcher(name).matches() &&
                name.length() <= MAX_NAME_LENGTH &&
                name.length() >= MIN_NAME_LENGTH;
    }

    public static boolean isValidUuid(String uuid) {
        if (!isValidParameter(uuid)) return false;
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isValidPage(String page) {
        return isValidParameter(page) && PAGE_PATTERN.matcher(page).matches();
    }
}
