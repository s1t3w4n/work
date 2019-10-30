import fixers.DocumentFixer;
import readwrite.ReadWriteFiles;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ReadWriteFiles readWriteFiles = new ReadWriteFiles("src/main/resources/test.docx");
        DocumentFixer documentFixer = new DocumentFixer(readWriteFiles);
        readWriteFiles.writeFile(documentFixer.fixDocument());
    }
}
