package operations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleOperation implements Operation {

    private final String regex;
    private final String replacement;

    public SimpleOperation(String regex, String replacement) {
        this.regex = regex;
        this.replacement = replacement;
    }

    @Override
    public String fix(String text) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        text = matcher.replaceAll(replacement);
        return text;
    }

    @Override
    public String get() {
        return regex;
    }
}
