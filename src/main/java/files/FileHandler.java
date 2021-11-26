package files;

import java.io.*;

public class FileHandler {







    public static byte[] loadFile(String fileName) {
        File file = new File(fileName);
        byte[] content = null;
        if (file.length() > 0) {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                content = inputStream.readAllBytes();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return content;
    }


}
