package org.kivislime.tennisscoreboard;

import java.util.UUID;
import java.util.regex.Pattern;

public class ValidatorUtil {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z]+$");

    public static boolean isValidParameter(String name) {
        return name != null && !name.isBlank();
    }

    public static boolean isValidName(String name) {
        return isValidParameter(name) && NAME_PATTERN.matcher(name).matches();
    }

    public static boolean isValidUuid(String uuid) {
        if (isValidParameter(uuid)) return false;
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
