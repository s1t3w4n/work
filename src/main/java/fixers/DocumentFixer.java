package fixers;

import operations.AnswerOperation;
import operations.HeadOperation;
import operations.Operation;
import operations.SimpleOperation;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import readwrite.ReadWriteFiles;

import java.io.IOException;
import java.util.*;

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
        operations.add(new SimpleOperation("^I\\.", "I:")); // замена старой маркировки
        operations.add(new SimpleOperation("^I;", "I:")); // замена старой маркировки
        operations.add(new SimpleOperation("^I,", "I:")); // замена старой маркировки
        operations.add(new SimpleOperation("^S\\.", "S:")); // замена старой маркировки
        operations.add(new SimpleOperation("^S;", "S:")); // замена старой маркировки
        operations.add(new SimpleOperation("^S,", "S:")); // замена старой маркировки
        operations.add(new SimpleOperation("^R\\s", "R")); // правый с проелом
        operations.add(new SimpleOperation("^L\\s", "L")); // левый с пробелом
        operations.add(new SimpleOperation("^V\\s", "V")); // глава с пробелом
        operations.add(new SimpleOperation("^V-", "V")); // глава с минусом
        operations.add(new SimpleOperation("\u00AC", "")); //¬ символ
        operations.add(new SimpleOperation("\\.\\.\\.", "\u2026")); //сохранение троеточий
        operations.add(new SimpleOperation("^V\\s", "V")); // замена старой маркировки
        operations.add(new HeadOperation("^I", ":")); //Двоеточия после I
        operations.add(new HeadOperation("^S", ":")); //Двоеточия после S
        operations.add(new HeadOperation("^V\\d+", ":")); //Двоеточия после V b цифры
        operations.add(new AnswerOperation("^\\+","^-","^R\\d+","^L\\d+", "^\\d")); // однообразные операции надо всем вариантами ответов
        operations.add(new SimpleOperation("::", ":")); //двойные двоеточия - зло
    }

    public DocumentFixer(ReadWriteFiles readWriteFiles) throws IOException {
        document = readWriteFiles.readFile();
    }

    public XWPFDocument fixDocument() {
        List<String> lines = new ArrayList<>();
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            String text = paragraph.getText();
            if (!text.isEmpty()) {
                String[] split = text.split("\n");
                lines.addAll(Arrays.asList(split));
            }
        }
        return fixText(lines);
    }

    private XWPFDocument fixText(List<String> lines) {
        XWPFDocument newDoc = new XWPFDocument();
        for (String line : lines) {
            if (!line.isEmpty()) {
                XWPFParagraph newPar = newDoc.createParagraph();
                newPar.setSpacingAfter(0);
                XWPFRun run = newPar.createRun();
                for (int i = 0; i < operations.size(); i++) {
                    if (operations.get(i).match(line)) {
                        line = operations.get(i).fix(line);
                        i = 0;
                    }
                }
                run.setText(line);
                run.setFontSize(14);
                run.setFontFamily("Times New Roman");
            }
        }
        return newDoc;
    }

}
