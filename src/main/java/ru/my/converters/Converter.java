package ru.my.converters;

import com.aspose.words.License;

import java.io.InputStream;

public interface Converter {
    void convert() throws Exception;

    default boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("license/license.xml");
            License asposeLic = new License();
            asposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return !result;
    }
}
