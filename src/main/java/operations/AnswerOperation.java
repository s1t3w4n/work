package operations;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnswerOperation implements Operation {
    private final String[] symbols = new String[]{"\\.", ",", ";", "\\s"};
    private final List<Pattern> patterns;

    private final char pare = ':';
    private final List<Pattern> endWith;

    public AnswerOperation(String... regexes) {
        patterns = new ArrayList<>();
        for (String regex : regexes) {
            patterns.add(Pattern.compile(regex));
        }
        String[] badEnds = new String[]{";", "\\.", ":", ","};
        endWith = new ArrayList<>();
        for (String badEnd : badEnds) {
            endWith.add(Pattern.compile(badEnd + "$"));
        }
    }

    @Override
    public String fix(String text) {
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                for (String symbol : symbols) {
                    Matcher extraMatcher = Pattern.compile(pattern.toString() + symbol).matcher(text);
                    if (extraMatcher.find()) {
                        int end = extraMatcher.end();
                        return changeEnding(text.substring(0, end - 1) + pare +
                                text.substring(end));
                    }
                }
                int end = matcher.end();
                return changeEnding(text.substring(0, end) + pare +
                        text.substring(end));
            }
        }
        return changeEnding(text);
    }

    @Override
    public boolean match(String text) {
        if (!Pattern.compile("^\\d\\d+").matcher(text).find()) {
            for (Pattern pattern : patterns) {
                Matcher first = pattern.matcher(text);
                if (first.find()) {
                    Matcher second = Pattern.compile(pattern.toString() + pare).matcher(text);
                    if (!second.find()) {
                        return true;
                    } else {
                        if (isBadEnding(text)) {
                            return !isEmptyAnswer(text);
                        }
                    }
                }
            }
        }
        return false;
    }

    private String changeEnding(String text) {
        for (int i = 0; i < endWith.size(); i++) {
            while (endWith.get(i).matcher(text).find()) {
                text = endWith.get(i).matcher(text).replaceFirst("");
                i = 0;
            }
        }
        return text;
    }

    private boolean isBadEnding(String text) {
        for (Pattern pattern1 : endWith) {
            if (pattern1.matcher(text).find()) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmptyAnswer(String text) {
        for (Pattern pattern : patterns) {
            if (Pattern.compile(pattern.toString() + pare + "$").matcher(text).find()) {
                return true;
            }
        }
        return false;
    }
}
