import fixers.DocumentFixer;
import readwrite.ReadWriteFiles;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        final File folder = new File("src/main/resources");
        listFilesForFolder(folder);
    }

    private static void listFilesForFolder(final File folder) throws IOException {
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (!fileEntry.isDirectory()
                    && (fileEntry.getName().endsWith(".doc")
                    || (fileEntry.getName().endsWith(".docx")))
                    && (!fileEntry.getName().startsWith("fixed-"))) {
                doMagic(fileEntry.getPath());
            }
        }
    }

    private static void doMagic(String filePath) throws IOException {
        ReadWriteFiles readWriteFiles = new ReadWriteFiles(filePath);
        DocumentFixer documentFixer = new DocumentFixer(readWriteFiles);
        readWriteFiles.writeFile(documentFixer.fixDocument());
    }

}




