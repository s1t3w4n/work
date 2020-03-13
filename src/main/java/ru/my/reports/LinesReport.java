package ru.my.reports;

import ru.my.fixers.NameFixer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LinesReport implements Report {
    private final File file;

    private final List<Pattern> patterns;

    private final List<String> lines;

    public LinesReport(List<String> lines, String path, String name) {
        patterns = new ArrayList<>();
        String[] regexs = new String[]{"^V\\d+:", "^I:", "^S:", "^\\+:", "^-:", "^R\\d+:", "^L\\d+:", "^\\d:", "^F\\d+:"};
        for (String regex : regexs) {
            patterns.add(Pattern.compile(regex));
        }
        this.lines = lines;
        Pattern pattern = Pattern.compile(NameFixer.fix(name));
        File folder = new File(pattern.matcher(path).replaceFirst("orders/"));
        if (!folder.exists()) {
            folder.mkdir();
        }
        file = new File(pattern.matcher(path).replaceFirst("orders/lines-order-"
                + nameSet(name)));
    }

    @Override
    public void writeReport() {
        try (FileWriter writer = new FileWriter(file)) {
            for (String line : lines) {
                boolean print = true;
                for (Pattern pattern : patterns) {
                    if (pattern.matcher(line).find()) {
                        print = false;
                        break;
                    }
                }
                if (print && !line.isEmpty()) {
                    writer.write(line);
                    writer.write(System.getProperty("line.separator"));
                    writer.write(System.getProperty("line.separator"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
