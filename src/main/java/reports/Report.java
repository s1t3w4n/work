package reports;

import java.io.FileNotFoundException;

public interface Report {
    void writeReport() throws FileNotFoundException;
}
