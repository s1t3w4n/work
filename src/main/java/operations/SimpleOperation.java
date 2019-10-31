package operations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleOperation implements Operation {

    private final Pattern pattern;
    private final String replacement;

    public SimpleOperation(String regex, String replacement) {
        pattern = Pattern.compile(regex);
        this.replacement = replacement;

    }

    @Override
    public String fix(String text) {
        Matcher matcher = pattern.matcher(text);
        text = matcher.replaceAll(replacement);
        return text;
    }

    @Override
    public boolean match(String text) {
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }
}
