package converters;

import com.aspose.words.Document;

public class RTFConverter implements Converter {

    private final String filePath;

    public RTFConverter(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void convert() throws Exception {
        Document document = new Document(filePath);

        document.save(filePath + "x");
    }
}