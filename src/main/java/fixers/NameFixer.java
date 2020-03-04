package fixers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameFixer {

    private static final char[] META_SYMBOLS = "<([{^-=$!|]})?*+.>".toCharArray();

    public static String fix(String name) {

        for (char symbol : META_SYMBOLS) {
            StringBuilder character = new StringBuilder("\\");
            character.append(Character.toString(symbol));
            Pattern pattern = Pattern.compile(character.toString());
            Matcher matcher = pattern.matcher(name);
            if (matcher.find()) {
                name = matcher.replaceAll("\\" + character);
            }
        }
        return name;
    }
}
