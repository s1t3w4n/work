package reports;

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
        String[] regexs = new String[]{"^V\\d+:", "^I:", "^S:", "^\\+:", "^-:", "^R\\d+:", "^L\\d+:", "^\\d:"};
        for (String regex : regexs) {
            patterns.add(Pattern.compile(regex));
        }
        this.lines = lines;
        Pattern pattern = Pattern.compile(name);
        file = new File(pattern.matcher(path).replaceFirst("lines-order-"
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
                if (print) {
                    writer.write(line);
                    writer.write("\n\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
