package operations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeadOperation implements Operation {

    private final Pattern pattern;
    private final String replacement;

    public HeadOperation(String regex, String pare) {
        pattern = Pattern.compile(regex);
        replacement = regex+pare;
    }

    @Override
    public String fix(String text) {
        Matcher matcher = pattern.matcher(text);
        text = matcher.replaceAll(replacement.substring(1));
        return text;
    }

    @Override
    public boolean match(String text) {
        Matcher first = pattern.matcher(text);
        if (first.find()) {
            Matcher second = Pattern.compile(replacement).matcher(text);
            return !second.find();
        } else {
            return false;
        }
    }

}
