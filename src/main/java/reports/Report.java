package reports;

import java.io.FileNotFoundException;

public interface Report {
    void writeReport() throws FileNotFoundException;

    default String nameSet(String name) {
        String[] strings = new String[]{".doc", ".docx", ".rtf"};
        for (String string : strings) {
            if (name.endsWith(string)) {
                return name.replaceAll(string, ".txt");
            }
        }
        return name + ".txt";
    }
}
