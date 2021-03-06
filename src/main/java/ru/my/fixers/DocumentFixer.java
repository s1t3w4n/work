package ru.my.fixers;

import ru.my.operations.AnswerOperation;
import ru.my.operations.HeadOperation;
import ru.my.operations.Operation;
import ru.my.operations.SimpleOperation;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import ru.my.readwrite.ReadWriteFiles;

import java.io.IOException;
import java.util.*;

public class DocumentFixer {

    private final XWPFDocument document;

    private static final List<Operation> operations = new ArrayList<>();

    static {
        operations.add(new SimpleOperation("\t", " ")); // убрать табы из документа
        operations.add(new SimpleOperation("\u2006", " ")); // убрать убра какие-то порбелы
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
        operations.add(new SimpleOperation("^\u2160", "I")); // странная ишка
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
        operations.add(new AnswerOperation("^\\+", "^-", "^R\\d+", "^L\\d+", "^\\d")); // однообразные операции надо всем вариантами ответов
        operations.add(new SimpleOperation("::", ":")); //двойные двоеточия - зло
    }

    public DocumentFixer(ReadWriteFiles readWriteFiles) throws IOException {
        document = readWriteFiles.readFile();
    }

    public List<String> fixDocument() {
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

    private List<String> fixText(List<String> lines) {
        List<String> newLines = new ArrayList<>();
        for (String line : lines) {
            if (!line.isEmpty()) {
                for (int i = 0; i < operations.size(); i++) {
                    if (operations.get(i).match(line)) {
                        line = operations.get(i).fix(line);
                        i = 0;
                    }
                }
            }
            newLines.add(line);
        }
        return newLines;
    }

}
