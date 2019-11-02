package operations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeadOperation implements Operation {

    private final Pattern pattern;
    private final String replacement;
    private final String pare;

    public HeadOperation(String regex, String pare) {
        pattern = Pattern.compile(regex);
        replacement = regex + pare;
        this.pare = pare;
    }

    @Override
    public String fix(String text) {
        Matcher matcher = pattern.matcher(text);
        matcher.find();
        int end = matcher.end();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(text, 0, end);
        stringBuilder.append(pare);
        stringBuilder.append(text.substring(end));
        return stringBuilder.toString();
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
