package ru.my.reports;

import ru.my.fixers.NameFixer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TopicReport implements Report {
    private final File file;

    private final Pattern pattern;
    private final List<String> lines;

    public TopicReport(List<String> lines, String path, String name) {
        pattern = Pattern.compile("^V\\d+:");
        this.lines = lines;
        Pattern pattern = Pattern.compile(NameFixer.fix(name));
        File folder = new File(pattern.matcher(path).replaceFirst("orders/"));
        if (!folder.exists()) {
            folder.mkdir();
        }
        file = new File(pattern.matcher(path).replaceFirst("orders/topics-"
                + nameSet(name)));
    }

    @Override
    public void writeReport() {
        try (FileWriter writer = new FileWriter(file)) {
            for (String line : lines) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    int end = matcher.end();
                    int multiply = Integer.parseInt(line.substring(1, end - 1));
                    for (int i = 0; i < multiply - 1; i++) {
                        writer.write("\t");
                    }
                    writer.write(line);
                    writer.write(System.getProperty("line.separator"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
