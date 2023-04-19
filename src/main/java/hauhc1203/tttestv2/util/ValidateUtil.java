package hauhc1203.tttestv2.util;

public class ValidateUtil {
    public static boolean isEmptyString(String str) {
        return str == null || str.equals("") || str.equals("null");
    }

    public static boolean isValidStrings(String... strs) {
        for (String str : strs
        ) {
            if (isEmptyString(str))
                return false;
        }
        return true;
    }
}
