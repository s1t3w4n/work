package fixers;

import operations.Operation;
import operations.SimpleOperation;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import readwrite.ReadWriteFiles;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class DocumentFixer {

    private final XWPFDocument document;

    private static final List<Operation> operations = new ArrayList<>();

    static {
        operations.add(new SimpleOperation("\t", " ")); // убрать табы из документа
        operations.add(new SimpleOperation("\u00A0", " ")); // убрать неразрывные пробелы
        operations.add(new SimpleOperation("\\s\\s", " ")); // убрать двойные пробелы
        operations.add(new SimpleOperation("^\\s", "")); // убрать пробелы вначале строки
        operations.add(new SimpleOperation("\\s$", "")); // убрать пробелы вконце строки
        operations.add(new SimpleOperation("\\s:", ":")); // убрать пробелы перед двоеточиями
        operations.add(new SimpleOperation(":\\s", ":")); // убрать пробелы после двоеточий
        operations.add(new SimpleOperation("^\u2013", "-")); // длинный минус
        operations.add(new SimpleOperation("^--", "-")); // два минуса
        operations.add(new SimpleOperation("^\\+\\+", "+")); // два плюса
        operations.add(new SimpleOperation("^\\+-", "+")); // плюс минус
        operations.add(new SimpleOperation("^Q:", "S:")); // замена старой маркировки
        operations.add(new SimpleOperation("\u00AC", "")); //¬ символ

    }

    public DocumentFixer(ReadWriteFiles readWriteFiles) throws IOException {
        document = readWriteFiles.readFile();
    }

    public XWPFDocument fixDocument() {
        XWPFDocument newDoc = new XWPFDocument();
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            String text = paragraph.getText();
            if (!text.isEmpty()) {
                String[] split = text.split("\n");
                for (String s : split) {
                    newDoc.createParagraph()
                            .createRun()
                            .setText(s);
                }
            }
        }
        return fixText(newDoc);
    }

    private XWPFDocument fixText(XWPFDocument document) {
        XWPFDocument newDoc = new XWPFDocument();
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            String text = paragraph.getText();
            XWPFParagraph newPar = newDoc.createParagraph();
            newPar.setSpacingAfter(0);
            XWPFRun run = newPar.createRun();
            for (int i = 0; i < operations.size(); i++) {
                Pattern pattern = Pattern.compile(operations.get(i).get());
                if (pattern.matcher(text).find()) {
                    text = operations.get(i).fix(text);
                    i = 0;
                }
            }
            run.setText(text);
            run.setFontSize(14);
            run.setFontFamily("Times New Roman");
        }
        return newDoc;
    }

}
