package converters;

import com.aspose.words.Document;

public class DOCConverter implements Converter {

    private final String filePath;

    public DOCConverter(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void convert() throws Exception {
        if (getLicense()) {
            return;
        }
        if (filePath.endsWith(".doc")) {
            Document document = new Document(filePath);
            document.save(filePath + "x");
        }
    }

}
