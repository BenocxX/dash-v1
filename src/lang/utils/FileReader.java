package lang.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class FileReader {
    public static String readSource(String path) throws IOException {
        byte[] bytes = readBytes(path);
        return new String(bytes, Charset.defaultCharset());
    }

    private static byte[] readBytes(String path) throws IOException {
        String filePath = Objects.requireNonNull(
                FileReader.class
                        .getClassLoader()
                        .getResource(path)
                        .getPath());
        return Files.readAllBytes(Paths.get(filePath));
    }
}
