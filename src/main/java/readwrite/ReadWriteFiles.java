package readwrite;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
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

    public void writeFile(XWPFDocument document) throws IOException {
        String path = file.getPath();
        String name = file.getName();
        Pattern whatever = Pattern.compile(name);
        FileOutputStream out = new FileOutputStream(whatever.matcher(path).replaceFirst("fixed-" + name));
        document.write(out);
        out.close();
        document.close();
    }
}
