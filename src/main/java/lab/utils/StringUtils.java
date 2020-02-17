package lab.utils;

public class StringUtils {
    public static Integer toInteger(String intString) {
        Integer number = null;
        try {
            number = Integer.parseInt(intString);
        } catch (Exception error) {}
        return number;
    }
}
