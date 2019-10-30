package fixers;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import readwrite.ReadWriteFiles;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentFixer {

    private final XWPFDocument document;

    private static final Map<String,String> options = new HashMap<>();

    static {
        options.put("\u00A0", " ");
        options.put("\t", " ");
        options.put("\\s\\s", " ");
        options.put("^\\s", "");
        options.put("\\s$", "");
        options.put(" :", ":");
        options.put("^\u2013", "-");
        options.put("\u00AD", "");
        options.put("", "");
        options.put("^Q:", "S:");
        //options.put("^-\\w", "-:");

    }

    public DocumentFixer(ReadWriteFiles readWriteFiles) throws IOException {
        document = readWriteFiles.readFile();
    }

    public XWPFDocument fixDocument() {
        List<String> keys = new ArrayList<>(options.keySet());
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                String runText = run.getText(0);
                if (Objects.nonNull(runText)) {
                    for (int i = 0; i < options.size(); i++) {
                        Pattern pattern = Pattern.compile(keys.get(i));
                        if (pattern.matcher(runText).find()) {
                            runText = changeSymbols(runText, keys.get(i));
                            i = 0;
                        }
                    }
                    run.setText(runText, 0);
                }
            }
        }
        return document;
    }

    private String changeSymbols(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        text = matcher.replaceAll(options.get(regex));
        return text;
    }
}
