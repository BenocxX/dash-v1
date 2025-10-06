package lang.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileReader {
    public static String readSource(String path) throws IOException {
        byte[] bytes = readBytes(path);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static byte[] readBytes(String path) throws IOException {
        InputStream inputStream = FileReader.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + path);
        }

        byte[] bytes = inputStream.readAllBytes();
        inputStream.close();

        return bytes;
    }
}
