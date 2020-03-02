package fixers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameFixer {

    private static final char[] META_SYMBOLS = "<([{\\^-=$!|]})?*+.>".toCharArray();

    public static String fix(String name) {

        for (char META_SYMBOL : META_SYMBOLS) {
            String character = Character.toString(META_SYMBOL);
            Pattern pattern = Pattern.compile(character);
            Matcher matcher = pattern.matcher(name);
            if (matcher.find()) {
                name = matcher.replaceAll("\\" + character);
            }
        }
        return name;
    }
}
