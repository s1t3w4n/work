import converters.Converter;
import converters.DOCConverter;
import converters.RTFConverter;
import fixers.DocumentFixer;
import readwrite.ReadWriteFiles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws IOException {
        final File folder = new File(System.getProperty("user.dir"));
        convert(folder);
        listFilesForFolder(folder);
    }

    private static void listFilesForFolder(final File folder) throws IOException {
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (!fileEntry.isDirectory()
                    && fileEntry.getName().endsWith(".docx")
                    && (!fileEntry.getName().startsWith("fixed-"))) {
                doMagic(fileEntry.getPath());
            }
        }
    }

    private static void convert(final File folder) {
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            List<Converter> toConvert = new ArrayList<>();
            if ((!fileEntry.isDirectory()
                    && fileEntry.getName().endsWith(".doc"))) {
                toConvert.add(new DOCConverter(fileEntry.getPath()));
            }
            if ((!fileEntry.isDirectory()
                    && fileEntry.getName().endsWith(".rtf"))) {
                toConvert.add(new RTFConverter(fileEntry.getPath()));
            }
            for (Converter converter : toConvert) {
                try {
                    converter.convert();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void doMagic(String filePath) throws IOException {
        ReadWriteFiles readWriteFiles = new ReadWriteFiles(filePath);
        DocumentFixer documentFixer = new DocumentFixer(readWriteFiles);
        readWriteFiles.writeFile(documentFixer.fixDocument());
    }

}




