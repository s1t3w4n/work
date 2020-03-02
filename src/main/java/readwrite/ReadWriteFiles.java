package readwrite;

import fixers.NameFixer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import reports.LinesReport;
import reports.TopicReport;

import java.io.*;
import java.util.List;
import java.util.regex.Pattern;

public class ReadWriteFiles {

    private final File file;

    public ReadWriteFiles(String path) {
        file = new File(path);
    }

    public XWPFDocument readFile() throws IOException {
        FileInputStream fs = new FileInputStream(file.getPath());
        return new XWPFDocument(fs);
    }

    public void writeFile(List<String> lines) throws IOException {
        String path = file.getPath();
        String name = file.getName();


        Pattern whatever = Pattern.compile(NameFixer.fix(name));
        XWPFDocument document = fixText(lines);
        FileOutputStream out = new FileOutputStream(whatever.matcher(path).replaceFirst("fixed-" + name));
        document.write(out);
        out.close();
        document.close();

        new LinesReport(lines, path, name).writeReport();
        new TopicReport(lines, path, name).writeReport();
    }

    private XWPFDocument fixText(List<String> lines) {
        XWPFDocument newDoc = new XWPFDocument();
        for (String line : lines) {
            if (!line.isEmpty()) {
                XWPFParagraph newPar = newDoc.createParagraph();
                newPar.setSpacingAfter(0);
                XWPFRun run = newPar.createRun();
                run.setText(line);
                run.setFontSize(14);
                run.setFontFamily("Times New Roman");
            }
        }
        return newDoc;
    }

}
